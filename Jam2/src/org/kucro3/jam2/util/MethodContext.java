package org.kucro3.jam2.util;

import java.lang.reflect.Method;

import org.objectweb.asm.Type;

import static org.kucro3.jam2.util.Jam2Util.*;

public class MethodContext extends ConstructorContext
{
	public static MethodContext newContext(Method method)
	{
		return newContext(method.getDeclaringClass(), method.getName(), method.getReturnType(), method.getParameterTypes());
	}
	
	public static MethodContext newContext(Class<?> declaringClass, String methodName, Class<?> returnType, Class<?>[] arguments,
			Class<?>... throwings)
	{
		return new MethodContext(declaringClass, methodName, returnType, arguments, throwings);
	}
	
	public static MethodContext newContext(String declaringClass, String methodName, String returnType, String[] arguments,
			String[] throwings)
	{
		return new MethodContext(declaringClass, methodName, returnType, arguments, throwings);
	}
	
	MethodContext(Class<?> declaringClass, String methodName, Class<?> returnType, Class<?>[] arguments, Class<?>[] throwings)
	{
		this(declaringClass, Type.getInternalName(declaringClass), methodName,
				returnType, Type.getInternalName(returnType),
				arguments, _toDescriptors(arguments),
				throwings, _toDescriptors(throwings));
	}
	
	MethodContext(String declaringClass, String methodName, String returnType, String[] arguments, String[] throwings)
	{
		this(null, declaringClass, methodName,
				null, returnType,
				null, arguments, 
				null, throwings);
	}
	
	protected MethodContext(Class<?> declaringClass, String declaringClassInternalName, String methodName,
			Class<?> returnType, String returnTypeDescriptor,
			Class<?>[] arguments, String[] argumentDescriptors,
			Class<?>[] exceptionTypes, String[] throwings)
	{
		super(declaringClass, declaringClassInternalName, arguments, argumentDescriptors, exceptionTypes, throwings);
		this.returnType = returnType;
		this.returnTypeDescriptor = returnTypeDescriptor;
		this.methodName = methodName;
	}
	
	public Class<?> getReturnType()
	{
		return returnType;
	}
	
	public String getReturnTypeDescriptor()
	{
		return returnTypeDescriptor;
	}
	
	public String getMethodName()
	{
		return methodName;
	}
	
	private final String methodName;
	
	private final Class<?> returnType;
	
	private final String returnTypeDescriptor;
}
