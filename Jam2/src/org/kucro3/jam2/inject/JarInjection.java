package org.kucro3.jam2.inject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.kucro3.jam2.jar.ClassFile;
import org.kucro3.jam2.jar.JarFile;

public class JarInjection {
	public static JarInjection getInjection(File jarfile) throws IOException, UnsatisfiedInjectionException
	{
		return new JarInjection(new JarFile(jarfile, false, true));
	}
	
	public static JarInjection getInjection(JarFile jarfile) throws UnsatisfiedInjectionException
	{
		if(!jarfile.isLoaded())
			throw new IllegalArgumentException("Jar object must be in \"loaded\" mode");
		return new JarInjection(jarfile);
	}
	
	JarInjection(JarFile file) throws UnsatisfiedInjectionException
	{
		this.jar = file;
		this.injections = new HashMap<>();
		this.init();
	}
	
	public JarFile getJar()
	{
		return jar;
	}
	
	public boolean containsInjection(String className)
	{
		return injections.containsKey(className);
	}
	
	public boolean containsInjection(Class<?> clazz)
	{
		return containsInjection(clazz.getCanonicalName());
	}
	
	public Injection getInjection(String className)
	{
		return injections.get(className);
	}
	
	public Injection getInjection(Class<?> clazz)
	{
		return getInjection(clazz.getCanonicalName());
	}
	
	private final void init() throws UnsatisfiedInjectionException
	{
		for(ClassFile cf : jar.getClasses())
			injections.put(cf.getClassName(), Injector.getInjection(cf.getLoadedClass()));
	}
	
	private final Map<String, Injection> injections;
	
	private final JarFile jar;
}
