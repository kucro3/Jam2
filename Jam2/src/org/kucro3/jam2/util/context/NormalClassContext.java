package org.kucro3.jam2.util.context;

public abstract class NormalClassContext extends AbstractClassContext {
	public NormalClassContext(String name)
	{
		this.name = name;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	protected final String name;
}
