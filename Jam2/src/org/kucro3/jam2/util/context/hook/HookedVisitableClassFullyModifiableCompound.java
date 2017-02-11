package org.kucro3.jam2.util.context.hook;

import org.kucro3.jam2.util.ClassContext;
import org.objectweb.asm.ClassVisitor;

public class HookedVisitableClassFullyModifiableCompound
		extends HookedVisitableClassCompoundTemplateB implements ClassContext.FullyModifiable {
	public HookedVisitableClassFullyModifiableCompound(ClassContext.FullyModifiable ref, ClassVisitor cv) 
	{
		super(ref, cv);
	}
	
	public HookedVisitableClassFullyModifiableCompound(ClassContext.FullyModifiable ref) 
	{
		super(ref);
	}

	@Override
	public void setName(String name) 
	{
		ref.setName((String) hooks.fire(ref, null, null, Action.CC_setName, name)[0]);
	}
}
