package org.kucro3.jam2.util.context.visitable;

import org.kucro3.jam2.util.MethodContext;
import org.objectweb.asm.MethodVisitor;

public class VisitedMethodConstantCompound extends VisitedMethodCompound {
	public VisitedMethodConstantCompound(MethodContext mc, MethodVisitor mv) 
	{
		super(mc, mv);
	}
	
	public VisitedMethodConstantCompound(MethodContext mc) 
	{
		super(mc);
	}
}