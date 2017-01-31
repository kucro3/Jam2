package org.kucro3.jam2.util.annotation;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.TypePath;

public class LocalVariableAnnotation extends TypeAnnotation {
	public LocalVariableAnnotation()
	{
		super();
	}
	
	public LocalVariableAnnotation(int typeRef, TypePath typePath,
			Label[] start, Label[] end, int[] index, String desc, boolean visible)
	{
		super(typeRef, typePath, desc, visible);
		this.start = start;
		this.end = end;
		this.index = index;
	}
	
	@Override
	protected boolean checkAttribute()
	{
		return index != null && start != null && end != null && super.checkAttribute();
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
		return mv.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, descriptor, visible);
	}
	
	public Label[] getStart()
	{
		return start;
	}
	
	public Label[] getEnd()
	{
		return end;
	}
	
	public int[] getIndex()
	{
		return index;
	}
	
	protected Label[] start;
	
	protected Label[] end;
	
	protected int[] index;
}
