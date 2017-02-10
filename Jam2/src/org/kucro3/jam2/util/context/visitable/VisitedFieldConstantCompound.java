package org.kucro3.jam2.util.context.visitable;

import org.kucro3.jam2.util.FieldContext;
import org.objectweb.asm.FieldVisitor;

public class VisitedFieldConstantCompound extends VisitedFieldCompound {
	public VisitedFieldConstantCompound(FieldContext fc, FieldVisitor fv)
	{
		super(fc, fv);
	}
	
	public VisitedFieldConstantCompound(FieldContext fc)
	{
		super(fc);
	}
}
