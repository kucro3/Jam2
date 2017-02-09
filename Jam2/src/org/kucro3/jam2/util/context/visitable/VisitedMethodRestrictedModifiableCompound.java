package org.kucro3.jam2.util.context.visitable;

import org.kucro3.jam2.util.MethodContext;
import org.objectweb.asm.MethodVisitor;

public class VisitedMethodRestrictedModifiableCompound extends VisitedMethodCompound
		implements MethodContext.RestrictedModifiable {
	public VisitedMethodRestrictedModifiableCompound(MethodContext.RestrictedModifiable mc, MethodVisitor mv)
	{
		super(mc, mv);
	}
}
