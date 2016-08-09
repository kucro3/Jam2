package org.kucro3.jam2.util;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Type;

public class FieldContextImpl extends FieldContext
{
	protected FieldContextImpl(Class<?> declaringClass, int modifier, String fieldName, Class<?> type)
	{
		this(declaringClass, Type.getInternalName(declaringClass), modifier, fieldName,
				type, Type.getDescriptor(type));
	}
	
	protected FieldContextImpl(String declaringClass, int modifier, String fieldName, Class<?> type)
	{
		this(null, declaringClass, modifier, fieldName,
				type, Type.getDescriptor(type));
	}
	
	protected FieldContextImpl(String declaringClass, int modifier, String fieldName, String type)
	{
		this(null, declaringClass, modifier, fieldName,
				null, type);
	}
	
	FieldContextImpl(Class<?> declaringClass, String declaringClassInternalName, int modifier, String fieldName,
			Class<?> type, String descriptor)
	{
		super();
		this.declaringClass = declaringClass;
		this.classInternalName = declaringClassInternalName;
		this.type = type;
		this.descriptor = descriptor;
		this.fieldName = fieldName;
		this.modifier = modifier;
	}
	
	public final FieldContext copyAsField()
	{
		return new FieldContextImpl(getDeclaringClass(), getDeclaringClassInternalName(), getModifier(), getFieldName(),
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
		return super.fv;
	}
	
	@Override
	public int getModifier()
	{
		return modifier;
	}
	
	public FieldVisitor bind(ClassVisitor ref, ClassVisitor cv)
	{
		if(super.fv == null)
			return super.fv = cv.visitField(modifier, fieldName, descriptor, null, null);
		throw new IllegalStateException("Already binded");
	}
	
	public FieldVisitor ensureBinded(ClassVisitor ref, ClassVisitor cv)
	{
		if(super.fv == null)
			return bind(ref, cv);
		return super.fv;
	}
	
	private final int modifier;
	
	private final Class<?> declaringClass;
	
	private final String classInternalName;
	
	private final Class<?> type;
	
	private final String descriptor;
	
	private final String fieldName;
}
