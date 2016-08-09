package org.kucro3.jam2.util;

import java.lang.reflect.Field;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;

public abstract class FieldContext extends FieldVisitor implements AccessableContext {
	protected FieldContext()
	{
		this(null);
	}
	
	protected FieldContext(FieldVisitor fv)
	{
		super(ClassContext.API, fv);
	}
	
	public abstract FieldVisitor bind(ClassVisitor ref, ClassVisitor cv);
	
	public abstract FieldContext copyAsField();
	
	public abstract FieldVisitor ensureBinded(ClassVisitor ref, ClassVisitor cv);
	
	public abstract Class<?> getDeclaringClass();
	
	public abstract String getDeclaringClassInternalName();
	
	public abstract String getDescriptor();
	
	public abstract String getFieldName();
	
	public abstract Class<?> getType();
	
	public static FieldContext newContext(Field field)
	{
		return new FieldContextImpl(field.getDeclaringClass(), field.getModifiers(), field.getName(), field.getType());
	}
	
	public static FieldContext newContext(Class<?> declaringClass, int modifier, String fieldName, Class<?> type)
	{
		return new FieldContextImpl(declaringClass, modifier, fieldName, type);
	}
	
	public static FieldContext newContext(String declaringClass, int modifier, String fieldName, Class<?> type)
	{
		return new FieldContextImpl(declaringClass, modifier, fieldName, type);
	}
	
	public static FieldContext newContext(String declaringClass, int modifier, String fieldName, String type)
	{
		return new FieldContextImpl(declaringClass, modifier, fieldName, type);
	}
}
