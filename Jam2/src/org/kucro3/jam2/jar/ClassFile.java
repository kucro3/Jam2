package org.kucro3.jam2.jar;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassReader;

public class ClassFile {
	ClassFile(JarFile owner, Class<?> loadedClass, ClassReader reader, boolean cached)
	{
		this.owner = owner;
		this.loadedClass = loadedClass;
		this.cr = reader;
		this.cached = cached;
		if(cached)
			this.cfv = new ClassFileVisitor(this);
		else
			this.cfv = null;
		
		if(this.cr != null)
			this.cr.accept(this.cfv, 0);
	}
	
	public JarFile getOwner()
	{
		return owner;
	}
	
	public Class<?> getLoadedClass()
	{
		return loadedClass;
	}
	
	public boolean isCached()
	{
		return cached;
	}
	
	private final JarFile owner;
	
	private final Class<?> loadedClass;
	
	private final ClassReader cr;
	
	private final boolean cached;
	
	private final ClassFileVisitor cfv;
	
	// initialized by CFV
	
	String source;
	
	String debug;
	
	final Map<String, ClassField> fields = new HashMap<>();
	
	final Map<String, ClassMethod> methods = new HashMap<>();
}
