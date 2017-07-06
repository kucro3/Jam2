package org.kucro3.jam2.util.annotation;

import org.kucro3.jam2.util.Version;
import org.kucro3.jam2.util.annotation.Annotation.Value;
import org.kucro3.jam2.util.annotation.Annotation.Value.EnumValue;
import org.objectweb.asm.AnnotationVisitor;

public class AnnotationValueAdapterVisitor extends AnnotationVisitor {
	public AnnotationValueAdapterVisitor(AnnotationVisitor av, Value value)
	{
		super(Version.getASMVersion(), av);
		this.value = value;
	}
	
	public AnnotationValueAdapterVisitor(Value value)
	{
		this(null, value);
	}
	
	@Override
	public void visit(String name, Object value)
	{
		this.value.setValue(value);
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String name, String desc)
	{
		AnnotationVisitor av = super.visitAnnotation(name, desc);
		Annotation annotation = new Annotation(desc);
		AnnotationAdapterVisitor aav = new AnnotationAdapterVisitor(av, annotation);
		value.setValue(annotation);
		return aav;
	}
	
	@Override
	public AnnotationVisitor visitArray(String name)
	{
		AnnotationVisitor av = super.visitArray(name);
		value.clear();
		value.initializeAsArray();
		AnnotationArrayAdapterVisitor aaav = new AnnotationArrayAdapterVisitor(av, value);
		return aaav;
	}
	
	@Override
	public void visitEnum(String name, String desc, String value)
	{
		this.value.setValue(new EnumValue(desc, value));
	}
	
	public Value getValue()
	{
		return value;
	}
	
	protected final Value value;
}
