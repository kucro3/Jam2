package org.kucro3.jam2.visitor;

import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.TypePath;

public interface ClassVisitorListener {
	public default boolean onVisit(ClassVisitor cv, int version, int access, String name, String signature, String superName, String[] interfaces)
	{return true;}
	
	public default boolean onVisitAnnotation(ClassVisitor cv, String desc, boolean flag)
	{return true;}
	
	public default boolean onVisitAttribute(ClassVisitor cv, Attribute attribute)
	{return true;}
	
	public default boolean onVisitEnd(ClassVisitor cv)
	{return true;}
	
	public default boolean onVisitField(ClassVisitor cv, int access, String name, String descriptor, String signature, Object value)
	{return true;}
	
	public default boolean onVisitInnerClass(ClassVisitor cv, String name, String outerName, String innerName, int access)
	{return true;}
	
	public default boolean onVisitMethod(ClassVisitor cv, int access, String name, String descriptor, String signature, String[] exceptions)
	{return true;}
	
	public default boolean onVisitOuterClass(ClassVisitor cv, String owner, String name, String descriptor)
	{return true;}
	
	public default boolean onVisitSource(ClassVisitor cv, String source, String debug)
	{return true;}
	
	public default boolean visitTypeAnnotation(ClassVisitor cv, int typeRef, TypePath typePath, String desc, boolean visible)
	{return true;}
}
