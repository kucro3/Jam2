package org.kucro3.jam2.util.context;

import org.kucro3.jam2.util.MethodContext;

public class RestrictedModifiableMethodContext extends NormalMethodContext implements MethodContext.RestrictedModifiable {
	
	public RestrictedModifiableMethodContext(String declaringClass, String name, String descriptor)
	{
		super(declaringClass);
		super.name = name;
		super.descriptor = descriptor;
	}
	
	public RestrictedModifiableMethodContext(String declaringClass,
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
		super.exceptions = exceptions;
	}

	@Override
	public void setModifier(int modifier) 
	{
		super.modifier = modifier;
	}

	@Override
	public void setSignature(String signature) 
	{
		super.signature = signature;
	}	
}
