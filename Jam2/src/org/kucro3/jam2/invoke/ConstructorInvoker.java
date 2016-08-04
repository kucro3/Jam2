package org.kucro3.jam2.invoke;

public abstract class ConstructorInvoker {
	protected ConstructorInvoker(Class<?> declaringClass, int modifier, Class<?>[] arguments)
	{
		this.declaringClass = declaringClass;
		this.modifier = modifier;
		this.arguments = arguments;
		this.descriptor = toDescriptor(arguments);
	}
	
	static String toDescriptor(Class<?>[] arguments)
	{
		return MethodInvoker.toDescriptor("<init>", void.class, arguments);
	}
	
	public String getDescriptor()
	{
		return descriptor;
	}
	
	public int getModifier()
	{
		return modifier;
	}
	
	public Class<?>[] getArguments()
	{
		return arguments;
	}
	
	public abstract Object newInstance(Object... args);
	
	final String descriptor;
	
	final Class<?> declaringClass;
	
	final int modifier;
	
	final Class<?>[] arguments;
}
