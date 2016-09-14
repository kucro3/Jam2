package org.kucro3.jam2.visitor;

import org.kucro3.jam2.util.ClassContext;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.TypePath;

public class HookedFieldVisitor extends FieldVisitor implements IHookedFieldVisitor {
	public HookedFieldVisitor(FieldVisitor fv)
	{
		super(ClassContext.API, fv);
		this.fv = fv;
	}
	
	public HookedFieldVisitor(FieldVisitor fv, FieldVisitorListener listener)
	{
		this(fv);
		setListener(listener);
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String descriptor, boolean visible)
	{
		if(listener.onVisitAnnotation(fv, descriptor, visible))
			return super.visitAnnotation(descriptor, visible);
		throw new HookedInterruption();
	}
	
	@Override
	public void visitAttribute(Attribute attribute)
	{
		if(listener.onVisitAttribute(fv, attribute))
			super.visitAttribute(attribute);
	}
	
	@Override
	public void visitEnd()
	{
		if(listener.onVisitEnd(fv))
			super.visitEnd();
	}
	
	@Override
	public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		if(listener.onVisitTypeAnnotation(fv, typeRef, typePath, desc, visible))
			return super.visitTypeAnnotation(typeRef, typePath, desc, visible);
		throw new HookedInterruption();
	}
	
	@Override
	public FieldVisitorListener getListener()
	{
		return listener;
	}
	
	@Override
	public void setListener(FieldVisitorListener listener)
	{
		this.listener = listener;
	}
	
	final FieldVisitor fv;
	
	FieldVisitorListener listener;
}
