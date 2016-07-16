package org.kucro3.jam2.util;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassWriter;

import static org.kucro3.jam2.util.Jam2Util.*;

public class ClassContext extends ClassWriter
{
	public ClassContext()
	{
		this(ClassWriter.COMPUTE_FRAMES);
	}
	
	public ClassContext(int flags)
	{
		super(flags);
	}
	
	public MethodContext getMethod(String name, Class<?> returnType, Class<?>... arguments)
	{
		return getMethod(name + _toDescriptor(returnType, arguments));
	}
	
	public MethodContext getMethod(String name, String returnType, String... arguments)
	{
		return getMethod(name + _toDescriptor(returnType, arguments));
	}
	
	public ConstructorContext getConstructor(Class<?>... arguments)
	{
		return getConstructor(_toConstructorDescriptor(arguments));
	}
	
	public ConstructorContext getConstructor(String... arguments)
	{
		return getConstructor(_toConstructorDescriptor(arguments));
	}
	
	MethodContext getMethod(String signature)
	{
		return mappedMethods.get(signature);
	}
	
	public FieldContext getField(String name)
	{
		return mappedFields.get(name);
	}
	
	ConstructorContext getConstructor(String signature)
	{
		return mappedConstructors.get(signature);
	}
	
	
	
	private final Map<String, MethodContext> mappedMethods = new HashMap<>();
	
	private final Map<String, FieldContext> mappedFields = new HashMap<>();
	
	private final Map<String, ConstructorContext> mappedConstructors = new HashMap<>();
	
	private MethodContext staticBlock;
}