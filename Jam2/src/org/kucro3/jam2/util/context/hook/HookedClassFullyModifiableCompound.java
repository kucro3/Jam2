package org.kucro3.jam2.util.context.hook;

import org.kucro3.jam2.util.ClassContext;

public class HookedClassFullyModifiableCompound 
		extends HookedClassCompoundTemplateB implements ClassContext.FullyModifiable {
	public HookedClassFullyModifiableCompound(ClassContext.FullyModifiable ctx) 
	{
		super(ctx);
	}
	
	@Override
	public void setName(String name) 
	{
		ref.setName((String) hooks.fire(ref, null, null, Action.CC_setName, name)[0]);
	}
}