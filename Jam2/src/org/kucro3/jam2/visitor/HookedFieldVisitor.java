package org.kucro3.jam2.visitor;

import org.kucro3.jam2.util.Version;
import org.kucro3.util.Reference;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.TypePath;

public class HookedFieldVisitor extends FieldVisitor implements IHookedFieldVisitor {
	public HookedFieldVisitor(FieldVisitor fv)
	{
		super(Version.getASMVersion(), fv);
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
		Reference<AnnotationVisitor> ref;
		if(!listener.preVisitAnnotation(fv, descriptor, visible))
			throw new HookedInterruption();
		ref = new Reference<AnnotationVisitor>(super.visitAnnotation(descriptor, visible));
		listener.onVisitAnnotation(fv, descriptor, visible, ref);
		return ref.get();
	}
	
	@Override
	public void visitAttribute(Attribute attribute)
	{
		if(!listener.preVisitAttribute(fv, attribute))
			return;
		super.visitAttribute(attribute);
		listener.onVisitAttribute(fv, attribute);
	}
	
	@Override
	public void visitEnd()
	{
		if(!listener.preVisitEnd(fv))
			return;
		super.visitEnd();
		listener.onVisitEnd(fv);
	}
	
	@Override
	public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		Reference<AnnotationVisitor> ref;
		if(!listener.preVisitTypeAnnotation(fv, typeRef, typePath, desc, visible))
			throw new HookedInterruption();
		ref = new Reference<AnnotationVisitor>(super.visitTypeAnnotation(typeRef, typePath, desc, visible));
		return ref.get();
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
