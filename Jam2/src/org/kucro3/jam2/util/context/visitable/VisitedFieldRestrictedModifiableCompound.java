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
}