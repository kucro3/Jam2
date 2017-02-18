package org.kucro3.jam2.jar;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.jar.Manifest;

import org.kucro3.jam2.jar.Jar.Resource.ResourceOutputStream;
import org.kucro3.jam2.util.Jam2Util;
import org.kucro3.jam2.visitor.cache.ClassCacheVisitor;
import org.kucro3.jam2.visitor.cache.RevisitationHelper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class Jar {
	public Jar()
	{
		this(new Manifest());
	}
	
	public Jar(Manifest manifest)
	{
		this.manifest = Objects.requireNonNull(manifest);
	}
	
	public Collection<ClassFile> getClasses()
	{
		return classes.values();
	}
	
	public Collection<Resource> getResources()
	{
		return resources.values();
	}
	
	public boolean containsClass(String name)
	{
		return classes.containsKey(name);
	}
	
	public boolean containsResource(String name)
	{
		return resources.containsKey(name);
	}
	
	public ClassFile getClass(String name)
	{
		return classes.get(name);
	}
	
	public Resource getResource(String name)
	{
		return resources.get(name);
	}
	
	public ClassFile addClass(String name)
	{
		checkClassDuplication(name);
		
		ClassFile cf = new ClassFile(name);
		classes.put(name, cf);
		return cf;
	}
	
	public Resource addResource(String name)
	{
		checkResourceDuplication(name);
		
		if(name.equals(MANIFEST_NAME))
			throw new IllegalArgumentException("You cannot create a manifest");
		Resource res = new Resource(name);
		res.ensureStateWiriting();
		resources.put(name, res);
		return res;
	}
	
	public void removeClass(String name)
	{
		classes.remove(name);
	}
	
	public void removeResource(String name)
	{
		resources.remove(name);
	}
	
	public Manifest getManifest()
	{
		return manifest;
	}
	
	public void transferAllClasses()
	{
		String[] keys = classes.keySet().toArray(new String[0]);
		for(String key : keys)
			classes.get(key).transfer();
	}
	
	public void transferClassResources()
	{
		String[] keys = resources.keySet().toArray(new String[0]);
		for(String key : keys)
		{
			Resource res = resources.get(key);
			if(Jam2Util.isClassResource(res.getResourceName()))
				res.transfer();
		}
	}
	
	protected void checkClassDuplication(String name)
	{
		if(classes.containsKey(name) 
		|| resources.containsKey(Jam2Util.fromInternalNameToResource(name)))
			throw new IllegalArgumentException("Class duplicated: " + name);
	}
	
	protected void checkResourceDuplication(String name)
	{
		if(resources.containsKey(name)
		|| (Jam2Util.isClassResource(name) && classes.containsKey(Jam2Util.fromResourceToInternalName(name))))
			throw new IllegalArgumentException("Resource duplicated: " + name);
	}
	
	protected final Map<String, ClassFile> classes = new HashMap<>();
	
	protected final Map<String, Resource> resources = new HashMap<>();
	
	protected Manifest manifest;
	
    public static final String MANIFEST_NAME = "META-INF/MANIFEST.MF";
	
	public class ClassFile extends ClassCacheVisitor
	{
		public ClassFile()
		{
		}
		
		public ClassFile(String name)
		{
			initName(name);
		}
		
		public byte[] toBytes()
		{
			ClassWriter cw = new ClassWriter(0);
			RevisitationHelper.revisit(this, cw);
			return cw.toByteArray();
		}
		
		void initName(String name)
		{
			super.setName(name);
		}
		
		@Override
		public void setName(String name)
		{
			if(name.equals(super.getName()))
				return;
			
			checkClassDuplication(name);
			
			Jar.this.classes.remove(getName());
			Jar.this.classes.put(name, this);
			super.setName(name);
		}
		
		public Resource transfer()
		{
			if(this.free)
				throw new IllegalStateException("Jar element escaped");
			
			String name = Jam2Util.fromInternalNameToResource(getName());
			
			assert !Jar.this.resources.containsKey(name);
			
			removeClass(getName());
			Resource res = addResource(name);
			
			res.ensureStateWiriting();
			ResourceOutputStream ros = res.getOutputStream();
			ros.write(toBytes());
			res.flipState();
			
			this.free = true;
			
			return res;
		}
		
		public final boolean isFree()
		{
			return free;
		}
		
		volatile boolean free = true;
	}
	
	public class Resource
	{
		public Resource(String resourceName)
		{
			this.resourceName = resourceName;
			this.ros = new ResourceOutputStream();
		}
		
		public ResourceState getState()
		{
			return state;
		}
		
		public ClassFile transfer()
		{
			if(this.free)
				throw new IllegalStateException("Jar element escaped");
			
			if(!Jam2Util.isClassResource(this.resourceName))
				throw new IllegalStateException("Not a class name: " + this.resourceName);
			
			checkStateSolid();
			
			String name = Jam2Util.fromResourceToInternalName(this.resourceName);
			
			assert !Jar.this.classes.containsKey(name);
			
			ClassFile cf = addClass(name);
			
			this.ensureStateSolid();
			ResourceInputStream ris = this.getInputStream();
			try {
				ClassReader cr = new ClassReader(ris);
				cr.accept(cf, 0);
			} catch (IOException e) {
				throw new RuntimeException(e); // unused
			}
			
			cf.initName(name); // forced
			
			Jar.this.resources.remove(this.resourceName);
			Jar.this.classes.put(name, cf);
			this.free = true;
			
			return cf;
		}
		
		public void rename(String name)
		{
			checkResourceDuplication(name);
			
			Jar.this.resources.remove(this.resourceName);
			Jar.this.resources.put(name, this);
			this.resourceName = name;
		}
		
		public String getResourceName()
		{
			return this.resourceName;
		}
		
		public ResourceOutputStream getOutputStream()
		{
			checkStateWriting();
			return ros;
		}
		
		public ResourceInputStream getInputStream()
		{
			checkStateSolid();
			return ris;
		}
		
		public synchronized ResourceState flipState()
		{
			if(this.state.equals(ResourceState.SOLID))
			{
				this.ris = null;
				this.state = ResourceState.WRITING;
				modCount++;
			}
			else
			{
				this.ris = new ResourceInputStream(this.ros.toByteArray(), ++modCount);
				this.state = ResourceState.SOLID;
			}
			return this.state;
		}
		
		public void ensureStateWiriting()
		{
			setState(ResourceState.WRITING);
		}
		
		public void ensureStateSolid()
		{
			setState(ResourceState.SOLID);
		}
		
		public void setState(ResourceState state)
		{
			Objects.requireNonNull(state);
			if(!this.state.equals(state))
				flipState();
		}
		
		public boolean isWriting()
		{
			return state.equals(ResourceState.WRITING);
		}
		
		public boolean isSolid()
		{
			return state.equals(ResourceState.SOLID);
		}
		
		protected void checkStateWriting()
		{
			if(!isWriting())
				throw new IllegalStateException("The state must be WRITING");
		}
		
		protected void checkStateSolid()
		{
			if(!isSolid())
				throw new IllegalStateException("The state must be SOLID");
		}
		
		public final boolean isFree()
		{
			return free;
		}
		
		protected ResourceOutputStream ros;
		
		protected ResourceInputStream ris;
		
		private ResourceState state = ResourceState.WRITING;
		
		private String resourceName;
		
		int modCount;
		
		volatile boolean free = true;
		
		public class ResourceOutputStream extends ByteArrayOutputStream
		{
			public ResourceOutputStream()
			{
				super();
			}
			
			public ResourceOutputStream(int size)
			{
				super(size);
			}
			
			@Override
			public void write(int b)
			{
				checkStateWriting();
				modCount++;
				super.write(b);
			}
			
			@Override
			public void write(byte[] b)
			{
				write(b, 0, b.length);
			}
			
			@Override
			public void write(byte[] b, int off, int len)
			{
				checkStateWriting();
				modCount++;
				super.write(b, off, len);
			}
			
			@Override
			public void writeTo(OutputStream os) throws IOException
			{
				checkStateSolid();
				super.writeTo(os);
			}
			
			@Override
			public byte[] toByteArray()
			{
				checkStateSolid();
				return super.toByteArray();
			}
			
			@Override
			public void reset()
			{
				checkStateWriting();
				modCount++;
				super.reset();
			}
		}
		
		public class ResourceInputStream extends ByteArrayInputStream
		{
			public ResourceInputStream(byte[] buf, int modCount)
			{
				super(buf);
				this.modCount = modCount;
			}
			
			@Override
			public int read()
			{
				checkStateSolid();
				checkModification();
				return super.read();
			}
			
			
			public int read(byte b[], int off, int len)
			{
				checkStateSolid();
				checkModification();
				return super.read(b, off, len);
			}
			
			protected void checkModification()
			{
				if(modCount != Resource.this.modCount)
					throw new ConcurrentModificationException();
			}
			
			public byte[] getBuffered()
			{
				return super.buf;
			}
			
			protected final int modCount;
		}
	}
	
	public static enum ResourceState
	{
		WRITING,
		SOLID
	}
}