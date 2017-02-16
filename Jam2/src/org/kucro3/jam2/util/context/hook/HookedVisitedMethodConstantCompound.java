package org.kucro3.jam2.util.context.hook;

import org.kucro3.jam2.util.MethodContext;
import org.objectweb.asm.MethodVisitor;

public class HookedVisitedMethodConstantCompound extends HookedVisitedMethodCompoundTemplateA {
	public HookedVisitedMethodConstantCompound(MethodContext mc) 
	{
		super(mc);
	}
	
	public HookedVisitedMethodConstantCompound(MethodContext mc, MethodVisitor mv) 
	{
		super(mc, mv);
	}
}