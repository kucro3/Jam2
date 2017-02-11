package org.kucro3.jam2.util.context.hook;

import org.kucro3.jam2.util.ClassContext;

public class HookedClassRestrictedModifiableCompound
		extends HookedClassCompoundTemplateB implements ClassContext.RestrictedModifiable {
	public HookedClassRestrictedModifiableCompound(ClassContext.RestrictedModifiable ctx) 
	{
		super(ctx);
	}
}
