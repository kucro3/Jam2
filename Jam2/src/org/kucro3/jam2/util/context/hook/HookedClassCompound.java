package org.kucro3.jam2.util.context.hook;

import org.kucro3.jam2.util.ClassContext;
import org.kucro3.jam2.util.context.ClassCompound;

public abstract class HookedClassCompound extends ClassCompound {
	public static HookedClassCompound newCompound(ClassContext ctx, HookFunction... funcs)
	{
		HookedClassCompound ret;
		if(ctx instanceof ClassContext.RestrictedModifiable)
			ret = new HookedClassRestrictedModifiableCompound((ClassContext.RestrictedModifiable) ctx);
		else if(ctx instanceof ClassContext.FullyModifiable)
			ret = new HookedClassFullyModifiableCompound((ClassContext.FullyModifiable) ctx);
		else
			ret = new HookedClassConstantCompound(ctx);
		ret.hooks.hook(funcs);
		return ret;
	}
	
	public HookedClassCompound(ClassContext ctx)
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
