package org.kucro3.jam2.util.context.hook;

import org.kucro3.jam2.util.FieldContext;
import org.objectweb.asm.FieldVisitor;

public class HookedVisitedFieldRestrictedModifiableCompound
		extends HookedVisitedFieldCompoundTemplateB implements FieldContext.RestrictedModifiable {
	public HookedVisitedFieldRestrictedModifiableCompound(FieldContext.RestrictedModifiable fc)
	{
		super(fc);
	}
	
	public HookedVisitedFieldRestrictedModifiableCompound(FieldContext.RestrictedModifiable fc, FieldVisitor fv)
	{
		super(fc, fv);
	}
}