package org.kucro3.jam2.invoke;

public abstract class FieldInvoker {
	protected FieldInvoker(Class<?> declaringClass, int modifier, String name, Class<?> type)
	{
		this.declaringClass = declaringClass;
		this.modifier = modifier;
		this.name = name;
		this.type = type;
	}
	
	public Class<?> getDeclaringClass()
	{
		return declaringClass;
	}
	
	public int getModifier()
	{
		return modifier;
	}
	
	public String getName()
	{
		return name;
	}
	
	public Class<?> getType()
	{
		return type;
	}
	
	public abstract void set(Object obj, Object ref);
	
	public abstract Object get(Object obj);
	
	final Class<?> declaringClass;
	
	final int modifier;
	
	final String name;
	
	final Class<?> type;
}
