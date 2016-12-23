package org.kucro3.jam2.visitor;

import java.util.LinkedList;

import org.kucro3.jam2.util.Version;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.TypePath;

public class FieldCacheVisitor extends FieldVisitor implements CacheVisitor<FieldVisitor> {
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
	public void revisit(FieldVisitor t) 
	{
		ac.revisit(t);
		for(Action acv : annos)
			acv.revisit(t);
		
		if(endVisited)
			t.visitEnd();
	}
	
	@Override
	public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		AnnotationVisitor av = super.visitTypeAnnotation(typeRef, typePath, desc, visible);
		AnnotationCacheVisitor acv = new AnnotationCacheVisitor(av);
		annos.add(new ActionVisitTypeAnnotation(typeRef, typePath, desc, visible, acv));
		return acv;
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible)
	{
		AnnotationVisitor av = super.visitAnnotation(desc, visible);
		AnnotationCacheVisitor acv = new AnnotationCacheVisitor(av);
		annos.add(new ActionVisitAnnotation(desc, visible, acv));
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
	
	final LinkedList<Action> annos = new LinkedList<>();
	
	boolean endVisited;
	
	private final AttributeCache ac = new AttributeCache();
}