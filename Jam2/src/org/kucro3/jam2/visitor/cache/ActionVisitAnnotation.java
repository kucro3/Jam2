package org.kucro3.jam2.visitor.cache;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

class ActionVisitAnnotation implements Action {
	ActionVisitAnnotation(String desc, boolean visible, AnnotationCacheVisitor acv)
	{
		this.acv = acv;
		this.desc = desc;
		this.visible = visible;
	}
	
	@Override
	public void revisit(FieldVisitor fv)
	{
		_revisit(fv.visitAnnotation(desc, visible));
	}
	
	@Override
	public void revisit(MethodVisitor mv)
	{
		_revisit(mv.visitAnnotation(desc, visible));
	}
	
	@Override
	public void revisit(ClassVisitor cv)
	{
		_revisit(cv.visitAnnotation(desc, visible));
	}
	
	private final void _revisit(AnnotationVisitor av)
	{
		if(acv != null)
			acv.revisit(av);
	}
	
	final AnnotationCacheVisitor acv;
	
	final String desc;
	
	final boolean visible;
}
