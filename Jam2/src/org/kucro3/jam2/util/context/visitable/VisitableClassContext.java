package org.kucro3.jam2.util.context.visitable;

import org.kucro3.jam2.util.ClassContext;
import org.kucro3.jam2.util.Version;
import org.objectweb.asm.ClassVisitor;

public abstract class VisitableClassContext extends ClassVisitor implements ClassContext {
	public VisitableClassContext() 
	{
		super(Version.getASMVersion());
	}
	
	public VisitableClassContext(ClassVisitor cv)
	{
		super(Version.getASMVersion(), cv);
	}
}
