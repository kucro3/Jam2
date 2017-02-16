package org.kucro3.jam2.util.context.hook;

import static org.kucro3.jam2.util.context.hook.Action.MC_setExceptions;
import static org.kucro3.jam2.util.context.hook.Action.MC_setModifier;
import static org.kucro3.jam2.util.context.hook.Action.MC_setSignature;

import org.kucro3.jam2.util.MethodContext;
import org.objectweb.asm.MethodVisitor;

abstract class HookedVisitedMethodCompoundTemplateB extends HookedVisitedMethodCompoundTemplateA {
	HookedVisitedMethodCompoundTemplateB(MethodContext mc) 
	{
		super(mc);
	}
		
	HookedVisitedMethodCompoundTemplateB(MethodContext mc, MethodVisitor mv) 
	{
		super(mc, mv);
	}
	
	@Override
	public void setModifier(int modifier)
	{
		mc.setModifier((Integer) hooks.fire(null, null, mc, MC_setModifier, modifier)[0]);
	}
	
	@Override
	public void setSignature(String signature)
	{
		mc.setSignature((String) hooks.fire(null, null, mc, MC_setSignature, signature)[0]);
	}
	
	@Override
	public void setExceptions(String[] exceptions)
	{
		mc.setExceptions((String[]) hooks.fire(null, null, mc, MC_setExceptions, (Object) exceptions)[0]);
	}
}