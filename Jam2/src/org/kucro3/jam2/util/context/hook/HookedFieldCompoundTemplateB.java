package org.kucro3.jam2.util.context.hook;

import org.kucro3.jam2.util.FieldContext;

import static org.kucro3.jam2.util.context.hook.Action.*;

abstract class HookedFieldCompoundTemplateB extends HookedFieldCompoundTemplateA {
	HookedFieldCompoundTemplateB(FieldContext ctx)
	{
		super(ctx);
	}
	
	@Override
	public void setDescriptor(String descriptor)
	{
		ref.setDescriptor((String) hooks.fire(null, ref, null, FC_setDescriptor, descriptor)[0]);
	}
	
	@Override
	public void setModifier(int modifier)
	{
		ref.setModifier((Integer) hooks.fire(null, ref, null, FC_setModifier, modifier)[0]);
	}
	
	@Override
	public void setSignature(String signature)
	{
		ref.setSignature((String) hooks.fire(null, ref, null, FC_setSignature, signature)[0]);
	}
	
	@Override
	public void setValue(Object value)
	{
		ref.setValue((Object) hooks.fire(null, ref, null, FC_setValue, value)[0]);
	}
}