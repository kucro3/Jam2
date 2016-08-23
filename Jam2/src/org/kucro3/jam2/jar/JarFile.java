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
	
	public JarFile(File file, boolean cached) throws IOException
	{
		if(file == null)
			throw new NullPointerException("null in File");
		this.zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(file)));
		this.loader = new JarClassLoader(file, this.getClass().getClassLoader());
		this.cached = cached;
		this.classes = new HashMap<>();
		this.loadAll();
		this.loader.close();
	}
	
	private void loadAll() throws IOException
	{
		List<String> entries = new ArrayList<>();
		ZipEntry zEntry;
		
		String temp;
		while((zEntry = zis.getNextEntry()) != null)
			if((temp = zEntry.getName()).endsWith(".class"))
				entries.add(temp);
			else
				continue;
		
		if(entries.isEmpty())
			return;
		
		ListIterator<String> iter = entries.listIterator();
		
		ClassFile cf;
		while((cf = nextClass(iter)) != null)
			classes.put(cf.getLoadedClass().getCanonicalName(), cf);
	}
	
	private ClassFile nextClass(ListIterator<String> iter) throws IOException
	{
		ClassReader cr;
		ByteArrayCallback callback = null;
		
		if(isCached())
			callback = (byts) -> {this.tempCr = new ClassReader(byts);};
		
		Class<?> next = this.loader.nextClass(iter, callback);
		if(next == null)
			return null;
		
		cr = this.tempCr;
		this.tempCr = null;
		
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
	
	private final Map<String, ClassFile> classes;
	
	private final boolean cached;
	
	private final ZipInputStream zis;
}