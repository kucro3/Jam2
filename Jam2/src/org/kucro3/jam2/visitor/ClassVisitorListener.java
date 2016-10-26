package org.kucro3.jam2.visitor;

import org.kucro3.util.Reference;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.TypePath;

public interface ClassVisitorListener {
	public default boolean preVisit(ClassVisitor cv, int version, int access, String name, String signature, String superName,
			String[] interfaces)
	{return true;}
	
	public default boolean preVisitAnnotation(ClassVisitor cv, String desc, boolean flag)
	{return true;}
	
	public default boolean preVisitAttribute(ClassVisitor cv, Attribute attribute)
	{return true;}
	
	public default boolean preVisitEnd(ClassVisitor cv)
	{return true;}
	
	public default boolean preVisitField(ClassVisitor cv, int access, String name, String descriptor, String signature, Object value)
	{return true;}
	
	public default boolean preVisitInnerClass(ClassVisitor cv, String name, String outerName, String innerName, int access)
	{return true;}
	
	public default boolean preVisitMethod(ClassVisitor cv, int access, String name, String descriptor, String signature, String[] exceptions)
	{return true;}
	
	public default boolean preVisitOuterClass(ClassVisitor cv, String owner, String name, String descriptor)
	{return true;}
	
	public default boolean preVisitSource(ClassVisitor cv, String source, String debug)
	{return true;}
	
	public default boolean preVisitTypeAnnotation(ClassVisitor cv, int typeRef, TypePath typePath, String desc, boolean visible)
	{return true;}
	
	public default void onVisit(ClassVisitor cv, int version, int access, String name, String signature, String superName,
			String[] interfaces)
	{}
	
	public default void onVisitAnnotation(ClassVisitor cv, String desc, boolean flag, Reference<AnnotationVisitor> ref)
	{}
	
	public default void onVisitAttribute(ClassVisitor cv, Attribute attribute)
	{}
	
	public default void onVisitEnd(ClassVisitor cv)
	{}
	
	public default void onVisitField(ClassVisitor cv, int access, String name, String descriptor, String signature, Object value,
			Reference<FieldVisitor> ref)
	{}
	
	public default void onVisitInnerClass(ClassVisitor cv, String name, String outerName, String innerName, int access)
	{}
	
	public default void onVisitMethod(ClassVisitor cv, int access, String name, String descriptor, String signature, String[] exceptions,
			Reference<MethodVisitor> ref)
	{}
	
	public default void onVisitOuterClass(ClassVisitor cv, String owner, String name, String descriptor)
	{}
	
	public default void onVisitSource(ClassVisitor cv, String source, String debug)
	{}
	
	public default void onVisitTypeAnnotation(ClassVisitor cv, int typeRef, TypePath typePath, String desc, boolean visible,
			Reference<AnnotationVisitor> ref)
	{}
}
