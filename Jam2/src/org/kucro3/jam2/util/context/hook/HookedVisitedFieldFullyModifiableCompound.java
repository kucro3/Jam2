package org.kucro3.jam2.util.context.hook;

import org.kucro3.jam2.util.FieldContext;
import org.objectweb.asm.FieldVisitor;

public class HookedVisitedFieldFullyModifiableCompound 
		extends HookedVisitedFieldCompoundTemplateB implements FieldContext.FullyModifiable {
	public HookedVisitedFieldFullyModifiableCompound(FieldContext.FullyModifiable fc) 
	{
		super(fc);
	}
	
	public HookedVisitedFieldFullyModifiableCompound(FieldContext.FullyModifiable fc, FieldVisitor fv) 
	{
		super(fc, fv);
	}
	
	@Override
	public void setName(String name)
	{
		fc.setName((String) hooks.fire(null, fc, null, Action.FC_setName, name)[0]);
	}
}