package org.kucro3.jam2.util.context.hook;

import org.kucro3.jam2.util.MethodContext;
import org.kucro3.jam2.util.context.MethodCompound;

public abstract class HookedMethodCompound extends MethodCompound {
	public static HookedMethodCompound newCompound(MethodContext ctx, HookFunction... funcs)
	{
		HookedMethodCompound ret;
		if(ctx instanceof MethodContext.RestrictedModifiable)
			ret = new HookedMethodRestrictedModifiableCompound((MethodContext.RestrictedModifiable) ctx);
		else if(ctx instanceof MethodContext.FullyModifiable)
			ret = new HookedMethodFullyModifiableCompound((MethodContext.FullyModifiable) ctx);
		else
			ret = new HookedMethodConstantCompound(ctx);
		ret.hook(funcs);
		return ret;
	}
	
	public HookedMethodCompound(MethodContext ctx) 
	{
		super(ctx);
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
	
	protected final HookFunctionCollection hooks = new HookFunctionCollection();
}
