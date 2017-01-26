package org.kucro3.jam2.visitor.cache;

import org.objectweb.asm.MethodVisitor;

public interface MethodRevisitable {
	default void revisitEnd(MethodVisitor mv) {}
	
	default void revisitInstructions(MethodVisitor mv) {}
	
	default void revisitMaxs(MethodVisitor mv) {}
	
	default void revisitTryCatchBlocks(MethodVisitor mv) {}
	
	default void revisitAnnotationDefault(MethodVisitor mv) {}
	
	default void revisitAnnotations(MethodVisitor mv) {}
	
	default void revisitAttributes(MethodVisitor mv) {}
	
	default void revisitLineNumbers(MethodVisitor mv) {}
	
	default void revisitLocalAnnotations(MethodVisitor mv) {}
	
	default void revisitLocals(MethodVisitor mv) {}
	
	default void revisitParameters(MethodVisitor mv) {}
}
