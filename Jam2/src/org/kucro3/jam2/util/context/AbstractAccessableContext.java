package org.kucro3.jam2.util.context;

import org.kucro3.jam2.util.AccessableContext;

public abstract class AbstractAccessableContext implements AccessableContext {
	@Override
	public int getModifier() 
	{
		return modifier;
	}

	@Override
	public String getName() 
	{
		return name;
	}

	@Override
	public String getDescriptor() 
	{
		return descriptor;
	}

	@Override
	public String getSignature() 
	{
		return signature;
	}
	
	protected int modifier;
	
	protected String name;
	
	protected String descriptor;
	
	protected String signature;
}
