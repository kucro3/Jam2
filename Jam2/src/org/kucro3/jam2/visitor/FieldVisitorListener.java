package org.kucro3.jam2.visitor;

import org.kucro3.util.Reference;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.TypePath;

public interface FieldVisitorListener {
	public default boolean preVisitAnnotation(FieldVisitor fv, String descriptor, boolean visible)
	{return true;}
	
	public default boolean preVisitAttribute(FieldVisitor fv, Attribute attribute)
	{return true;}
	
	public default boolean preVisitEnd(FieldVisitor fv)
	{return true;}
	
	public default boolean preVisitTypeAnnotation(FieldVisitor fv, int typeRef, TypePath typePath, String desc, boolean visible)
	{return true;}
	
	public default void onVisitAnnotation(FieldVisitor fv, String descriptor, boolean visible,
			Reference<AnnotationVisitor> ref)
	{}
	
	public default void onVisitAttribute(FieldVisitor fv, Attribute attribute)
	{}
	
	public default void onVisitEnd(FieldVisitor fv)
	{}
	
	public default void onVisitTypeAnnotation(FieldVisitor fv, int typeRef, TypePath typePath, String desc, boolean visible,
			Reference<AnnotationVisitor> ref)
	{}
}
