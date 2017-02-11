package org.kucro3.jam2.util.context.hook;

import org.kucro3.jam2.util.MethodContext;

import static org.kucro3.jam2.util.context.hook.Action.*;

public class HookedMethodFullyModifiableCompound
		extends HookedMethodCompoundTemplateB implements MethodContext.FullyModifiable {
	public HookedMethodFullyModifiableCompound(MethodContext.FullyModifiable ctx)
	{
		super(ctx);
	}
	
	@Override
	public void setName(String name)
	{
		ref.setName((String) hooks.fire(null, null, ref, MC_setName, name)[0]);
	}
	
	@Override
	public void setDescriptor(String descriptor)
	{
		ref.setDescriptor((String) hooks.fire(null, null, ref, MC_setDescriptor, descriptor)[0]);
	}
}