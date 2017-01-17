package org.kucro3.jam2.visitor.cache;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.TypePath;

class ActionVisitTypeAnnotation implements Action {
	ActionVisitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible,
			AnnotationCacheVisitor acv)
	{
		this.acv = acv;
		this.typeRef = typeRef;
		this.typePath = typePath;
		this.desc = desc;
		this.visible = visible;
	}
	
	@Override
	public void revisit(FieldVisitor fv)
	{
		_revisit(fv.visitTypeAnnotation(typeRef, typePath, desc, visible));
	}
	
	@Override
	public void revisit(MethodVisitor mv)
	{
		_revisit(mv.visitTypeAnnotation(typeRef, typePath, desc, visible));
	}
	
	@Override
	public void revisit(ClassVisitor cv)
	{
		_revisit(cv.visitTypeAnnotation(typeRef, typePath, desc, visible));
	}
	
	private final void _revisit(AnnotationVisitor av)
	{
		acv.revisitOptional(av);
	}
	
	final AnnotationCacheVisitor acv;
	
	final int typeRef;
	
	final TypePath typePath;
	
	final String desc;
	
	final boolean visible;
}