package org.kucro3.jam2.util;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;

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
	
	public MethodContext addMethod(String name, Class<?> returnType, Class<?>... arguments)
	{
		return addMethod
	}
	
	public MethodContext addMethod(String name, String returnType, String[] arguments, String[] throwings)
	{
		return addMethod(MethodContext.newContext(internalName, name, returnType, arguments, throwings));
	}
	
	public MethodContext addMethod(MethodContext ctx)
	{
		mappedMethods.put(toMethodKey(ctx), ctx);
		return ctx;
	}
	
	public MethodContext addMethodCopy(MethodContext ctx)
	{
		return addMethod(ctx.copyAsMethod());
	}
	
	public MethodContext getMethod(String name, Class<?> returnType, Class<?>... arguments)
	{
		return getMethod(name, Type.getDescriptor(returnType), _toDescriptors(arguments));
	}
	
	public MethodContext getMethod(String name, String returnType, String... arguments)
	{
		return getMethod(toMethodKey(name, returnType, arguments));
	}
	
	public ConstructorContext getConstructor(Class<?>... arguments)
	{
		return getConstructor(_toDescriptors(arguments));
	}
	
	public ConstructorContext getConstructor(String... arguments)
	{
		return getConstructor(toConstructorKey(arguments));
	}
	
	MethodContext getMethod(String signature)
	{
		return mappedMethods.get(signature);
	}
	
	public FieldContext getField(String name)
	{
		return mappedFields.get(toFieldKey(name));
	}
	
	ConstructorContext getConstructor(String signature)
	{
		return mappedConstructors.get(signature);
	}
	
	static void checkDuplication(Map<String, ?> map, String key, String msg)
	{
		if(map.containsKey(key))
			throw new IllegalArgumentException(String.format(msg, key));
	}
	
	final void checkDuplicationForMethod(String key)
	{
		checkDuplication(mappedMethods, key, "Method duplicated: %s");
	}
	
	final void checkDuplicationForField(String key)
	{
		checkDuplication(mappedFields, key, "Field duplicated: %s");
	}
	
	final void checkDuplicationForConstructor(String key)
	{
		checkDuplication(mappedConstructors, key, "Constructor duplicated: %s");
	}
	
	static String toConstructorKey(ConstructorContext ctx)
	{
		return toConstructorKey(ctx.getArgumentDescriptors());
	}
	
	static String toConstructorKey(String[] arguments)
	{
		return _toConstructorDescriptor(arguments);
	}
	
	static String toMethodKey(MethodContext ctx)
	{
		return toMethodKey(ctx.getMethodName(), ctx.getReturnTypeDescriptor(), ctx.getArgumentDescriptors());
	}
	
	static String toMethodKey(String name, String returnType, String[] arguments)
	{
		return name + _toDescriptor(returnType, arguments);
	}
	
	static String toFieldKey(FieldContext ctx)
	{
		return toFieldKey(ctx.getFieldName());
	}
	
	static String toFieldKey(String name)
	{
		return name;
	}
	
	private final String superClass;
	
	private final String[] interfaces;
	
	private final int version;
	
	private final String internalName;
	
	private final int modifiers;
	
	private final Map<String, MethodContext> mappedMethods = new HashMap<>();
	
	private final Map<String, FieldContext> mappedFields = new HashMap<>();
	
	private final Map<String, ConstructorContext> mappedConstructors = new HashMap<>();
	
	private MethodContext staticBlock;
}