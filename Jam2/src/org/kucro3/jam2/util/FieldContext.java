package org.kucro3.jam2.util;

import java.lang.reflect.Field;

import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Type;

public class FieldContext extends AccessableContext
{
	public static FieldContext newContext(Field field)
	{
		return new FieldContext(field.getDeclaringClass(), field.getName(), field.getType());
	}
	
	public static FieldContext newContext(Class<?> declaringClass, String fieldName, Class<?> type)
	{
		return new FieldContext(declaringClass, fieldName, type);
	}
	
	public static FieldContext newContext(String declaringClass, String fieldName, String type)
	{
		return new FieldContext(declaringClass, fieldName, type);
	}
	
	FieldContext(Class<?> declaringClass, String fieldName, Class<?> type)
	{
		this(declaringClass, Type.getInternalName(declaringClass), fieldName,
				type, Type.getDescriptor(type));
	}
	
	FieldContext(String declaringClass, String fieldName, String type)
	{
		this(null, declaringClass, fieldName,
				null, type);
	}
	
	protected FieldContext(Class<?> declaringClass, String declaringClassInternalName, String fieldName,
			Class<?> type, String descriptor)
	{
		this.declaringClass = declaringClass;
		this.classInternalName = declaringClassInternalName;
		this.type = type;
		this.descriptor = descriptor;
		this.fieldName = fieldName;
	}
	
	public final FieldContext copyAsField()
	{
		return new FieldContext(getDeclaringClass(), getDeclaringClassInternalName(), getFieldName(),
				getType(), getDescriptor());
	}
	
	public String getFieldName()
	{
		return fieldName;
	}
	
	public Class<?> getDeclaringClass()
	{
		return declaringClass;
	}
	
	public String getDeclaringClassInternalName()
	{
		return classInternalName;
	}
	
	public Class<?> getType()
	{
		return type;
	}
	
	public String getDescriptor()
	{
		return descriptor;
	}
	
	public FieldVisitor getFieldVisitor()
	{
		return fv;
	}
	
	FieldVisitor fv;
	
	private final Class<?> declaringClass;
	
	private final String classInternalName;
	
	private final Class<?> type;
	
	private final String descriptor;
	
	private final String fieldName;
}
