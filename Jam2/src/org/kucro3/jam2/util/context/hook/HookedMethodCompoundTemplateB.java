package org.kucro3.jam2.util.context.hook;

import org.kucro3.jam2.util.MethodContext;

import static org.kucro3.jam2.util.context.hook.Action.*;

abstract class HookedMethodCompoundTemplateB extends HookedMethodCompoundTemplateA {
	HookedMethodCompoundTemplateB(MethodContext ctx)
	{
		super(ctx);
	}
	
	@Override
	public void setModifier(int modifier)
	{
		ref.setModifier((Integer) hooks.fire(null, null, ref, MC_setModifier, modifier)[0]);
	}
	
	@Override
	public void setSignature(String signature)
	{
		ref.setSignature((String) hooks.fire(null, null, ref, MC_setSignature, signature)[0]);
	}
	
	@Override
	public void setExceptions(String[] exceptions)
	{
		ref.setExceptions((String[]) hooks.fire(null, null, ref, MC_setExceptions, (Object) exceptions)[0]);
	}
}
