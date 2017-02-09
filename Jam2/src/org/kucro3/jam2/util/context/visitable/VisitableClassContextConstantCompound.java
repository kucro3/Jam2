package org.kucro3.jam2.util.context.visitable;

import org.kucro3.jam2.util.ClassContext;
import org.objectweb.asm.ClassVisitor;

public class VisitableClassContextConstantCompound extends VisitableClassContextCompound {
	public VisitableClassContextConstantCompound(ClassContext ref) 
	{
		super(ref);
	}
	
	public VisitableClassContextConstantCompound(ClassContext ref, ClassVisitor cv)
	{
		super(ref, cv);
	}
}
