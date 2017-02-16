package org.kucro3.jam2.util.context.hook;

import org.kucro3.jam2.util.MethodContext;
import org.objectweb.asm.MethodVisitor;

public class HookedVisitedMethodRestrictedModifiableCompound
		extends HookedVisitedMethodCompoundTemplateB implements MethodContext.RestrictedModifiable {
	public HookedVisitedMethodRestrictedModifiableCompound(MethodContext.RestrictedModifiable mc) 
	{
		super(mc);
	}
	
	public HookedVisitedMethodRestrictedModifiableCompound(MethodContext.RestrictedModifiable mc, MethodVisitor mv) 
	{
		super(mc, mv);
	}
}