package org.kucro3.jam2.util.context.hook;

import static org.kucro3.jam2.util.context.hook.Action.FC_getDeclaringClass;
import static org.kucro3.jam2.util.context.hook.Action.FC_getDescriptor;
import static org.kucro3.jam2.util.context.hook.Action.FC_getModifier;
import static org.kucro3.jam2.util.context.hook.Action.FC_getName;
import static org.kucro3.jam2.util.context.hook.Action.FC_getSignature;
import static org.kucro3.jam2.util.context.hook.Action.FC_getValue;

import org.kucro3.jam2.util.FieldContext;
import org.objectweb.asm.FieldVisitor;

abstract class HookedVisitedFieldCompoundTemplateA extends HookedVisitedFieldCompound {
	HookedVisitedFieldCompoundTemplateA(FieldContext fc) 
	{
		super(fc);
	}
	
	HookedVisitedFieldCompoundTemplateA(FieldContext fc, FieldVisitor fv) 
	{
		super(fc, fv);
	}
	
	@Override
	public String getDeclaringClass()
	{
		hooks.fire(null, fc, null, FC_getDeclaringClass, (Object[]) null);
		return fc.getDeclaringClass();
	}
	
	@Override
	public String getDescriptor()
	{
		hooks.fire(null, fc, null, FC_getDescriptor, (Object[]) null);
		return fc.getDescriptor();
	}
	
	@Override
	public int getModifier()
	{
		hooks.fire(null, fc, null, FC_getModifier, (Object[]) null);
		return fc.getModifier();
	}
	
	@Override
	public String getName()
	{
		hooks.fire(null, fc, null, FC_getName, (Object[]) null);
		return fc.getName();
	}
	
	@Override
	public String getSignature()
	{
		hooks.fire(null, fc, null, FC_getSignature, (Object[]) null);
		return fc.getSignature();
	}
	
	@Override
	public Object getValue()
	{
		hooks.fire(null, fc, null, FC_getValue, (Object[]) null);
		return fc.getValue();
	}
}