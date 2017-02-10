package org.kucro3.jam2.util.context.visitable;

import org.kucro3.jam2.util.FieldContext;
import org.kucro3.jam2.util.Version;
import org.objectweb.asm.FieldVisitor;

public abstract class VisitableFieldContext extends FieldVisitor implements FieldContext {
	public VisitableFieldContext(FieldVisitor fv) 
	{
		super(Version.getASMVersion(), fv);
	}
	
	public VisitableFieldContext() 
	{
		super(Version.getASMVersion());
	}
}
