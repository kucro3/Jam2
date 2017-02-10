package org.kucro3.jam2.util.context.visitable;

import org.kucro3.jam2.util.MethodContext;
import org.kucro3.jam2.util.Version;
import org.objectweb.asm.MethodVisitor;

public abstract class VisitableMethodContext extends MethodVisitor implements MethodContext {
	public VisitableMethodContext(MethodVisitor mv)
	{
		super(Version.getASMVersion(), mv);
	}
	
	public VisitableMethodContext()
	{
		super(Version.getASMVersion());
	}
}
