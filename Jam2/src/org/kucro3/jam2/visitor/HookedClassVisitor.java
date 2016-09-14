package org.kucro3.jam2.visitor;

import org.kucro3.jam2.util.ClassContext;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.TypePath;

public class HookedClassVisitor extends ClassVisitor implements IHookedClassVisitor {
	public HookedClassVisitor(ClassVisitor cv)
	{
		super(ClassContext.API, cv);
		this.cv = cv;
	}
	
	public HookedClassVisitor(ClassVisitor cv, ClassVisitorListener listener)
	{
		this(cv);
		this.setListener(listener);
	}
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
	{
		if(listener.onVisit(cv, version, access, name, signature, superName, interfaces))
			super.visit(version, access, name, signature, superName, interfaces);
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible)
	{
		if(listener.onVisitAnnotation(cv, desc, visible))
			return super.visitAnnotation(desc, visible);
		throw new HookedInterruption();
	}
	
	@Override
	public void visitAttribute(Attribute attribute)
	{
		if(listener.onVisitAttribute(cv, attribute))
			super.visitAttribute(attribute);
	}
	
	@Override
	public void visitEnd()
	{
		if(listener.onVisitEnd(cv))
			super.visitEnd();
	}
	
	@Override
	public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value)
	{
		if(listener.onVisitField(cv, access, name, descriptor, signature, value))
			return super.visitField(access, name, descriptor, signature, value);
		throw new HookedInterruption();
	}
	
	@Override
	public void visitInnerClass(String name, String outerName, String innerName, int access)
	{
		if(listener.onVisitInnerClass(cv, name, outerName, innerName, access))
			super.visitInnerClass(name, outerName, innerName, access);
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions)
	{
		if(listener.onVisitMethod(cv, access, name, descriptor, signature, exceptions))
			return super.visitMethod(access, name, descriptor, signature, exceptions);
		throw new HookedInterruption();
	}
	
	@Override
	public void visitOuterClass(String owner, String name, String descriptor)
	{
		if(listener.onVisitOuterClass(cv, owner, name, descriptor))
			super.visitOuterClass(owner, name, descriptor);
	}
	
	@Override
	public void visitSource(String source, String debug)
	{
		if(listener.onVisitSource(cv, source, debug))
			super.visitSource(source, debug);
	}
	
	@Override
	public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		if(listener.onVisitTypeAnnotation(cv, typeRef, typePath, desc, visible))
			return super.visitTypeAnnotation(typeRef, typePath, desc, visible);
		throw new HookedInterruption();
	}
	
	public ClassVisitorListener getListener()
	{
		return listener;
	}
	
	public void setListener(ClassVisitorListener listener)
	{
		this.listener = listener;
	}
	
	final ClassVisitor cv;
	
	ClassVisitorListener listener;
}
