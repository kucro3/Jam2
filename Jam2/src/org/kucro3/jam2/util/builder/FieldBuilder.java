package org.kucro3.jam2.util.builder;

import org.kucro3.jam2.util.FieldContext;
import org.kucro3.jam2.util.builder.AnnotationBuilder.FieldAnnotationBuilder;
import org.objectweb.asm.TypePath;

public class FieldBuilder {
	FieldBuilder(ClassBuilder owner, FieldContext fv)
	{
		this.owner = owner;
		this.fv = fv;
	}
	
	public ClassBuilder finish()
	{
		fv.visitEnd();
		return owner;
	}
	
	public FieldAnnotationBuilder appendAnnotation(String desc, boolean visible)
	{
		return new FieldAnnotationBuilder(fv.visitAnnotation(desc, visible), this);
	}
	
	public FieldAnnotationBuilder appendTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		return new FieldAnnotationBuilder(fv.visitTypeAnnotation(typeRef, typePath, desc, visible), this);
	}
	
	public FieldContext getContext()
	{
		return fv;
	}
	
	private final ClassBuilder owner;
	
	private final FieldContext fv;
}
