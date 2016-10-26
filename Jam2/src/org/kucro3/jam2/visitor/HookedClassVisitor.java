package org.kucro3.jam2.visitor;

import org.kucro3.jam2.util.Version;
import org.kucro3.util.Reference;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.TypePath;

public class HookedClassVisitor extends ClassVisitor implements IHookedClassVisitor {
	public HookedClassVisitor(ClassVisitor cv)
	{
		super(Version.getASMVersion(), cv);
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
		if(!listener.preVisit(cv, version, access, name, signature, superName, interfaces))
			return;
		super.visit(version, access, name, signature, superName, interfaces);
		listener.onVisit(cv, version, access, name, signature, superName, interfaces);
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible)
	{
		Reference<AnnotationVisitor> ref;
		if(!listener.preVisitAnnotation(cv, desc, visible))
			throw new HookedInterruption();
		ref = new Reference<AnnotationVisitor>(super.visitAnnotation(desc, visible));
		listener.onVisitAnnotation(cv, desc, visible, ref);
		return ref.get();
	}
	
	@Override
	public void visitAttribute(Attribute attribute)
	{
		if(!listener.preVisitAttribute(cv, attribute))
			return;
		super.visitAttribute(attribute);
		listener.onVisitAttribute(cv, attribute);
	}
	
	@Override
	public void visitEnd()
	{
		if(!listener.preVisitEnd(cv))
			return;
		super.visitEnd();
		listener.onVisitEnd(cv);
	}
	
	@Override
	public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value)
	{
		Reference<FieldVisitor> ref;
		if(!listener.preVisitField(cv, access, name, descriptor, signature, value))
			throw new HookedInterruption();
		ref = new Reference<FieldVisitor>(super.visitField(access, name, descriptor, signature, value));
		listener.onVisitField(cv, access, name, descriptor, signature, value, ref);
		return ref.get();
	}
	
	@Override
	public void visitInnerClass(String name, String outerName, String innerName, int access)
	{
		if(!listener.preVisitInnerClass(cv, name, outerName, innerName, access))
			return;
		super.visitInnerClass(name, outerName, innerName, access);
		listener.onVisitInnerClass(cv, name, outerName, innerName, access);
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions)
	{
		Reference<MethodVisitor> ref;
		if(!listener.preVisitMethod(cv, access, name, descriptor, signature, exceptions))
			throw new HookedInterruption();
		ref = new Reference<MethodVisitor>(super.visitMethod(access, name, descriptor, signature, exceptions));
		listener.onVisitMethod(cv, access, name, descriptor, signature, exceptions, ref);
		return ref.get();
	}
	
	@Override
	public void visitOuterClass(String owner, String name, String descriptor)
	{
		if(!listener.preVisitOuterClass(cv, owner, name, descriptor))
			return;
		super.visitOuterClass(owner, name, descriptor);
	}
	
	@Override
	public void visitSource(String source, String debug)
	{
		if(!listener.preVisitSource(cv, source, debug))
			return;
		super.visitSource(source, debug);
	}
	
	@Override
	public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		Reference<AnnotationVisitor> ref;
		if(!listener.preVisitTypeAnnotation(cv, typeRef, typePath, desc, visible))
			throw new HookedInterruption();
		ref = new Reference<AnnotationVisitor>(super.visitTypeAnnotation(typeRef, typePath, desc, visible));
		listener.onVisitTypeAnnotation(cv, typeRef, typePath, desc, visible, ref);
		return ref.get();
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
