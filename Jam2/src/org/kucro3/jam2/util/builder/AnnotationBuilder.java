package org.kucro3.jam2.util.builder;

import org.objectweb.asm.AnnotationVisitor;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AnnotationBuilder<T extends AnnotationBuilder, R> {
	AnnotationBuilder(AnnotationVisitor av, R owner)
	{
		this.av = av;
		this.owner = owner;
	}
	
	public T append(String name, Object value)
	{
		av.visit(name, value);
		return (T) this;
	}
	
	public AnnotationAnnotationBuilder<AnnotationBuilder<T, R>> appendArray(String name)
	{
		return new AnnotationAnnotationBuilder<>(av.visitArray(name), this);
	}
	
	public AnnotationAnnotationBuilder<AnnotationBuilder<T, R>> appendAnnotation(String name, String descriptor)
	{
		return new AnnotationAnnotationBuilder<>(av.visitAnnotation(name, descriptor), this);
	}
	
	public T appendEnum(String name, String descriptor, String value)
	{
		av.visitEnum(name, descriptor, value);
		return (T) this;
	}
	
	public R finish()
	{
		av.visitEnd();
		return owner;
	}
	
	private final R owner;
	
	private final AnnotationVisitor av;
	
	public static class AnnotationAnnotationBuilder<R> extends AnnotationBuilder<AnnotationAnnotationBuilder, R>
	{
		AnnotationAnnotationBuilder(AnnotationVisitor av, R owner)
		{
			super(av, owner);
		}
	}
	
	public static class MethodAnnotationBuilder extends AnnotationBuilder<MethodAnnotationBuilder, MethodBuilder>
	{
		MethodAnnotationBuilder(AnnotationVisitor av, MethodBuilder owner) 
		{
			super(av, owner);
		}
	}
	
	public static class FieldAnnotationBuilder extends AnnotationBuilder<FieldAnnotationBuilder, FieldBuilder>
	{
		FieldAnnotationBuilder(AnnotationVisitor av, FieldBuilder owner) 
		{
			super(av, owner);
		}
	}
	
	public static class ClassAnnotationBuilder extends AnnotationBuilder<ClassAnnotationBuilder, ClassBuilder>
	{
		ClassAnnotationBuilder(AnnotationVisitor av, ClassBuilder owner) 
		{
			super(av, owner);
		}
	}
	
	public static class LocalVariableAnnotationBuilder<T> extends AnnotationBuilder<ClassAnnotationBuilder, T>
	{
		public LocalVariableAnnotationBuilder(AnnotationVisitor av, T owner) 
		{
			super(av, owner);
		}
	}
}
