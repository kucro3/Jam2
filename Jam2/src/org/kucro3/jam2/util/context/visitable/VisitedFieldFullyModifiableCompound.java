package org.kucro3.jam2.util.context.visitable;

import org.kucro3.jam2.util.FieldContext;
import org.objectweb.asm.FieldVisitor;

public class VisitedFieldFullyModifiableCompound extends VisitedFieldCompound
		implements FieldContext.FullyModifiable {
	public VisitedFieldFullyModifiableCompound(FieldContext.FullyModifiable fc, FieldVisitor fv) 
	{
		super(fc, fv);
	}
}
