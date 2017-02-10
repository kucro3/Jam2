package org.kucro3.jam2.util.context;

import org.kucro3.jam2.util.MethodContext;

public class MethodFullyModifiableCompound extends MethodCompound
		implements MethodContext.FullyModifiable {
	public MethodFullyModifiableCompound(MethodContext.FullyModifiable ctx)
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
	
	@Override
	public void setName(String name)
	{
		ref.setName(name);
	}
	
	@Override
	public void setDescriptor(String descriptor)
	{
		ref.setDescriptor(descriptor);
	}
}
