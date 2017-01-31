package org.kucro3.jam2.util.annotation;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.TypePath;

public class TryCatchAnnotation extends TypeAnnotation {
	public TryCatchAnnotation()
	{
		super();
	}
	
	public TryCatchAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		super(typeRef, typePath, desc, visible);
	}
	
	@Override
	protected AnnotationVisitor preVisit(ClassVisitor cv)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	protected AnnotationVisitor preVisit(FieldVisitor fv)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	protected AnnotationVisitor preVisit(MethodVisitor mv)
	{
		return mv.visitTryCatchAnnotation(typeRef, typePath, descriptor, visible);
	}
}
