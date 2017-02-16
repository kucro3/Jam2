package org.kucro3.jam2.util.context.hook;

import static org.kucro3.jam2.util.context.hook.Action.MC_setDescriptor;
import static org.kucro3.jam2.util.context.hook.Action.MC_setName;

import org.kucro3.jam2.util.MethodContext;
import org.objectweb.asm.MethodVisitor;

public class HookedVisitedMethodFullyModifiableCompound 
		extends HookedVisitedMethodCompoundTemplateB implements MethodContext.FullyModifiable {
	public HookedVisitedMethodFullyModifiableCompound(MethodContext mc)
	{
		super(mc);
	}
	
	public HookedVisitedMethodFullyModifiableCompound(MethodContext mc, MethodVisitor mv)
	{
		super(mc, mv);
	}
	
	@Override
	public void setName(String name)
	{
		mc.setName((String) hooks.fire(null, null, mc, MC_setName, name)[0]);
	}
	
	@Override
	public void setDescriptor(String descriptor)
	{
		mc.setDescriptor((String) hooks.fire(null, null, mc, MC_setDescriptor, descriptor)[0]);
	}
}