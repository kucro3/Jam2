package org.kucro3.jam2.visitor;

import org.objectweb.asm.Attribute;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.TypePath;

public interface FieldVisitorListener {
	public default boolean onVisitAnnotation(FieldVisitor fv, String descriptor, boolean visible)
	{return true;}
	
	public default boolean onVisitAttribute(FieldVisitor fv, Attribute attribute)
	{return true;}
	
	public default boolean onVisitEnd(FieldVisitor fv)
	{return true;}
	
	public default boolean onVisitTypeAnnotation(FieldVisitor fv, int typeRef, TypePath typePath, String desc, boolean visible)
	{return true;}
}
