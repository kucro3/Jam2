package org.kucro3.jam2.util;

import org.objectweb.asm.FieldVisitor;

public interface FieldContext extends AccessableContext {
	Object getValue();
	
	String getDeclaringClass();
	
	default void setValue(Object value) {throw new UnsupportedOperationException();}
	
	interface Modifiable extends FieldContext, AccessableContext.Modifiable
	{
		@Override
		void setValue(Object value);
	}
	
	interface RestrictedModifiable extends Modifiable, AccessableContext.RestrictedModifiable
	{
		@Override
		void setDescriptor(String descriptor);
	}
	
	interface FullyModifiable extends Modifiable, AccessableContext.FullyModifiable
	{
	}
	
	interface Compound extends FieldContext
	{
		FieldContext getContext();
	}
	
	interface Visited extends FieldContext
	{
		FieldVisitor getVisitor();
	}
}
