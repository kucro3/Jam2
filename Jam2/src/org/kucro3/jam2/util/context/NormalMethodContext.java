package org.kucro3.jam2.util.context;

public abstract class NormalMethodContext extends AbstractMethodContext {
	public NormalMethodContext(String declaringClass)
	{
		this.declaringClass = declaringClass;
	}
	
	@Override
	public String getDeclaringClass() 
	{
		return declaringClass;
	}
	
	protected final String declaringClass;
}