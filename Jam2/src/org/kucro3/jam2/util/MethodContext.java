package org.kucro3.jam2.util;

import static org.kucro3.jam2.util.Jam2Util._nextDescriptor;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.objectweb.asm.*;

public abstract class MethodContext extends MethodVisitor implements AccessableContext {
	protected MethodContext()
	{
		super(ClassContext.API);
	}
	
	protected MethodContext(MethodVisitor mv)
	{
		super(ClassContext.API, mv);
	}
	
	public abstract MethodVisitor bind(ClassVisitor ref, ClassVisitor cv);
	
	public abstract MethodContext copyAsMethod();
	
	public abstract MethodVisitor ensureBinded(ClassVisitor ref, ClassVisitor cv);
	
	public abstract String[] getArgumentDescriptors();
	
	public abstract Class<?>[] getArguments();
	
	public abstract Class<?> getDeclaringClass();
	
	public abstract String getDeclaringClassInternalName();
	
	public abstract String[] getExceptions();
	
	public abstract Class<?>[] getExceptionTypes();
	
	public abstract String getMethodName();
	
	public abstract int getModifier();
	
	public abstract Class<?> getReturnType();
	
	public abstract String getReturnTypeDescriptor();
	
	public static MethodContext newContext(Method method)
	{
		return newContext(method.getDeclaringClass(), method.getModifiers(), method.getName(), method.getReturnType(), method.getParameterTypes());
	}
	
	public static MethodContext newContext(Method method, int modifier)
	{
		return newContext(method.getDeclaringClass(), modifier, method.getName(), method.getReturnType(), method.getParameterTypes());
	}
	
	public static MethodContext newContext(Class<?> declaringClass, int modifier, String methodName, Class<?> returnType, Class<?>[] arguments,
			Class<?>... throwings)
	{
		return new MethodContextImpl(declaringClass, modifier, methodName, returnType, arguments, throwings);
	}
	
	public static MethodContext newContext(String declaringClass, int modifier, String methodName, Class<?> returnType, Class<?>[] arguments,
			Class<?>... throwings)
	{
		return new MethodContextImpl(declaringClass, modifier, methodName, returnType, arguments, throwings);
	}
	
	public static MethodContext newContext(String declaringClass, int modifier, String methodName, String returnType, String[] arguments,
			String[] throwings)
	{
		return new MethodContextImpl(declaringClass, modifier, methodName, returnType, arguments, throwings);
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
		return new MethodContextImpl(null, declaringClass, modifier, name,
				null, returnType,
				null, arguments,
				null, exceptions);
	}
}
