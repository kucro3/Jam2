package org.kucro3.jam2.util;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.TypePath;

public class ClassContextVisitor extends ClassVisitor {
	public ClassContextVisitor(int api) 
	{
		super(api);
	}
	
	public ClassContextVisitor()
	{
		super(Version.getASMVersion());
	}
	
	public ClassContext getContext()
	{
		return ctx;
	}
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
	{
		this.ctx = new ClassContext(version, access, name, signature, superName, interfaces);
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible)
	{
		return ctx.visitAnnotation(desc, visible);
	}
	
	@Override
	public void visitAttribute(Attribute attr)
	{
		ctx.visitAttribute(attr);
	}
	
	@Override
	public void visitEnd()
	{
		ctx.visitEnd();
	}
	
	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value)
	{
		return ctx.visitField(access, name, desc, signature, value);
	}
	
	@Override
	public void visitInnerClass(String name, String outerName, String innerName, int access)
	{
		ctx.visitInnerClass(name, outerName, innerName, access);
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
	{
		return ctx.visitMethod(access, name, desc, signature, exceptions);
	}
	
	@Override
	public void visitOuterClass(String owner, String name, String desc)
	{
		ctx.visitOuterClass(owner, name, desc);
	}
	
	@Override
	public void visitSource(String source, String debug)
	{
		ctx.visitSource(source, debug);
	}
	
	@Override
	public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		return ctx.visitTypeAnnotation(typeRef, typePath, desc, visible);
	}
	
	private ClassContext ctx;
}
