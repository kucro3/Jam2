package org.kucro3.jam2.visitor;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

class ActionVisitEnd implements Action {
	@Override
	public void revisit(AnnotationVisitor av)
	{
		av.visitEnd();
	}
	
	@Override
	public void revisit(FieldVisitor fv)
	{
		fv.visitEnd();
	}
	
	@Override
	public void revisit(MethodVisitor mv)
	{
		mv.visitEnd();
	}
	
	@Override
	public void revisit(ClassVisitor cv)
	{
		cv.visitEnd();
	}
}
