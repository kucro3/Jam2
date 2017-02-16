package org.kucro3.jam2.util.context.hook;

import static org.kucro3.jam2.util.context.hook.Action.FC_setDescriptor;
import static org.kucro3.jam2.util.context.hook.Action.FC_setModifier;
import static org.kucro3.jam2.util.context.hook.Action.FC_setSignature;
import static org.kucro3.jam2.util.context.hook.Action.FC_setValue;

import org.kucro3.jam2.util.FieldContext;
import org.objectweb.asm.FieldVisitor;

abstract class HookedVisitedFieldCompoundTemplateB extends HookedVisitedFieldCompoundTemplateA {
	HookedVisitedFieldCompoundTemplateB(FieldContext fc)
	{
		super(fc);
	}
	
	HookedVisitedFieldCompoundTemplateB(FieldContext fc, FieldVisitor fv)
	{
		super(fc, fv);
	}
	
	@Override
	public void setDescriptor(String descriptor)
	{
		fc.setDescriptor((String) hooks.fire(null, fc, null, FC_setDescriptor, descriptor)[0]);
	}
	
	@Override
	public void setModifier(int modifier)
	{
		fc.setModifier((Integer) hooks.fire(null, fc, null, FC_setModifier, modifier)[0]);
	}
	
	@Override
	public void setSignature(String signature)
	{
		fc.setSignature((String) hooks.fire(null, fc, null, FC_setSignature, signature)[0]);
	}
	
	@Override
	public void setValue(Object value)
	{
		fc.setValue((Object) hooks.fire(null, fc, null, FC_setValue, value)[0]);
	}
}