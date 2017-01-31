package org.kucro3.jam2.visitor.cache;

import org.kucro3.jam2.util.Version;
import org.kucro3.jam2.util.annotation.Annotation;
import org.kucro3.jam2.util.annotation.AnnotationContainer;
import org.kucro3.jam2.util.annotation.TypeAnnotation;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.TypePath;

public class FieldCacheVisitor extends FieldVisitor implements CacheVisitor, FieldRevisitable {
	public FieldCacheVisitor()
	{
		super(Version.getASMVersion());
	}
	
	public FieldCacheVisitor(FieldVisitor fv)
	{
		super(Version.getASMVersion(), fv);
	}
	
	@Override
	public void clear()
	{
		ac.clear();
		endVisited = false;
	}
	
	@Override
	public void revisitAttribute(FieldVisitor fv)
	{
		ac.revisit(fv);
	}
	
	@Override
	public void revisitAnnotations(FieldVisitor fv)
	{
		annos.visit(fv);
	}
	
	@Override
	public void revisitEnd(FieldVisitor fv)
	{
		if(endVisited)
			fv.visitEnd();
	}
	
	@Override
	public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		AnnotationVisitor av = super.visitTypeAnnotation(typeRef, typePath, desc, visible);
		TypeAnnotation anno = new TypeAnnotation(typeRef, typePath, desc, visible);
		AnnotationCacheVisitor acv = new AnnotationCacheVisitor(av, anno);
		annos.putAnnotation(anno);
		return acv;
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible)
	{
		AnnotationVisitor av = super.visitAnnotation(desc, visible);
		Annotation anno = new Annotation(desc, visible);
		AnnotationCacheVisitor acv = new AnnotationCacheVisitor(av, anno);
		annos.putAnnotation(anno);
		return acv;
	}
	
	@Override
	public void visitAttribute(Attribute attr)
	{
		super.visitAttribute(attr);
		ac.visitAttribute(attr);
	}
	
	@Override
	public void visitEnd()
	{
		super.visitEnd();
		endVisited = true;
	}
	
	public AnnotationContainer getAnnotationContainer()
	{
		return annos;
	}
	
	protected final AnnotationContainer annos = new AnnotationContainer();
	
	protected boolean endVisited;
	
	protected final AttributeCache ac = new AttributeCache();
}