package org.kucro3.jam2.visitor;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

public interface Action {
	public default void revisit(AnnotationVisitor av) {};
	
	public default void revisit(FieldVisitor fv) {};
	
	public default void revisit(MethodVisitor mv) {};
	
	public default void revisit(ClassVisitor cv) {};
}