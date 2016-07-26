package org.kucro3.jam2.invoke;

public abstract class MethodInvoker {
	protected MethodInvoker(Class<?> declaringClass, int modifier, String name, Class<?> returnType, Class<?>[] arguments)
	{
		this.declaringClass = declaringClass;
		this.name = name;
		this.returnType = returnType;
		this.arguments = arguments;
		this.modifier = modifier;
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
	
	final int modifier;
	
	final Class<?> declaringClass;
	
	final String name;
	
	final Class<?> returnType;
	
	final Class<?>[] arguments;
}
