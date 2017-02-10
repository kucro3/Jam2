package org.kucro3.jam2.util.context.visitable;

import org.kucro3.jam2.util.MethodContext;
import org.kucro3.jam2.util.Version;
import org.objectweb.asm.MethodVisitor;

public abstract class AbstractVisitableMethodContext extends MethodVisitor implements MethodContext {
	public AbstractVisitableMethodContext(MethodVisitor mv)
	{
		super(Version.getASMVersion(), mv);
	}
	
	public AbstractVisitableMethodContext()
	{
		super(Version.getASMVersion());
	}
}
