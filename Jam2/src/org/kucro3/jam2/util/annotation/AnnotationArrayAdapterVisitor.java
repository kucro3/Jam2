package org.kucro3.jam2.util.annotation;

import org.kucro3.jam2.util.Version;
import org.kucro3.jam2.util.annotation.Annotation.Value;
import org.kucro3.jam2.util.annotation.Annotation.Value.EnumValue;
import org.objectweb.asm.AnnotationVisitor;

public class AnnotationArrayAdapterVisitor extends AnnotationVisitor {
	public AnnotationArrayAdapterVisitor(AnnotationVisitor av, Value value) 
	{
		super(Version.getASMVersion(), av);
		if(!value.isArray())
			throw new IllegalArgumentException("Not a container of array (see isArray())");
		this.value = value;
	}
	
	public AnnotationArrayAdapterVisitor(Value value)
	{
		this(null, value);
	}
	
	@Override
	public void visit(String name, Object value)
	{
		super.visit(name, value);
		this.value.addValue(value);
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String name, String desc)
	{
		AnnotationVisitor av = super.visitAnnotation(name, desc);
		Annotation element = new Annotation(desc);
		this.value.addValue(element);
		return new AnnotationAdapterVisitor(av, element);
	}
	
	@Override
	public void visitEnum(String name, String desc, String value)
	{
		super.visitEnum(name, desc, value);
		this.value.addValue(new EnumValue(desc, value));
	}
	
	public Value getContainer()
	{
		return value;
	}

	protected final Value value;
}
