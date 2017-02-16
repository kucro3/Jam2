package org.kucro3.jam2.util.context.hook;

import org.kucro3.jam2.util.MethodContext;
import org.kucro3.jam2.util.context.visitable.VisitedMethodCompound;
import org.objectweb.asm.MethodVisitor;

public class HookedVisitedMethodCompound extends VisitedMethodCompound {
	public static HookedVisitedMethodCompound newCompound(VisitedMethodCompound vmc, HookFunction... funcs)
	{
		HookedVisitedMethodCompound ret;
		if(vmc instanceof MethodContext.RestrictedModifiable)
			ret = new HookedVisitedMethodRestrictedModifiableCompound((MethodContext.RestrictedModifiable) vmc);
		else if(vmc instanceof MethodContext.FullyModifiable)
			ret = new HookedVisitedMethodFullyModifiableCompound((MethodContext.FullyModifiable) vmc);
		else
			ret = new HookedVisitedMethodConstantCompound(vmc);
		ret.hook(funcs);
		return ret;
	}
	
	public HookedVisitedMethodCompound(MethodContext mc)
	{
		super(mc);
	}
	
	public HookedVisitedMethodCompound(MethodContext mc, MethodVisitor mv)
	{
		super(mc, mv);
	}
	
	public void hook(HookFunction... funcs)
	{
		hooks.hook(funcs);
	}
	
	public void unhook(HookFunction... funcs)
	{
		hooks.unhook(funcs);
	}
	
	public void unhookAll()
	{
		hooks.unhookAll();
	}
	
	protected HookFunctionCollection hooks = new HookFunctionCollection();
}