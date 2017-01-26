package org.kucro3.jam2.visitor.cache;

import org.objectweb.asm.ClassVisitor;

public interface ClassRevisitable {
	default void revisitAnnotations(ClassVisitor cv) {}
	
	default void revisitAttributes(ClassVisitor cv) {}
	
	default void revisitEnd(ClassVisitor cv) {}
	
	default void revisitEssentials(ClassVisitor cv) {}
	
	default void revisitFields(ClassVisitor cv) {}
	
	default void revisitInnerClasses(ClassVisitor cv) {}
	
	default void revisitMethods(ClassVisitor cv) {}
	
	default void revisitOuterClass(ClassVisitor cv) {}
	
	default void revisitSource(ClassVisitor cv) {}
}
