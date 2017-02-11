package org.kucro3.jam2.util.context.hook;

import org.kucro3.jam2.util.MethodContext;

public class HookedMethodRestrictedModifiableCompound 
		extends HookedMethodCompoundTemplateB implements MethodContext.RestrictedModifiable {
	public HookedMethodRestrictedModifiableCompound(MethodContext.RestrictedModifiable ctx)
	{
		super(ctx);
	}
}
