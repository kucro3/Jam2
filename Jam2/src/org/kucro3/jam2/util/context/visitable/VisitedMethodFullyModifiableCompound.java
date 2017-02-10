package org.kucro3.jam2.util.context.visitable;

import org.kucro3.jam2.util.MethodContext;
import org.objectweb.asm.MethodVisitor;

public class VisitedMethodFullyModifiableCompound extends VisitedMethodCompound
		implements MethodContext.FullyModifiable {
	public VisitedMethodFullyModifiableCompound(MethodContext.FullyModifiable mc, MethodVisitor mv) 
	{
		super(mc, mv);
	}
	
	public VisitedMethodFullyModifiableCompound(MethodContext.FullyModifiable mc) 
	{
		super(mc);
	}
}
