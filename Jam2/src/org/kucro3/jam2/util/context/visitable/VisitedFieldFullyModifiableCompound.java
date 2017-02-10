package org.kucro3.jam2.util.context.visitable;

import org.kucro3.jam2.util.FieldContext;
import org.objectweb.asm.FieldVisitor;

public class VisitedFieldFullyModifiableCompound extends VisitedFieldCompound
		implements FieldContext.FullyModifiable {
	public VisitedFieldFullyModifiableCompound(FieldContext.FullyModifiable fc, FieldVisitor fv) 
	{
		super(fc, fv);
	}
	
	public VisitedFieldFullyModifiableCompound(FieldContext.FullyModifiable fc) 
	{
		super(fc);
	}
	
	@Override
	public void setDescriptor(String descriptor) 
	{
		this.fc.setDescriptor(descriptor);
	}
	
	@Override
	public void setSignature(String signature)
	{
		this.fc.setSignature(signature);
	}
	
	@Override
	public void setModifier(int modifier)
	{
		this.fc.setModifier(modifier);
	}
	
	@Override
	public void setName(String name)
	{
		this.fc.setName(name);
	}
	
	@Override
	public void setValue(Object value)
	{
		this.fc.setValue(value);
	}
}
