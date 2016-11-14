package org.kucro3.jam2.jar;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Manifest;

import org.kucro3.jam2.opcode.Instruction;
import org.kucro3.jam2.opcode.InstructionContainer;
import org.kucro3.jam2.util.ClassContext;
import org.kucro3.jam2.util.Jam2Util;
import org.kucro3.jam2.util.Version;
import org.kucro3.jam2.util.builder.ClassBuilder;
import org.kucro3.jam2.visitor.ClassVisitorListener;
import org.kucro3.jam2.visitor.HookedClassVisitor;
import org.kucro3.jam2.visitor.InstructionVisitor;
import org.kucro3.util.Reference;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

public class JarPot implements Jar.Modifiable {
	public JarPot()
	{
		this.manifest = new Manifest();
		this.ensuringQueue = new EnsuringQueue();
		this.resources = new HashMap<>();
		this.classes = new HashMap<>();
		
		this._init();
	}
	
	public JarPot(Jar jar) throws IOException
	{
		this();
		_import(jar);
	}
	
	final void _import(Jar jar) throws IOException
	{
		for(String loc : jar.getResources())
			_importResource(loc, jar.getResourceAsStream(loc));
	}
	
	final void _importResource(String loc, InputStream is) throws IOException
	{
		if(JarFile.isClass(loc))
			_importClass(loc, is);
		else
		{
			ResourceImpl impl = new ResourceImpl(loc);
			OutputStream os = impl.getOutputStream();
			int b;
			while((b = is.read()) != -1)
				os.write(b);
			
			// I'm struggling for this
			// ((ByteArrayReferenceOutputStream) os).ref.reset();
			
			resources.put(loc, impl);
		}
	}
	
	final void _importClass(String loc, InputStream is) throws IOException
	{
		ClassReader cr = new ClassReader(is);
		ClassImpl clz = new ClassImpl(Version.getClassVersion(),
				cr.getAccess(), cr.getClassName(), null, cr.getSuperName(), cr.getInterfaces());
		InstructionVisitor insns = new InstructionVisitor(cctx);
		HookedClassVisitor hooked = new HookedClassVisitor(insns);
		hooked.setListener(new ClassListener(clz)); 
		cr.accept(hooked, 0);
		classes.put(JarFile.toClassName(loc), clz);
		resources.put(loc, clz);
	}
	
	private final void _init()
	{
		// Manifest resources
		final String resManifest = "META-INF/MANIFEST.MF";
		class $ResourceManifest implements UnremoveableResource.Modifiable, UnremoveableResource.Writeable
		{
			public InputStream asInputStream()
			{
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				try {
					manifest.write(bos);
				} catch (IOException e) {
					// unused
					return null;
				}
				return new ByteArrayInputStream(bos.toByteArray());
			}
			
			@Override
			public String getName()
			{
				return resManifest;
			}
			
			@Override
			public OutputStream getOutputStream() 
			{
				return ros;
			}
			
			@Override
			public void force()
			{
			}
			
			@Override
			public boolean ensured() 
			{
				return true;
			}
			
			@Override
			public void clear() 
			{
				manifest.clear();
				ros.setReference(new ByteArrayOutputStream());
			}
			
			private final ByteArrayReferenceOutputStream ros = new ByteArrayReferenceOutputStream(new ByteArrayOutputStream())
				{
					@Override
					public void flush() throws IOException
					{
						manifest.read(new ByteArrayInputStream(this.getReference().toByteArray()));
					}
				};
		}
		resources.put(resManifest, new $ResourceManifest());
		//
	}
	
	static InputStream optionalBIS(byte[] byts)
	{
		return new ByteArrayInputStream(byts == null ? new byte[0] : byts);
	}
	
	@Override
	public Manifest getManifest()
	{
		return manifest;
	}
	
	@Override
	public ClassLoader getClassLoader()
	{
		return Jam2Util.getInstance();
	}
	
	@Override
	public Resource getResource(String name)
	{
		return resources.get(name);
	}
	
	@Override
	public Collection<String> getResources()
	{
		return resources.keySet();
	}
	
	@Override
	public InputStream getResourceAsStream(String name)
	{
		Resource res = getResource(name);
		if(res == null)
			return null;
		return res.asInputStream();
	}
	
	@Override
	public boolean removeResource(String name)
	{
		Resource res = resources.get(name);
		if(res instanceof UnremoveableResource)
			return false;
		if(res instanceof UnremoveableResource.Modifiable)
			return false;
		if(res instanceof ClassImpl)
		{
			ClassImpl impl = (ClassImpl) res;
			classes.remove(impl.getClassName());
			ensuringQueue.remove(impl.hook);
		}
		return resources.remove(name) != null;
	}
	
	@Override
	public boolean ensureResources()
	{
		if(ensuringQueue.ensured)
			return false;
		ensuringQueue.ensureAll();
		return true;
	}
	
	@Override
	public boolean resourcesEnsured()
	{
		return ensuringQueue.ensured;
	}
	
	@Override
	public ClassFile forName(String name) throws ClassNotFoundException
	{
		ClassFile cf = getClass(name);
		if(cf == null)
			throw new ClassNotFoundException(name);
		return cf;
	}

	@Override
	public ClassFile getClass(String name)
	{
		return classes.get(name);
	}

	@Override
	public Collection<ClassFile> getClasses()
	{
		return classes.values();
	}

	@Override
	public boolean removeClass(String name) 
	{
		ClassImpl impl;
		if((impl = (ClassImpl) classes.remove(name)) == null)
			return false;
		resources.remove(impl.resourceName);
		return true;
	}

	@Override
	public org.kucro3.jam2.jar.ClassFile.Modifiable addClass(int version, int access, String name, String signature,
			Class<?> superClass, Class<?>[] interfaces) {
		ClassImpl clz = new ClassImpl(version, access, name, signature, superClass, interfaces);
		resources.put(clz.resourceName, clz);
		classes.put(name, clz);
		return clz;
	}

	@Override
	public Resource addResource(String name)
	{
		if(resources.containsKey(name))
			throw new IllegalArgumentException("resource already existed");
		Resource resource = new ResourceImpl(name);
		resources.put(name, resource);
		return null;
	}
	
	private final EnsuringQueue ensuringQueue;
	
	private final Manifest manifest;
	
	private final Map<String, Resource> resources;
	
	private final Map<String, ClassFile> classes;
	
	interface UnremoveableResource extends Jar.Resource
	{
		interface Modifiable extends Jar.Resource.Modifiable
		{
		}
		
		interface Writeable extends Jar.Resource.Writeable
		{
		}
	}
	
	interface EnsuringHook
	{
		void ensure();
	}
	
	class EnsuringQueue extends ArrayList<EnsuringHook>
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 4109330251862073555L;
		
		public synchronized void clearHook()
		{
			super.clear();
			ensured = true;
		}
		
		public synchronized void addHook(EnsuringHook hook)
		{
			ensured = false;
			super.add(hook);
		}
		
		public synchronized void ensureAll()
		{
			for(EnsuringHook hook : this)
				hook.ensure();
			this.clear();
			ensured = true;
		}
		
		@Override
		public int size()
		{
			if(ensured)
				return -1;
			return super.size();
		}
		
		volatile boolean ensured = false;
	}
	
	class ClassImpl implements ClassFile.Modifiable, Jar.Resource.Modifiable
	{
		ClassImpl(ClassContext cctx)
		{
			assert cctx.getMethods().isEmpty() && cctx.getFields().isEmpty()
				: new IllegalArgumentException("ClassContext must be empty (information template only)");
			
			this.name = cctx.getInternalName().replace('/', '.');
			this.resourceName = cctx.getInternalName() + ".class";
			this.builder = new ClassBuilder(cctx);
			this._hook();
		}
		
		ClassImpl(int version, int access, String name, String signature, Class<?> superClass, Class<?>[] interfaces)
		{
			this.name = name;
			this.resourceName = Jam2Util.fromCanonicalToInternalName(name) + ".class";
			this.builder = new ClassBuilder(version, access, 
					Jam2Util.fromCanonicalToInternalName(superClass.getCanonicalName()),
					signature,
					Jam2Util.toInternalName(superClass),
					Jam2Util.toInternalNames(interfaces));
			this._hook();
		}
		
		ClassImpl(int version, int access, String name, String signature, String superClass, String[] interfaces)
		{
			this.name = name;
			this.resourceName = Jam2Util.fromCanonicalToInternalName(name) + ".class";
			this.builder = new ClassBuilder(version, access, 
					superClass,
					signature,
					superClass,
					interfaces);
			this._hook();
		}
		
		private final void _hook()
		{
			JarPot.this.ensuringQueue.addHook(this.hook = () -> {
				this.subHooks.ensureAll();
				this.builder.appendSource(source, debug);
				this.ensuredBytes = this.builder.buildAsBytes();
			});
		}
		
		@Override
		public boolean containsField(String name)
		{
			return getField(name) != null;
		}

		@Override
		public boolean containsMethod(String name, Class<?> returnType, Class<?>... arguments) 
		{
			return getMethod(name, returnType, arguments) != null;
		}

		@Override
		public String getClassName() 
		{
			return name;
		}

		@Override
		public String getDebug() 
		{
			return debug;
		}

		@Override
		public ClassField getField(String name)
		{
			return fields.get(name);
		}

		@Override
		public Collection<ClassField> getFields() 
		{
			return fields.values();
		}
		
		@Override
		public Class<?> getLoadedClass() 
		{
			if(cached != null)
				return cached;
			if(ensuredBytes != null)
				return cached = Jam2Util.getInstance().defClass(Jam2Util.fromCanonicalToInternalName(name)
						, ensuredBytes, 0, ensuredBytes.length);
			return null;
		}

		@Override
		public ClassMethod getMethod(String name, Class<?> returnType, Class<?>... arguments)
		{
			return getMethod(name, Jam2Util.toDescriptor(null, returnType, arguments));
		}
		
		@Override
		public ClassMethod getMethod(String name, String returnType, String... arguments)
		{
			return getMethod(name, Jam2Util.toDescriptor(null, returnType, arguments));
		}
		
		@Override
		public ClassMethod getMethod(String name, String descriptor)
		{
			return methods.get(name + descriptor);
		}

		@Override
		public Collection<ClassMethod> getMethods()
		{
			return methods.values();
		}

		@Override
		public String getSource()
		{
			return source;
		}
		
		@Override
		public void force()
		{
			hook.ensure();
			ensuringQueue.remove(hook);
		}
		
		@Override
		public Class<?> forceLoad()
		{
			if(cached != null)
				return cached;
			force();
			return cached = Jam2Util.getInstance().defClass(Jam2Util.fromCanonicalToInternalName(name)
					, ensuredBytes, 0, ensuredBytes.length);
		}

		@Override
		public boolean removeMethod(String name, Class<?> returnType, Class<?>... arguments) 
		{
			MethodImpl impl;
			if((impl = (MethodImpl) methods.remove(Jam2Util.toDescriptor(name, returnType, arguments))) != null)
				this.subHooks.remove(impl.hook);
			else
				return false;
			return true;
		}

		@Override
		public boolean removeField(String name)
		{
			FieldImpl impl;
			if((impl = (FieldImpl) fields.remove(name)) != null)
				this.subHooks.remove(impl.hook);
			else
				return false;
			return true;
		}

		@Override
		public ClassMethod.Modifiable addMethod(String name, Class<?> returnType,
				Class<?>... arguments) 
		{
			return addMethod(name, Jam2Util.toDescriptor(null, returnType, arguments));
		}
		
		@Override
		public ClassMethod.Modifiable addMethod(String name, String returnType,
				String... arguments) 
		{
			return addMethod(name, Jam2Util.toDescriptor(null, returnType, arguments));
		}
		
		@Override
		public ClassMethod.Modifiable addMethod(String name, String descriptor)
		{
			String key = name + descriptor;
			if(methods.containsKey(key))
				throw new IllegalArgumentException("Method duplicated: " + key);
			MethodImpl impl = new MethodImpl(this, name, descriptor);
			methods.put(key, impl);
			return impl;
		}

		@Override
		public ClassField.Modifiable addField(String name)
		{
			if(fields.containsKey(name))
				throw new IllegalArgumentException("Field duplicated: " + name);
			FieldImpl impl = new FieldImpl(this, name);
			fields.put(name, impl);
			return impl;
		}

		@Override
		public void setDebug(String debug) 
		{
			this.debug = debug;
		}

		@Override
		public void setSource(String source) 
		{
			this.source = source;
		}
		
		// methods implementing Resource.Modifiable
		@Override
		public InputStream asInputStream() 
		{
			return optionalBIS(ensuredBytes);
		}
		
		@Override
		public String getName() 
		{
			return resourceName;
		}

		@Override
		public void clear() 
		{
			this.ensuredBytes = null;
			this._hook();
		}

		@Override
		public boolean ensured() 
		{
			return ensuredBytes != null;
		}
		//
		
		Class<?> cached;
		
		byte[] ensuredBytes;
		
		private String debug;
		
		private String source;
		
		private final String name;
		
		private final String resourceName;
		
		private final ClassBuilder builder;
		
		private final EnsuringQueue subHooks = new EnsuringQueue();
		
		private final Map<String, ClassMethod> methods = new HashMap<>();
		
		private final Map<String, ClassField> fields = new HashMap<>();
		
		private EnsuringHook hook;
	}
	
	class FieldImpl implements ClassField.Modifiable
	{
		FieldImpl(ClassImpl owner, String name)
		{
			this.owner = owner;
			this.name = name;
			this.owner.subHooks.addHook(this.hook = () -> {
				this.owner.builder.appendField(access, name, descriptor, signature, owner);
			});
		}
		
		@Override
		public int getAccess()
		{
			return access;
		}

		@Override
		public String getDescriptor() 
		{
			return descriptor;
		}

		@Override
		public String getName() 
		{
			return name;
		}

		@Override
		public String getSignature() 
		{
			return signature;
		}

		@Override
		public Object getValue()
		{
			return value;
		}
		
		@Override
		public void setAccess(int access) 
		{
			this.access = access;
		}

		@Override
		public void setDescriptor(String desc)
		{
			this.descriptor = desc;
		}

		@Override
		public void setSignature(String signature) 
		{
			this.signature = signature;
		}

		@Override
		public void setValue(Object value)
		{
			this.value = value;
		}
		
		private final EnsuringHook hook;
		
		int access;
		
		String descriptor;
		
		final String name;
		
		String signature;
		
		Object value;
		
		private final ClassImpl owner;
	}
	
	class MethodImpl implements ClassMethod.Modifiable
	{
		MethodImpl(ClassImpl owner, String name, String descriptor)
		{
			this.owner = owner;
			this.name = name;
			this.descriptor = descriptor;
			this.descAnalyzer = new LazyDescriptorAnalyzer(descriptor);
			this.insns = new InstructionContainer();
			this.owner.subHooks.addHook(this.hook = () -> {
				this.insns.revisit(this.owner.builder.appendMethod(access, name, descriptor, signature, exceptions).getContext());
			});
		}
		
		@Override
		public int getAccess()
		{
			return access;
		}

		@Override
		public String getDescriptor()
		{
			return descriptor;
		}

		@Override
		public String[] getExceptions()
		{
			return exceptions;
		}

		@Deprecated
		@Override
		public Instruction[] getInstructions() 
		{
			return insns.toInstructions();
		}

		@Override
		public InstructionContainer getInstructionContainer() 
		{
			return insns;
		}

		@Override
		public String getName() 
		{
			return name;
		}

		@Override
		public String getSignature() 
		{
			return signature;
		}

		@Override
		public void setAccess(int access) 
		{
			this.access = access;
		}

		@Override
		public void setExceptions(String[] exceptions)
		{
			this.exceptions = exceptions;
		}

		@Override
		public void setSignature(String signature)
		{
			this.signature = signature;
		}
		
		@Override
		public String getReturnType()
		{
			return descAnalyzer.getReturnType();
		}
		
		@Override
		public String[] getArguments()
		{
			return descAnalyzer.getArguments();
		}
		
		private final EnsuringHook hook;
		
		private final LazyDescriptorAnalyzer descAnalyzer;
		
		int access;
		
		final String descriptor;
		
		String[] exceptions;
		
		final InstructionContainer insns;
		
		final String name;
		
		String signature;
		
		private final ClassImpl owner;
	}
	
	class ResourceImpl implements Resource.Modifiable, Resource.Writeable
	{
		ResourceImpl(String name)
		{
			this.name = name;
			this.ros = new ByteArrayReferenceOutputStream();
			this._hook();
		}
		
		private final void _hook()
		{
			JarPot.this.ensuringQueue.addHook(this.hook = () -> {
				ensuredBytes = ros.getReference().toByteArray();
			});
		}
		
		@Override
		public InputStream asInputStream() 
		{
			return optionalBIS(ensuredBytes);
		}

		@Override
		public String getName() 
		{
			return name;
		}

		@Override
		public void clear() 
		{
			ensuredBytes = null;
			ros.setReference(new ByteArrayOutputStream());
		}

		@Override
		public boolean ensured() 
		{
			return ensuredBytes != null;
		}
		
		@Override
		public OutputStream getOutputStream() 
		{
			return ros;
		}
		
		@Override
		public void force()
		{
			hook.ensure();
			ensuringQueue.remove(hook);
		}
		
		private EnsuringHook hook;
		
		byte[] ensuredBytes;
		
		private final String name;
		
		private final ByteArrayReferenceOutputStream ros;
	}
	
	class ByteArrayReferenceOutputStream extends OutputStream
	{
		ByteArrayReferenceOutputStream()
		{
		}
		
		ByteArrayReferenceOutputStream(ByteArrayOutputStream bos)
		{
			this.ref = bos;
		}
		
		@Override
		public void write(int b) throws IOException
		{
			if(ref != null)
				ref.write(b);
		}
		
		@Override
		public void write(byte[] b) throws IOException
		{
			if(ref != null)
				try {
					ref.write(b);
				} catch (IOException e) {
					// unused
				}
		}
		
		@Override
		public void write(byte[] b, int off, int len) throws IOException
		{
			if(ref != null)
				ref.write(b, off, len);
		}
		
		public void reset()
		{
			if(ref != null)
				ref.reset();
		}
		
		@Override
		public void flush() throws IOException
		{
			if(ref != null)
				ref.flush();
		}
		
		@Override
		public void close() throws IOException
		{
			if(ref != null)
				ref.close();
		}
		
		public ByteArrayOutputStream getReference()
		{
			return ref;
		}
		
		public void setReference(ByteArrayOutputStream ref)
		{
			this.ref = ref;
		}
		
		ByteArrayOutputStream ref;
	}
	
	class ClassListener implements ClassVisitorListener
	{
		ClassListener(ClassImpl clz)
		{
			this.clz = clz;
		}
		
		@Override
		public void onVisitMethod(ClassVisitor cv,
				int access, String name, String descriptor, String signature, String[] exceptions,
				Reference<MethodVisitor> ref)
		{
			MethodImpl impl = (MethodImpl) clz.addMethod(name, descriptor);
			
			impl.setAccess(access);
			impl.setSignature(signature);
			impl.setExceptions(exceptions);
		}
		
		@Override
		public void onVisitField(ClassVisitor cv,
				int access, String name, String descriptor, String signature, Object value,
				Reference<FieldVisitor> ref)
		{
			FieldImpl impl = (FieldImpl) clz.addField(name);
			
			impl.setAccess(access);
			impl.setDescriptor(descriptor);
			impl.setSignature(signature);
			impl.setValue(value);
		}
		
		private final ClassImpl clz;
	}
}