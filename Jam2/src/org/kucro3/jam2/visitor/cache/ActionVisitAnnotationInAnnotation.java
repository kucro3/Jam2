package org.kucro3.jam2.visitor.cache;

import org.objectweb.asm.AnnotationVisitor;

class ActionVisitAnnotationInAnnotation implements Action {
	ActionVisitAnnotationInAnnotation(String name, String desc, AnnotationCacheVisitor acv)
	{
		this.acv = acv;
		this.name = name;
		this.desc = desc;
	}
	
	@Override
	public void revisit(AnnotationVisitor av) 
	{
		AnnotationVisitor v = av.visitAnnotation(name, desc);
		if(acv != null)
			acv.revisit(v);
	}
	
	final AnnotationCacheVisitor acv;
	
	final String name;
	
	final String desc;
}
