package org.kucro3.jam2.visitor.cache;

import org.objectweb.asm.FieldVisitor;

public interface FieldRevisitable {
	default void revisitAnnotations(FieldVisitor fv) {}
	
	default void revisitAttribute(FieldVisitor fv) {}
	
	default void revisitEnd(FieldVisitor fv) {}
}
