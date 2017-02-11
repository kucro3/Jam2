package org.kucro3.jam2.util.context.hook;

import org.kucro3.jam2.util.FieldContext;

public class HookedFieldFullyModifiableCompound
		extends HookedFieldCompoundTemplateB implements FieldContext.FullyModifiable {
	public HookedFieldFullyModifiableCompound(FieldContext.FullyModifiable ctx) 
	{
		super(ctx);
	}
	
	@Override
	public void setName(String name)
	{
		ref.setName((String) hooks.fire(null, ref, null, Action.FC_setName, name)[0]);
	}
}
