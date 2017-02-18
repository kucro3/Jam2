package org.kucro3.jam2.jar;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.kucro3.jam2.util.Jam2Util;
import org.objectweb.asm.ClassReader;

public class JarFile extends Jar {
	public static void main(String[] args) throws IOException
	{
		File file = new File("G:\\GitReleases\\Jam2\\v1.01-preview-#17016204\\Jam2-1-17012604.jar");
		JarFile jf = new JarFile(file);
		JarPacker.packTo(jf, "G:\\GitReleases\\Jam2\\v1.01-preview-#17016204\\Jam2-1-17012604.jar.test");
	}
	
	public JarFile(File file) throws IOException
	{
		this(file, false);
	}
	
	public JarFile(File file, boolean allToResource) throws IOException
	{
		this(file, allToResource, false);
	}
	
	public JarFile(File file, boolean allToResource, boolean preLoadAll) throws IOException
	{
		this.file = file;
		this.zipFile = new ZipFile(file);
		this.preLoad(allToResource, preLoadAll);
	}
	
	void preLoad(boolean allToResource, boolean preLoadAll) throws IOException
	{
		Enumeration<? extends ZipEntry> e = zipFile.entries();
		while(e.hasMoreElements())
		{
			ZipEntry entry = e.nextElement();
			String name = entry.getName();
			if(name.equals(Jar.MANIFEST_NAME))
				super.manifest.read(zipFile.getInputStream(entry));
			else if(!allToResource && Jam2Util.isClassResource(name))
			{
				name = Jam2Util.fromResourceToInternalName(name);
				InputStream is;
				ClassFile cf = super.addClass(name);
				ClassReader cr = new ClassReader(is = zipFile.getInputStream(entry));
				cr.accept(cf, 0);
				is.close();
				super.classes.put(name, cf);
			}
			else
				super.resources.put(name, new LoadedResource(name, entry));
		}
	}
	
	public File getFile()
	{
		return file;
	}
	
	protected class LoadedResource extends Resource
	{
		public LoadedResource(String resourceName, ZipEntry entry)
		{
			super(resourceName);
			this.entry = entry;
		}
		
		@Override
		public ResourceInputStream getInputStream()
		{
			ensureInitialized(true);
			return super.getInputStream();
		}
		
		@Override
		public ResourceOutputStream getOutputStream()
		{
			ensureInitialized(true);
			return super.getOutputStream();
		}
		
		@Override
		public ClassFile transfer()
		{
			ensureInitialized(true);
			return super.transfer();
		}
		
		@Override
		public ResourceState flipState()
		{
			ensureInitialized(true);
			return super.flipState();
		}
		
		void ensureInitialized(boolean protectState)
		{
			if(initialized)
				return;
			
			ResourceState old = super.getState();
			
			super.ensureStateWiriting();
			try {
				InputStream is = JarFile.this.zipFile.getInputStream(entry);
				int b;
				while((b = is.read()) != -1)
					super.ros.write(b);
				is.close();
			} catch (IOException e) {
				throw new JarIOException(e);
			}
			
			initialized = true;
			
			if(protectState)
				super.setState(old);
		}
		
		volatile boolean initialized;
		
		protected final ZipEntry entry;
	}
	
	protected final File file;
	
	protected final ZipFile zipFile;
}
