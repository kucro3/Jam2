package org.kucro3.jam2.util.context.hook;

import org.kucro3.jam2.util.ClassContext;
import org.objectweb.asm.ClassVisitor;

public class HookedVisitableClassRestrictedModifiableCompound
		extends HookedVisitableClassCompoundTemplateB implements ClassContext.RestrictedModifiable {
	public HookedVisitableClassRestrictedModifiableCompound(ClassContext.RestrictedModifiable ref, ClassVisitor cv) 
	{
		super(ref, cv);
	}
	
	public HookedVisitableClassRestrictedModifiableCompound(ClassContext.RestrictedModifiable ref) 
	{
		super(ref);
	}
}
