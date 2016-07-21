package org.kucro3.jam2.util;

import java.lang.reflect.Constructor;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import static org.kucro3.jam2.util.Jam2Util.*;

public class ConstructorContext extends AccessableContext
{
	public static ConstructorContext newContext(Constructor<?> constructor)
	{
		return new ConstructorContext(constructor.getDeclaringClass(), constructor.getParameterTypes(), 
				constructor.getExceptionTypes());
	}
	
	public static ConstructorContext newContext(Class<?> declaringClass, Class<?>[] arguments,
			Class<?>... throwings)
	{
		return new ConstructorContext(declaringClass, arguments, throwings);
	}
	
	public static ConstructorContext newContext(String declaringClass, String[] arguments,
			String... throwings)
	{
		return new ConstructorContext(declaringClass, arguments, throwings);
	}
	
	ConstructorContext(Class<?> declaringClass, Class<?>[] arguments, Class<?>[] throwings)
	{
		this(declaringClass, Type.getInternalName(declaringClass),
				arguments, _toDescriptors(arguments),
				throwings, _toDescriptors(throwings));
	}
	
	ConstructorContext(String delcaringClassInternalName, String[] argumentDescriptors, String[] throwings)
	{
		this(null, delcaringClassInternalName, null, argumentDescriptors, null, throwings);
	}
	
	protected ConstructorContext(Class<?> declaringClass, String declaringClassInternalName,
			Class<?>[] arguments, String[] argumentDescriptors, Class<?>[] exceptionTypes, String[] exceptions)
	{
		this.declaringClass = declaringClass;
		this.classInternalName = declaringClassInternalName;
		this.arguments = arguments;
		this.argumentDescriptors = argumentDescriptors;
		this.exceptions = exceptions;
		this.exceptionTypes = exceptionTypes;
	}
	
	public final ConstructorContext copyAsConstructor()
	{
		return new ConstructorContext(getDeclaringClass(), getDeclaringClassInternalName(),
				getArguments(), getArgumentDescriptors(), getExceptionTypes(), getExceptions());
	}
	
	public Class<?> getDeclaringClass()
	{
		return declaringClass;
	}
	
	public String getDeclaringClassInternalName()
	{
		return classInternalName;
	}
	
	public Class<?>[] getArguments()
	{
		return arguments;
	}
	
	public String[] getArgumentDescriptors()
	{
		return argumentDescriptors;
	}
	
	public String[] getExceptions()
	{
		return exceptions;
	}
	
	public Class<?>[] getExceptionTypes()
	{
		return exceptionTypes;
	}
	
	public MethodVisitor getMethodVisitor()
	{
		return mv;
	}
	
	MethodVisitor mv;
	
	private final String[] exceptions;
	
	private final Class<?>[] exceptionTypes;
	
	private final String classInternalName;
	
	private final Class<?> declaringClass;
	
	private final String[] argumentDescriptors;
	
	private final Class<?>[] arguments;
}