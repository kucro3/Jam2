package org.kucro3.jam2.util.context.hook;

import org.kucro3.jam2.util.MethodContext;

import static org.kucro3.jam2.util.context.hook.Action.*;

abstract class HookedMethodCompoundTemplateA extends HookedMethodCompound {
	HookedMethodCompoundTemplateA(MethodContext ctx)
	{
		super(ctx);
	}
	
	@Override
	public String getDeclaringClass()
	{
		hooks.fire(null, null, ref, MC_getDeclaringClass, (Object[]) null);
		return ref.getDeclaringClass();
	}
	
	@Override
	public String getDescriptor()
	{
		hooks.fire(null, null, ref, MC_getDescriptor, (Object[]) null);
		return ref.getDescriptor();
	}
	
	@Override
	public int getModifier()
	{
		hooks.fire(null, null, ref, MC_getModifier, (Object[]) null);
		return ref.getModifier();
	}
	
	@Override
	public String getName()
	{
		hooks.fire(null, null, ref, MC_getName, (Object[]) null);
		return ref.getName();
	}
	
	@Override
	public String getSignature()
	{
		hooks.fire(null, null, ref, MC_getSignature, (Object[]) null);
		return ref.getSignature();
	}
	
	@Override
	public String[] getExceptions()
	{
		hooks.fire(null, null, ref, MC_getExceptions, (Object[]) null);
		return ref.getExceptions();
	}
}