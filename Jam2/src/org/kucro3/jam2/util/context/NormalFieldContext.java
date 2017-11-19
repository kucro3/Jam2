package org.kucro3.jam2.util.context;

public abstract class NormalFieldContext extends AbstractFieldContext {
	public NormalFieldContext(String declaringClass)
	{
		this.declaringClass = declaringClass;
	}
	
	@Override
	public String getDeclaringType()
	{
		return declaringClass;
	}
	
	protected final String declaringClass;
}
