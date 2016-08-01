package org.kucro3.jam2.invoke;

import org.objectweb.asm.Type;

public abstract class MethodInvoker {
	protected MethodInvoker(Class<?> declaringClass, int modifier, String name, Class<?> returnType, Class<?>[] arguments)
	{
		this.declaringClass = declaringClass;
		this.name = name;
		this.returnType = returnType;
		this.arguments = arguments;
		this.modifier = modifier;
		this.descriptor = toDescriptor(name, returnType, arguments);
	}
	
	static String toDescriptor(String name, Class<?> returnType, Class<?>[] arguments)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		sb.append("(");
		for(int i = 0; i < arguments.length; i++)
			sb.append(Type.getDescriptor(arguments[i]));
		sb.append(")");
		sb.append(Type.getDescriptor(returnType));
		return sb.toString();
	}
	
	public final Class<?> getDeclaringClass()
	{
		return declaringClass;
	}
	
	public final String getName()
	{
		return name;
	}
	
	public Class<?> getReturnType()
	{
		return returnType;
	}
	
	public Class<?>[] getArguments()
	{
		return arguments;
	}
	
	public int getModifier()
	{
		return modifier;
	}
	
	public abstract Object invoke(Object obj, Object... args);
	
	public String getDescriptor()
	{
		return descriptor;
	}
	
	final String descriptor;
	
	final int modifier;
	
	final Class<?> declaringClass;
	
	final String name;
	
	final Class<?> returnType;
	
	final Class<?>[] arguments;
}
