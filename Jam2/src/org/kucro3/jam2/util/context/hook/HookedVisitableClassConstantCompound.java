package org.kucro3.jam2.util.context.hook;

import org.kucro3.jam2.util.ClassContext;
import org.objectweb.asm.ClassVisitor;

public class HookedVisitableClassConstantCompound extends HookedVisitableClassCompoundTemplateA {
	public HookedVisitableClassConstantCompound(ClassContext ref) 
	{
		super(ref);
	}
	
	public HookedVisitableClassConstantCompound(ClassContext ref, ClassVisitor cv) 
	{
		super(ref, cv);
	}
}