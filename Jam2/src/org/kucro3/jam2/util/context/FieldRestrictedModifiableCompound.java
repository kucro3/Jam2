package org.kucro3.jam2.util.context;

import org.kucro3.jam2.util.FieldContext;

public class FieldRestrictedModifiableCompound extends FieldCompound 
		implements FieldContext.RestrictedModifiable {
	public FieldRestrictedModifiableCompound(FieldContext.RestrictedModifiable ctx) 
	{
		super(ctx);
	}
	
	public FieldRestrictedModifiableCompound(FieldContext.FullyModifiable ctx) 
	{
		super(ctx);
	}
	
	@Override
	public void setValue(Object value) 
	{
		ref.setValue(value);
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
	public void setDescriptor(String descriptor)
	{
		ref.setDescriptor(descriptor);
	}
}
