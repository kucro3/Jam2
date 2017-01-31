package org.kucro3.jam2.util.annotation;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.TypePath;

public class TypeAnnotation extends Annotation {
	public TypeAnnotation()
	{
		super();
		this.typeRef = -1; // null check
	}
	
	public TypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		super(desc, visible);
		this.typeRef = typeRef;
		this.typePath = typePath;
	}
	
	@Override
	protected boolean checkAttribute()
	{
		return typeRef != -1 && typePath != null && super.checkAttribute();
	}
	
	@Override
	protected AnnotationVisitor preVisit(ClassVisitor cv)
	{
		return cv.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
	}
	
	@Override
	protected AnnotationVisitor preVisit(FieldVisitor fv)
	{
		return fv.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
	}
	
	@Override
	protected AnnotationVisitor preVisit(MethodVisitor mv)
	{
		return mv.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
	}
	
	public int getTypeRef()
	{
		return typeRef;
	}
	
	public TypePath getTypePath()
	{
		return typePath;
	}
	
	protected int typeRef;
	
	protected TypePath typePath;
}
