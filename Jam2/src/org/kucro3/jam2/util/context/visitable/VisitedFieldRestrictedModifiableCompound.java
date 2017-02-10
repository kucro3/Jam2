package org.kucro3.jam2.util.context.visitable;

import org.kucro3.jam2.util.FieldContext;
import org.objectweb.asm.FieldVisitor;

public class VisitedFieldRestrictedModifiableCompound extends VisitedFieldCompound
		implements FieldContext.RestrictedModifiable {
	public VisitedFieldRestrictedModifiableCompound(FieldContext.RestrictedModifiable fc, FieldVisitor fv)
	{
		super(fc, fv);
	}
	
	public VisitedFieldRestrictedModifiableCompound(FieldContext.RestrictedModifiable fc)
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
	public void setValue(Object value)
	{
		this.fc.setValue(value);
	}
}