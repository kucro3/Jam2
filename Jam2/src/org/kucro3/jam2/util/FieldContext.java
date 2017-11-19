package org.kucro3.jam2.util;

import org.objectweb.asm.FieldVisitor;

import java.lang.reflect.Field;
import java.util.Optional;

public interface FieldContext extends AccessableContext {
	Object getValue();

	default Optional<Class<?>> tryGetTypeClass()
	{
		return Jam2Util.tryFromInternalNameToClass(getDescriptor());
	}
	
	String getDeclaringType();

	default Optional<Class<?>> tryGetDeclaringClass()
	{
		return Jam2Util.tryFromInternalNameToClass(getDeclaringType());
	}
	
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

	interface Reflectable extends FieldContext
	{
		Field getField();
	}
}
