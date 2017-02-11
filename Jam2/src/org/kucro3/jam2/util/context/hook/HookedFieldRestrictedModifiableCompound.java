package org.kucro3.jam2.util.context.hook;

import org.kucro3.jam2.util.FieldContext;

public class HookedFieldRestrictedModifiableCompound 
		extends HookedFieldCompoundTemplateB implements FieldContext.RestrictedModifiable {
	public HookedFieldRestrictedModifiableCompound(FieldContext.RestrictedModifiable ctx) 
	{
		super(ctx);
	}
}