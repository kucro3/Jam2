package org.kucro3.jam2.util.context.hook;

import org.kucro3.jam2.util.ClassContext;
import org.kucro3.jam2.util.context.visitable.VisitableClassContext;
import org.kucro3.jam2.util.context.visitable.VisitableClassContextCompound;
import org.objectweb.asm.ClassVisitor;

public abstract class HookedVisitableClassCompound extends VisitableClassContextCompound {
	public static HookedVisitableClassCompound newCompound(VisitableClassContext vcc, HookFunction... funcs)
	{
		HookedVisitableClassCompound ret;
		if(vcc instanceof ClassContext.RestrictedModifiable)
			ret = new HookedVisitableClassRestrictedModifiableCompound((ClassContext.RestrictedModifiable) vcc);
		else if(vcc instanceof ClassContext.FullyModifiable)
			ret = new HookedVisitableClassFullyModifiableCompound((ClassContext.FullyModifiable) vcc);
		else
			ret = new HookedVisitableClassConstantCompound(vcc);
		ret.hook(funcs);
		return ret;
	}
	
	public HookedVisitableClassCompound(ClassContext ref, ClassVisitor cv)
	{
		super(ref, cv);
	}
	
	public HookedVisitableClassCompound(ClassContext ref)
	{
		super(ref);
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