package org.kucro3.jam2.jar;

import java.io.*;
import java.util.*;
import java.util.zip.*;

import org.kucro3.jam2.jar.JarClassLoader.ByteArrayCallback;
import org.objectweb.asm.ClassReader;

public class JarFile {
	public JarFile(File file) throws IOException
	{
		this(file, false);
	}
	
	public JarFile(InputStream is) throws IOException
	{
		this(is, false);
	}
	
	public JarFile(File file, boolean cached) throws IOException
	{
		this(new FileInputStream(file), cached);
	}
	
	public JarFile(InputStream is, boolean cached) throws IOException
	{
		if(is == null)
			throw new NullPointerException("null in InputStream");
		this.zis = new ZipInputStream(is);
		this.loader = new JarClassLoader(this.getClass().getClassLoader());
		this.cached = cached;
		this.loadAll();
	}
	
	private void loadAll() throws IOException
	{
		ClassFile cf;
		while((cf = nextClass()) != null)
			classes.put(cf.getLoadedClass().getCanonicalName(), cf);
	}
	
	private ClassFile nextClass() throws IOException
	{
		ClassReader cr;
		ByteArrayCallback callback = null;
		
		if(isCached())
			callback = (byts) -> {this.tempCr = new ClassReader(byts);};
		cr = this.tempCr;
		this.tempCr = null;
		
		ZipEntry nextEntry;
		if((nextEntry = zis.getNextEntry()) == null)
			return null;
		
		Class<?> next;
		while((next = loader.tryLoadClass(zis, nextEntry, callback)) == null);
		if(next == JarClassLoader.UNAVAILABLE)
			return null;
		
		return new ClassFile(this, next, cr, cached);
	}
	
	public Collection<ClassFile> getClasses()
	{
		return Collections.unmodifiableCollection(classes.values());
	}
	
	public ClassFile forName(String name) throws ClassNotFoundException
	{
		ClassFile cf;
		if((cf = getClass(name)) == null)
			throw new ClassNotFoundException(name);
		return cf;
	}
	
	public ClassFile getClass(String name)
	{
		return classes.get(name);
	}
	
	public boolean containsClass(String name)
	{
		return classes.containsKey(name);
	}
	
	public ClassLoader getClassLoader()
	{
		return loader;
	}
	
	public boolean isCached()
	{
		return cached;
	}
	
	ClassReader tempCr;
	
	final JarClassLoader loader;
	
	private final Map<String, ClassFile> classes = new HashMap<>();
	
	private final boolean cached;
	
	private final ZipInputStream zis;
}