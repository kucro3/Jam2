package org.kucro3.jam2.util.context;

import org.kucro3.jam2.util.MethodContext;

public class MethodRestrictedModifiableCompound extends MethodCompound
		implements MethodContext.RestrictedModifiable {
	public MethodRestrictedModifiableCompound(MethodContext.RestrictedModifiable ctx) 
	{
		super(ctx);
	}
	
	public MethodRestrictedModifiableCompound(MethodContext.FullyModifiable ctx) 
	{
		super(ctx);
	}

	@Override
	public void setExceptions(String[] exceptions) 
	{
		ref.setExceptions(exceptions);
	}
	
	@Override
	public void setSignature(String signature)
	{
		ref.setSignature(signature);
	}
	
	@Override
	public void setModifier(int modifier)
	{
		ref.setModifier(modifier);
	}
}
