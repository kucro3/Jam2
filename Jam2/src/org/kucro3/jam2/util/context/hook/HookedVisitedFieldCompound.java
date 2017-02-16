package org.kucro3.jam2.util.context.hook;

import org.kucro3.jam2.util.FieldContext;
import org.kucro3.jam2.util.context.visitable.VisitedFieldCompound;
import org.objectweb.asm.FieldVisitor;

public abstract class HookedVisitedFieldCompound extends VisitedFieldCompound {
	public HookedVisitedFieldCompound(FieldContext fc)
	{
		super(fc);
	}
	
	public HookedVisitedFieldCompound(FieldContext fc, FieldVisitor fv)
	{
		super(fc, fv);
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