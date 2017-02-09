package org.kucro3.jam2.util.context;

import org.kucro3.jam2.util.FieldContext;

public class FullyModifiableFieldContext extends NormalFieldContext implements FieldContext.FullyModifiable {
	public FullyModifiableFieldContext(String declaringClass) 
	{
		super(declaringClass);
	}
	
	public FullyModifiableFieldContext(String declaringClass, 
			int modifier, String name, String descriptor, String signature, Object value)
	{
		super(declaringClass);
		super.modifier = modifier;
		super.name = name;
		super.descriptor = descriptor;
		super.signature = signature;
		super.value = value;
	}

	@Override
	public void setValue(Object value) 
	{
		super.name = name;
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

	@Override
	public void setName(String name) 
	{
		super.name = name;
	}

	@Override
	public void setDescriptor(String descriptor) 
	{
		super.descriptor = descriptor;
	}
}
