package org.kucro3.jam2.util.builder;

import org.kucro3.jam2.util.ClassContext;
import org.kucro3.jam2.util.builder.AnnotationBuilder.ClassAnnotationBuilder;
import org.objectweb.asm.TypePath;

public class ClassBuilder {
	public ClassBuilder(int version, int access, String name, String signature, String superName, String[] interfaces)
	{
		this.cctx = new ClassContext(version, access, name, signature, superName, interfaces);
	}
	
	public ClassBuilder appendSource(String source, String debug)
	{
		cctx.visitSource(source, debug);
		return this;
	}
	
	public ClassBuilder appendOuterClass(String owner, String name, String descriptor)
	{
		cctx.visitOuterClass(owner, name, descriptor);
		return this;
	}
	
	public ClassBuilder appendInnerClass(String name, String outerName, String innerName, int access)
	{
		cctx.visitInnerClass(name, outerName, innerName, access);
		return this;
	}
	
	public ClassAnnotationBuilder appendAnnotation(String desc, boolean visible)
	{
		return new ClassAnnotationBuilder(cctx.visitAnnotation(desc, visible), this);
	}
	
	public ClassAnnotationBuilder appendTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		return new ClassAnnotationBuilder(cctx.visitTypeAnnotation(typeRef, typePath, desc, visible), this);
	}
	
	public FieldBuilder appendField(int access, String name, String desc, String signature, Object value)
	{
		return new FieldBuilder(this, cctx.visitField(access, name, desc, signature, value));
	}
	
	public MethodBuilder appendMethod(int access, String name, String desc, String signature, String[] exceptions)
	{
		return new MethodBuilder(this, cctx.visitMethod(access, name, desc, signature, exceptions));
	}
	
	public byte[] buildAsBytes()
	{
		return cctx.toByteArray();
	}
	
	public Class<?> buildAsClass()
	{
		if(builded != null)
			return builded;
		return builded = cctx.newClass();
	}
	
	Class<?> builded;
	
	private final ClassContext cctx;
}
