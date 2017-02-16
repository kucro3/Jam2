package org.kucro3.jam2.util.context.hook;

import static org.kucro3.jam2.util.context.hook.Action.MC_getDeclaringClass;
import static org.kucro3.jam2.util.context.hook.Action.MC_getDescriptor;
import static org.kucro3.jam2.util.context.hook.Action.MC_getExceptions;
import static org.kucro3.jam2.util.context.hook.Action.MC_getModifier;
import static org.kucro3.jam2.util.context.hook.Action.MC_getName;
import static org.kucro3.jam2.util.context.hook.Action.MC_getSignature;

import org.kucro3.jam2.util.MethodContext;
import org.objectweb.asm.MethodVisitor;

abstract class HookedVisitedMethodCompoundTemplateA extends HookedVisitedMethodCompound {
	HookedVisitedMethodCompoundTemplateA(MethodContext mc)
	{
		super(mc);
	}
	
	HookedVisitedMethodCompoundTemplateA(MethodContext mc, MethodVisitor mv)
	{
		super(mc, mv);
	}

	@Override
	public String getDeclaringClass()
	{
		hooks.fire(null, null, mc, MC_getDeclaringClass, (Object[]) null);
		return mc.getDeclaringClass();
	}
	
	@Override
	public String getDescriptor()
	{
		hooks.fire(null, null, mc, MC_getDescriptor, (Object[]) null);
		return mc.getDescriptor();
	}
	
	@Override
	public int getModifier()
	{
		hooks.fire(null, null, mc, MC_getModifier, (Object[]) null);
		return mc.getModifier();
	}
	
	@Override
	public String getName()
	{
		hooks.fire(null, null, mc, MC_getName, (Object[]) null);
		return mc.getName();
	}
	
	@Override
	public String getSignature()
	{
		hooks.fire(null, null, mc, MC_getSignature, (Object[]) null);
		return mc.getSignature();
	}
	
	@Override
	public String[] getExceptions()
	{
		hooks.fire(null, null, mc, MC_getExceptions, (Object[]) null);
		return mc.getExceptions();
	}
}