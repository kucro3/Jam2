package org.kucro3.jam2.util;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import static org.kucro3.jam2.util.Jam2Util.*;

public class MethodContextImpl extends MethodContext implements AccessableContext
{
	protected MethodContextImpl(Class<?> declaringClass, int modifier, String methodName, Class<?> returnType, Class<?>[] arguments, Class<?>[] throwings)
	{
		this(declaringClass, Type.getInternalName(declaringClass), modifier, methodName,
				returnType, Type.getDescriptor(returnType),
				arguments, _toDescriptors(arguments),
				throwings, _toInternalNames(throwings));
	}
	
	protected MethodContextImpl(String declaringClass, int modifier, String methodName, Class<?> returnType, Class<?>[] arguments, Class<?>[] throwings)
	{
		this(null, declaringClass, modifier, methodName,
				returnType, Type.getDescriptor(returnType),
				arguments, _toDescriptors(arguments),
				throwings, _toInternalNames(throwings));
	}
	
	protected MethodContextImpl(String declaringClass, int modifier, String methodName, String returnType, String[] arguments, String[] throwings)
	{
		this(null, declaringClass, modifier, methodName,
				null, returnType,
				null, arguments, 
				null, throwings);
	}
	
	MethodContextImpl(MethodVisitor mv, Class<?> declaringClass, String declaringClassInternalName, int modifier, String methodName,
			Class<?> returnType, String returnTypeDescriptor,
			Class<?>[] arguments, String[] argumentDescriptors,
			Class<?>[] exceptionTypes, String[] throwings)
	{
		super(mv);
		this.declaringClass = declaringClass;
		this.classInternalName = declaringClassInternalName;
		this.methodName = methodName;
		this.returnType = returnType;
		this.returnTypeDescriptor = returnTypeDescriptor;
		this.arguments = arguments;
		this.argumentDescriptors = argumentDescriptors;
		this.exceptionTypes = exceptionTypes;
		this.exceptions = throwings;
		this.modifier = modifier;
		this.descriptor = Jam2Util.toDescriptor(methodName, returnTypeDescriptor, argumentDescriptors);
	}
	
	MethodContextImpl(Class<?> declaringClass, String declaringClassInternalName, int modifier, String methodName,
			Class<?> returnType, String returnTypeDescriptor,
			Class<?>[] arguments, String[] argumentDescriptors,
			Class<?>[] exceptionTypes, String[] throwings)
	{
		this(null, declaringClass, declaringClassInternalName, modifier, methodName, returnType, returnTypeDescriptor,
				arguments, argumentDescriptors, exceptionTypes, throwings);
	}
	
	@Override
	public final MethodContext copyAsMethod()
	{
		return new MethodContextImpl(getDeclaringClass(), getDeclaringClassInternalName(), getModifier(), getMethodName(),
				getReturnType(), getReturnTypeDescriptor(),
				getArguments(), getArgumentDescriptors(),
				getExceptionTypes(), getExceptions());
	}
	
	@Override
	public void visitMaxs(int a, int b)
	{
		super.visitMaxs(a, b);
	}
	
	@Override
	public String getMethodDescriptor()
	{
		return descriptor;
	}
	
	@Override
	public Class<?> getReturnType()
	{
		return returnType;
	}
	
	@Override
	public String getReturnTypeDescriptor()
	{
		return returnTypeDescriptor;
	}
	
	@Override
	public String getMethodName()
	{
		return methodName;
	}
	
	@Override
	public Class<?> getDeclaringClass()
	{
		return declaringClass;
	}
	
	@Override
	public String getDeclaringClassInternalName()
	{
		return classInternalName;
	}
	
	@Override
	public Class<?>[] getArguments()
	{
		return arguments;
	}
	
	@Override
	public String[] getArgumentDescriptors()
	{
		return argumentDescriptors;
	}
	
	@Override
	public String[] getExceptions()
	{
		return exceptions;
	}
	
	@Override
	public Class<?>[] getExceptionTypes()
	{
		return exceptionTypes;
	}
	
	public MethodVisitor getMethodVisitor()
	{
		return super.mv;
	}
	
	@Override
	public MethodVisitor bind(ClassVisitor ref, ClassVisitor cv)
	{
		if(super.mv == null)
			return super.mv = cv.visitMethod(modifier, methodName, 
					_toDescriptor(returnTypeDescriptor, argumentDescriptors, ""), null, exceptions);
		throw new IllegalStateException("Already binded");
	}
	
	@Override
	public MethodVisitor ensureBinded(ClassVisitor ref, ClassVisitor cv)
	{
		if(super.mv == null)
			return bind(ref, cv);
		return super.mv;
	}
	
	@Override
	public int getModifier()
	{
		return modifier;
	}
	
	private final int modifier;
	
	private final String[] exceptions;
	
	private final Class<?>[] exceptionTypes;
	
	private final String classInternalName;
	
	private final Class<?> declaringClass;
	
	private final String[] argumentDescriptors;
	
	private final Class<?>[] arguments;
	
	private final String methodName;
	
	private final Class<?> returnType;
	
	private final String returnTypeDescriptor;
	
	private final String descriptor;
}
