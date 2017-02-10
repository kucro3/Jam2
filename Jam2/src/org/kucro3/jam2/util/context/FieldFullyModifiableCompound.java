package org.kucro3.jam2.util.context;

import org.kucro3.jam2.util.FieldContext;

public class FieldFullyModifiableCompound extends FieldCompound 
		implements FieldContext.FullyModifiable {
	public FieldFullyModifiableCompound(FieldContext.FullyModifiable ctx) 
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
