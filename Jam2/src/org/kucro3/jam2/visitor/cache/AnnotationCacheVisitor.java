package org.kucro3.jam2.visitor.cache;


import org.kucro3.jam2.util.Version;
import org.kucro3.jam2.util.annotation.Annotation;
import org.kucro3.jam2.util.annotation.AnnotationAdapterVisitor;
import org.objectweb.asm.AnnotationVisitor;

// for adapting
public class AnnotationCacheVisitor extends AnnotationVisitor implements CacheVisitor {
	public AnnotationCacheVisitor(Annotation annotation)
	{
		super(Version.getASMVersion());
		this.adapter = new AnnotationAdapterVisitor(annotation);
	}
	
	public AnnotationCacheVisitor(AnnotationVisitor av, Annotation annotation)
	{
		super(Version.getASMVersion(), av);
		this.adapter = new AnnotationAdapterVisitor(av, annotation);
	}
	
	@Override
	public void visit(String name, Object value)
	{
		super.visit(name, value);
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String name, String desc)
	{
		return adapter.visitAnnotation(name, desc);
	}
	
	@Override
	public AnnotationVisitor visitArray(String name)
	{
		return adapter.visitArray(name);
	}
	
	@Override
	public void visitEnd()
	{
		super.visitEnd();
	}
	
	@Override
	public void visitEnum(String name, String desc, String value)
	{
		super.visitEnum(name, desc, value);
	}
	
	@Override
	public void clear() 
	{
		adapter.getAnnotation().clearValues();
	}

	public void revisit(AnnotationVisitor t) 
	{
		adapter.getAnnotation().visitTo(t);
	}
	
	public void revisitOptional(AnnotationVisitor t)
	{
		if(t != null)
			revisit(t);
	}
	
	public Annotation getAnnotation()
	{
		return adapter.getAnnotation();
	}
	
	private final AnnotationAdapterVisitor adapter;
}