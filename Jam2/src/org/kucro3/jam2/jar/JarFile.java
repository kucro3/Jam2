package org.kucro3.jam2.jar;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.jar.Manifest;
import java.util.zip.*;

import org.kucro3.jam2.jar.JarClassLoader.ByteArrayCallback;
import org.objectweb.asm.ClassReader;

public class JarFile implements Jar {
	public JarFile(File file) throws IOException
	{
		this(file, false, true);
	}
	
	public JarFile(File file, boolean cached, boolean loadClass) throws IOException
	{
		if(file == null)
			throw new NullPointerException("null in File");
		this.zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(file)));
		this.loader = new JarClassLoader(file, this.getClass().getClassLoader());
		this.resources = new ArrayList<>();
		this.notClass = new ArrayList<>();
		this.cached = cached;
		this.classes = new HashMap<>();
		this.manifest = new Manifest();
		this.manifest.clear();
		this.loadClass = loadClass;
		this.loadAll();
	}
	
	@Override
	protected void finalize()
	{
		try {
			this.loader.close();
		} catch (IOException e) {
			// ignored
		}
	}
	
	private void loadAll() throws IOException
	{
		List<String> entries = new ArrayList<>();
		ZipEntry zEntry;
		
		String temp;
		while((zEntry = zis.getNextEntry()) != null)
		{
			resources.add(zEntry.getName());
			if(isClass(temp = zEntry.getName()))
				entries.add(temp);
			else
				notClass.add(zEntry.getName());
		}
		
		if(entries.isEmpty())
			return;
		
		ListIterator<String> iter = entries.listIterator();
		
		ClassFile cf;
		while((cf = nextClass(iter)) != null)
			classes.put(cf.getClassName(), cf);
		
		InputStream mis = loader.getResourceAsStream("META-INF/MANIFEST.MF");
		if(mis != null)
			this.manifest.read(mis);
	}
	
	private ClassFile nextClass(ListIterator<String> iter) throws IOException
	{
		if(!iter.hasNext())
			return null;
		
		ClassReader cr;
		ByteArrayCallback callback = null;
		
		if(isCached())
			callback = (byts) -> {this.tempCr = new ClassReader(byts);};
		
		String location = iter.next();
		Class<?> next = this.loader.nextClass(location, callback, loadClass);
		if(next == null)
			return null;
		else if(next == JarClassLoader.CLASS_NOT_REQUIRED)
			next = null;
		
		cr = this.tempCr;
		this.tempCr = null;
		
		return new ClassFile(this, next, cr, toClassName(location), location, cached);
	}
	
	@Override
	public Collection<ClassFile> getClasses()
	{
		return Collections.unmodifiableCollection(classes.values());
	}
	
	@Override
	public ClassFile forName(String name) throws ClassNotFoundException
	{
		ClassFile cf;
		if((cf = getClass(name)) == null)
			throw new ClassNotFoundException(name);
		return cf;
	}
	
	@Override
	public ClassFile getClass(String name)
	{
		return classes.get(name);
	}
	
	public boolean containsClass(String name)
	{
		return classes.containsKey(name);
	}
	
	@Override
	public ClassLoader getClassLoader()
	{
		return loader;
	}
	
	public boolean isCached()
	{
		return cached;
	}
	
	@Override
	public URL getResource(String name)
	{
		return loader.getResource(name);
	}
	
	@Override
	public InputStream getResourceAsStream(String name)
	{
		return loader.getResourceAsStream(name);
	}
	
	@Override
	public Collection<String> getResources()
	{
		return Collections.unmodifiableList(resources);
	}
	
	@Override
	public Manifest getManifest()
	{
		return manifest;
	}
	
	@Override
	public boolean removeResource(String name)
	{
		boolean r = resources.remove(name);
		if(r)
			if(isClass(name))
				removeClass(toClassName(name));
		return r;
	}
	
	@Override
	public boolean removeClass(String name)
	{
		return classes.remove(name) != null;
	}
	
	@Override
	public Collection<String> getResourcesExpectClasses()
	{
		return Collections.unmodifiableList(notClass);
	}
	
	static boolean isClass(String res)
	{
		return res.endsWith(".class");
	}
	
	static String toClassName(String res)
	{
		return res.replace(".class", "").replace('/', '.');
	}
	
	final Manifest manifest;
	
	ClassReader tempCr;
	
	final boolean loadClass;
	
	final JarClassLoader loader;
	
	final List<String> notClass;
	
	final List<String> resources;
	
	private final Map<String, ClassFile> classes;
	
	private final boolean cached;
	
	private final ZipInputStream zis;
}