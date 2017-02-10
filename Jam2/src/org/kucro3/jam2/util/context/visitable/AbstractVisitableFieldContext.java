package org.kucro3.jam2.util.context.visitable;

import org.kucro3.jam2.util.FieldContext;
import org.kucro3.jam2.util.Version;
import org.objectweb.asm.FieldVisitor;

public abstract class AbstractVisitableFieldContext extends FieldVisitor implements FieldContext {
	public AbstractVisitableFieldContext(FieldVisitor fv) 
	{
		super(Version.getASMVersion(), fv);
	}
	
	public AbstractVisitableFieldContext() 
	{
		super(Version.getASMVersion());
	}
}
