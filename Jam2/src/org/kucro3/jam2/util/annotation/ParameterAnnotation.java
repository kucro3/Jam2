package org.kucro3.jam2.util.annotation;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

public class ParameterAnnotation extends Annotation {
	public ParameterAnnotation()
	{
		super();
		this.index = -1;
	}
	
	public ParameterAnnotation(int index, String desc, boolean visible)
	{
		super(desc, visible);
		this.index = index;
	}
	
	@Override
	protected boolean checkAttribute()
	{
		return index != -1 && super.checkAttribute();
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
		return mv.visitParameterAnnotation(index, descriptor, visible);
	}
	
	public int getIndex()
	{
		return index;
	}
	
	protected int index;
}
