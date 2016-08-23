package org.kucro3.jam2.jar;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.kucro3.jam2.util.Jam2Util;
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
	
	public boolean isInstructionCached()
	{
		return cached;
	}
	
	public String getSource()
	{
		return source;
	}
	
	public String getDebug()
	{
		return debug;
	}
	
	public Collection<ClassField> getFields()
	{
		return fields.values();
	}
	
	public Collection<ClassMethod> getMethods()
	{
		return methods.values();
	}
	
	public boolean containsField(String name)
	{
		return fields.containsKey(name);
	}
	
	public ClassField getField(String name)
	{
		return fields.get(name);
	}
	
	public boolean containsMethod(String name)
	{
		return methods.containsKey(name);
	}
	
	public ClassMethod getMethod(String name, Class<?> returnType, Class<?>... arguments)
	{
		return methods.get(Jam2Util.toDescriptor(name, returnType, arguments));
	}
	
	private final JarFile owner;
	
	private final Class<?> loadedClass;
	
	private final ClassReader cr;
	
	private final boolean cached;
	
	final ClassFileVisitor cfv;
	
	// initialized by CFV
	
	String source;
	
	String debug;
	
	final Map<String, ClassField> fields = new HashMap<>();
	
	final Map<String, ClassMethod> methods = new HashMap<>();
}
