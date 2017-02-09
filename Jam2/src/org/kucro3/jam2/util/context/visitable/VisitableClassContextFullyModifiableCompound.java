package org.kucro3.jam2.util.context.visitable;

import org.kucro3.jam2.util.ClassContext;
import org.objectweb.asm.ClassVisitor;

public class VisitableClassContextFullyModifiableCompound extends VisitableClassContextCompound
		implements ClassContext.FullyModifiable {
	public VisitableClassContextFullyModifiableCompound(ClassContext.FullyModifiable ref) 
	{
		super(ref);
	}
	
	public VisitableClassContextFullyModifiableCompound(ClassContext.FullyModifiable ref, ClassVisitor cv) 
	{
		super(ref, cv);
	}
}
