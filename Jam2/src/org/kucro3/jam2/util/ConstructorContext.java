package org.kucro3.jam2.util;

import java.lang.reflect.Constructor;

import org.objectweb.asm.Type;

import static org.kucro3.jam2.util.Jam2Util.*;

public class ConstructorContext extends MethodContextImpl
{
	public static ConstructorContext newContext(Constructor<?> constructor)
	{
		return new ConstructorContext(constructor.getDeclaringClass(), constructor.getModifiers(), constructor.getParameterTypes(), 
				constructor.getExceptionTypes());
	}
	
	public static ConstructorContext newContext(Class<?> declaringClass, int modifier, Class<?>[] arguments,
			Class<?>... throwings)
	{
		return new ConstructorContext(declaringClass, modifier, arguments, throwings);
	}
	
	public static ConstructorContext newContext(String declaringClass, int modifier, Class<?>[] arguments,
			Class<?>... throwings)
	{
		return new ConstructorContext(declaringClass, modifier, arguments, throwings);
	}
	
	public static ConstructorContext newContext(String declaringClass, int modifier, String[] arguments,
			String... throwings)
	{
		return new ConstructorContext(declaringClass, modifier, arguments, throwings);
	}
	
	ConstructorContext(Class<?> declaringClass, int modifier, Class<?>[] arguments, Class<?>[] throwings)
	{
		this(declaringClass, Type.getInternalName(declaringClass), modifier,
				arguments, _toDescriptors(arguments),
				throwings, _toInternalNames(throwings));
	}
	
	ConstructorContext(String declaringClass, int modifier, Class<?>[] arguments, Class<?>[] throwings)
	{
		this(null, declaringClass, modifier,
				arguments, _toDescriptors(arguments),
				throwings, _toInternalNames(throwings));
	}
	
	ConstructorContext(String delcaringClassInternalName, int modifier, String[] argumentDescriptors, String[] throwings)
	{
		this(null, delcaringClassInternalName, modifier, null, argumentDescriptors, null, throwings);
	}
	
	protected ConstructorContext(Class<?> declaringClass, String declaringClassInternalName, int modifier,
			Class<?>[] arguments, String[] argumentDescriptors, Class<?>[] exceptionTypes, String[] exceptions)
	{
		super(declaringClass, declaringClassInternalName, modifier, "<init>",
				void.class, "V",
				arguments, argumentDescriptors,
				exceptionTypes, exceptions);
	}
	
	public final ConstructorContext copyAsConstructor()
	{
		return new ConstructorContext(getDeclaringClass(), getDeclaringClassInternalName(), getModifier(),
				getArguments(), getArgumentDescriptors(), getExceptionTypes(), getExceptions());
	}
}