package org.kucro3.jam2.util.context.hook;

import org.kucro3.jam2.util.FieldContext;
import org.objectweb.asm.FieldVisitor;

public class HookedVisitedFieldConstantCompound extends HookedVisitedFieldCompoundTemplateA {
	public HookedVisitedFieldConstantCompound(FieldContext fc)
	{
		super(fc);
	}
	
	public HookedVisitedFieldConstantCompound(FieldContext fc, FieldVisitor fv)
	{
		super(fc, fv);
	}
}