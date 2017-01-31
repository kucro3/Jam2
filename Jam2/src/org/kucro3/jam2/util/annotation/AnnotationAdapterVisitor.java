package org.kucro3.jam2.util.annotation;

import org.kucro3.jam2.util.Version;
import org.kucro3.jam2.util.annotation.Annotation.Value;
import org.kucro3.jam2.util.annotation.Annotation.Value.EnumValue;
import org.objectweb.asm.AnnotationVisitor;

public class AnnotationAdapterVisitor extends AnnotationVisitor {
	@Deprecated
	public AnnotationAdapterVisitor()
	{
		this(new Annotation());
	}
	
	public AnnotationAdapterVisitor(Annotation annotation)
	{
		super(Version.getASMVersion());
		this.annotation = annotation;
	}
	
	public AnnotationAdapterVisitor(String descriptor)
	{
		this(descriptor, false);
	}
	
	public AnnotationAdapterVisitor(String descriptor, boolean visible)
	{
		this(new Annotation(descriptor, visible));
	}
	
	@Deprecated
	public AnnotationAdapterVisitor(AnnotationVisitor av)
	{
		this(av, new Annotation());
	}
	
	public AnnotationAdapterVisitor(AnnotationVisitor av, Annotation annotation)
	{
		super(Version.getASMVersion(), av);
		this.annotation = annotation;
	}
	
	public AnnotationAdapterVisitor(AnnotationVisitor av, String descriptor)
	{
		this(av, descriptor, false);
	}
	
	public AnnotationAdapterVisitor(AnnotationVisitor av, String descriptor, boolean visible)
	{
		this(av, new Annotation(descriptor, visible));
	}
	
	@Override
	public void visit(String name, Object value)
	{
		super.visit(name, value);
		annotation.putValue(name, new Value(value));
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String name, String desc)
	{
		AnnotationVisitor av = super.visitAnnotation(name, desc);
		Annotation annotation = new Annotation(desc);
		Value value = new Value(annotation);
		this.annotation.putValue(name, value);
		return new AnnotationAdapterVisitor(av, annotation);
	}
	
	@Override
	public AnnotationVisitor visitArray(String name)
	{
		AnnotationVisitor av = super.visitArray(name);
		Value value = new Value();
		annotation.putValue(name, value);
		return new AnnotationArrayAdapterVisitor(av, value);
	}
	
	@Override
	public void visitEnum(String name, String desc, String value)
	{
		super.visitEnum(name, desc, value);
		annotation.putValue(name, new Value(new EnumValue(desc, value)));
	}
	
	public Annotation getAnnotation()
	{
		return annotation;
	}
	
	protected final Annotation annotation;
}
