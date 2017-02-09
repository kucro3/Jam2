package org.kucro3.jam2.util.builder;

import org.kucro3.jam2.asm.ASMCodeBuilderRoot;
import org.kucro3.jam2.util.builder.AnnotationBuilder.MethodAnnotationBuilder;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.TypePath;

public class MethodBuilder {
	MethodBuilder(ClassBuilder owner, MethodVisitor mv)
	{
		this.owner = owner;
		this.mv = mv;
	}
	
	public MethodCodeBuilder code()
	{
		if(mcb == null)
		{
			mcb = new MethodCodeBuilder();
			mv.visitCode();
		}
		
		return mcb;
	}
	
	public MethodBuilder end()
	{
		mv.visitEnd();
		return this;
	}
	
	public ClassBuilder finish()
	{
		return owner;
	}
	
	public MethodAnnotationBuilder appendAnnotation(String descriptor, boolean visible)
	{
		return new MethodAnnotationBuilder(mv.visitAnnotation(descriptor, visible), this);
	}
	
	public MethodAnnotationBuilder appendAnnotationDefault()
	{
		return new MethodAnnotationBuilder(mv.visitAnnotationDefault(), this);
	}
	
	public MethodAnnotationBuilder appendInsnAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		return new MethodAnnotationBuilder(mv.visitInsnAnnotation(typeRef, typePath, desc, visible), this);
	}
	
	public MethodBuilder appendParameter(String desc, int access)
	{
		mv.visitParameter(desc, access);
		return this;
	}
	
	public MethodAnnotationBuilder appendParameterAnnotation(int parameter, String desc, boolean visible)
	{
		return new MethodAnnotationBuilder(mv.visitParameterAnnotation(parameter, desc, visible), this);
	}
	
	public MethodAnnotationBuilder appendTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		return new MethodAnnotationBuilder(mv.visitTypeAnnotation(typeRef, typePath, desc, visible), this);
	}
	
	private MethodCodeBuilder mcb;
	
	private final ClassBuilder owner;
	
	private final MethodVisitor mv;
	
	public class MethodCodeBuilder extends ASMCodeBuilderRoot<MethodCodeBuilder>
	{
		MethodCodeBuilder() 
		{
			super(MethodBuilder.this.mv);
		}
		
		public MethodBuilder escape()
		{
			return MethodBuilder.this;
		}
	}
}
