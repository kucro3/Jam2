package org.kucro3.jam2.util.context;

import org.kucro3.jam2.util.MethodContext;

public class FullyModifiableMethodContext extends NormalMethodContext implements MethodContext.FullyModifiable {
	public FullyModifiableMethodContext(String declaringClass)
	{
		super(declaringClass);
	}
	
	public FullyModifiableMethodContext(String declaringClass,
			int modifier, String name, String descriptor, String signature, String[] exceptions)
	{
		super(declaringClass);
		super.modifier = modifier;
		super.name = name;
		super.descriptor = descriptor;
		super.signature = signature;
		super.exceptions = exceptions;
	}

	@Override
	public void setExceptions(String[] exceptions) 
	{
		this.exceptions = exceptions;
	}

	@Override
	public void setModifier(int modifier) 
	{
		this.modifier = modifier;
	}

	@Override
	public void setSignature(String signature) 
	{
		this.signature = signature;
	}

	@Override
	public void setName(String name) 
	{
		this.name = name;
	}

	@Override
	public void setDescriptor(String descriptor) 
	{
		this.descriptor = descriptor;
	}
}