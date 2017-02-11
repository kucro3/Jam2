package org.kucro3.jam2.util.context.hook;

import org.kucro3.jam2.util.FieldContext;

import static org.kucro3.jam2.util.context.hook.Action.*;

abstract class HookedFieldCompoundTemplateA extends HookedFieldCompound {
	HookedFieldCompoundTemplateA(FieldContext ctx) 
	{
		super(ctx);
	}
	
	@Override
	public String getDeclaringClass()
	{
		hooks.fire(null, ref, null, FC_getDeclaringClass, (Object[]) null);
		return ref.getDeclaringClass();
	}
	
	@Override
	public String getDescriptor()
	{
		hooks.fire(null, ref, null, FC_getDescriptor, (Object[]) null);
		return ref.getDescriptor();
	}
	
	@Override
	public int getModifier()
	{
		hooks.fire(null, ref, null, FC_getModifier, (Object[]) null);
		return ref.getModifier();
	}
	
	@Override
	public String getName()
	{
		hooks.fire(null, ref, null, FC_getName, (Object[]) null);
		return ref.getName();
	}
	
	@Override
	public String getSignature()
	{
		hooks.fire(null, ref, null, FC_getSignature, (Object[]) null);
		return ref.getSignature();
	}
	
	@Override
	public Object getValue()
	{
		hooks.fire(null, ref, null, FC_getValue, (Object[]) null);
		return ref.getValue();
	}
}