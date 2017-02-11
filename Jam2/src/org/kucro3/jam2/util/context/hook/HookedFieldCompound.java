package org.kucro3.jam2.util.context.hook;

import org.kucro3.jam2.util.FieldContext;
import org.kucro3.jam2.util.context.FieldCompound;

public abstract class HookedFieldCompound extends FieldCompound {
	public static HookedFieldCompound newCompound(FieldContext ctx, HookFunction... funcs)
	{
		HookedFieldCompound ret;
		if(ctx instanceof FieldContext.RestrictedModifiable)
			ret = new HookedFieldRestrictedModifiableCompound((FieldContext.RestrictedModifiable) ctx);
		else if(ctx instanceof FieldContext.FullyModifiable)
			ret = new HookedFieldFullyModifiableCompound((FieldContext.FullyModifiable) ctx);
		else
			ret = new HookedFieldConstantCompound(ctx);
		ret.hooks.hook(funcs);
		return ret;
	}
	
	public HookedFieldCompound(FieldContext ctx)
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
