package org.kucro3.jam2.util;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import static org.kucro3.jam2.util.Jam2Util.*;

public class MethodContext extends MethodVisitor implements AccessableContext
{
	public static MethodContext newContext(Method method)
	{
		return newContext(method.getDeclaringClass(), method.getModifiers(), method.getName(), method.getReturnType(), method.getParameterTypes());
	}
	
	public static MethodContext newContext(Class<?> declaringClass, int modifier, String methodName, Class<?> returnType, Class<?>[] arguments,
			Class<?>... throwings)
	{
		return new MethodContext(declaringClass, modifier, methodName, returnType, arguments, throwings);
	}
	
	public static MethodContext newContext(String declaringClass, int modifier, String methodName, Class<?> returnType, Class<?>[] arguments,
			Class<?>... throwings)
	{
		return new MethodContext(declaringClass, modifier, methodName, returnType, arguments, throwings);
	}
	
	public static MethodContext newContext(String declaringClass, int modifier, String methodName, String returnType, String[] arguments,
			String[] throwings)
	{
		return new MethodContext(declaringClass, modifier, methodName, returnType, arguments, throwings);
	}
	
	static MethodContext newContext(String declaringClass, int modifier, String name, String desc, String[] exceptions)
	{
		ArrayList<String> list = new ArrayList<>();
		String temp;
		int[] index = new int[] {0};
		while((temp = _nextDescriptor(desc, index)) != null)
			list.add(temp);
		String returnType = list.get(list.size() - 1);
		String[] arguments = new String[list.size() - 1];
		for(int i = 0; i < list.size() - 1; i++)
			arguments[i] = list.get(i);
		return new MethodContext(null, declaringClass, modifier, name,
				null, returnType,
				null, arguments,
				null, exceptions);
	}
	
	MethodContext(Class<?> declaringClass, int modifier, String methodName, Class<?> returnType, Class<?>[] arguments, Class<?>[] throwings)
	{
		this(declaringClass, Type.getInternalName(declaringClass), modifier, methodName,
				returnType, Type.getDescriptor(returnType),
				arguments, _toDescriptors(arguments),
				throwings, _toInternalNames(throwings));
	}
	
	MethodContext(String declaringClass, int modifier, String methodName, Class<?> returnType, Class<?>[] arguments, Class<?>[] throwings)
	{
		this(null, declaringClass, modifier, methodName,
				returnType, Type.getDescriptor(returnType),
				arguments, _toDescriptors(arguments),
				throwings, _toInternalNames(throwings));
	}
	
	MethodContext(String declaringClass, int modifier, String methodName, String returnType, String[] arguments, String[] throwings)
	{
		this(null, declaringClass, modifier, methodName,
				null, returnType,
				null, arguments, 
				null, throwings);
	}
	
	protected MethodContext(Class<?> declaringClass, String declaringClassInternalName, int modifier, String methodName,
			Class<?> returnType, String returnTypeDescriptor,
			Class<?>[] arguments, String[] argumentDescriptors,
			Class<?>[] exceptionTypes, String[] throwings)
	{
		super(ClassContext.API);
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
	}
	
	public final MethodContext copyAsMethod()
	{
		return new MethodContext(getDeclaringClass(), getDeclaringClassInternalName(), getModifier(), getMethodName(),
				getReturnType(), getReturnTypeDescriptor(),
				getArguments(), getArgumentDescriptors(),
				getExceptionTypes(), getExceptions());
	}
	
	public Class<?> getReturnType()
	{
		return returnType;
	}
	
	public String getReturnTypeDescriptor()
	{
		return returnTypeDescriptor;
	}
	
	public String getMethodName()
	{
		return methodName;
	}
	
	public Class<?> getDeclaringClass()
	{
		return declaringClass;
	}
	
	public String getDeclaringClassInternalName()
	{
		return classInternalName;
	}
	
	public Class<?>[] getArguments()
	{
		return arguments;
	}
	
	public String[] getArgumentDescriptors()
	{
		return argumentDescriptors;
	}
	
	public String[] getExceptions()
	{
		return exceptions;
	}
	
	public Class<?>[] getExceptionTypes()
	{
		return exceptionTypes;
	}
	
	public MethodVisitor getMethodVisitor()
	{
		return super.mv;
	}
	
	public MethodVisitor bind(ClassVisitor ref, ClassVisitor cv)
	{
		if(super.mv == null)
			return super.mv = cv.visitMethod(modifier, methodName, 
					_toDescriptor(returnTypeDescriptor, argumentDescriptors, ""), null, exceptions);
		throw new IllegalStateException("Already binded");
	}
	
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
}
