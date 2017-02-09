package org.kucro3.jam2.util.context.visitable;

import org.kucro3.jam2.util.ClassContext;
import org.objectweb.asm.ClassVisitor;

public class VisitableClassContextRestrictedModifiableCompound extends VisitableClassContextCompound
		implements ClassContext.RestrictedModifiable {
	public VisitableClassContextRestrictedModifiableCompound(ClassContext ref)
	{
		super(ref);
	}
	
	public VisitableClassContextRestrictedModifiableCompound(ClassContext ref, ClassVisitor cv)
	{
		super(ref, cv);
	}
}
