package org.kucro3.jam2.invoke;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.kucro3.jam2.invoke.MethodInvokerLambdaImpl.LambdaInvocation;
import org.kucro3.jam2.util.ClassContext;
import org.kucro3.jam2.util.Jam2Util;
import org.kucro3.jam2.util.Jam2Util.CallingType;
import org.kucro3.jam2.util.MethodContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public abstract class MethodInvoker implements Opcodes {
	protected MethodInvoker(Class<?> declaringClass, int modifier, String name, Class<?> returnType, Class<?>[] arguments)
	{
		this.declaringClass = declaringClass;
		this.name = name;
		this.returnType = returnType;
		this.arguments = arguments;
		this.modifier = modifier;
		this.descriptor = toDescriptor(name, returnType, arguments);
	}
	
	static String toDescriptor(String name, Class<?> returnType, Class<?>[] arguments)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		sb.append("(");
		for(int i = 0; i < arguments.length; i++)
			sb.append(Type.getDescriptor(arguments[i]));
		sb.append(")");
		sb.append(Type.getDescriptor(returnType));
		return sb.toString();
	}
	
	public static MethodInvoker newInvoker(Method method)
	{
		if(!Modifier.isPublic(method.getModifiers()))
			throw new IllegalArgumentException("method unaccessable");
	
		LambdaInvocation invocation;
		
		ClassContext ctx = new ClassContext(V1_8, ACC_PUBLIC,
				"org/kucro3/jam2/invoke/MethodInvoker$" + Jam2Util.generateUUIDForClassName(),
				null, "java/lang/Object", new String[] {"org/kucro3/jam2/invoke/MethodInvokerLambdaImpl$LambdaInvocation"});
		Jam2Util.pushCaller(ctx, ACC_PUBLIC, "invoke", MethodContext.newContext(method), CallingType.VIRTUAL, true, true);
		Jam2Util.pushEmptyConstructor(ctx, ACC_PUBLIC, Object.class);
		
		try {
			invocation = (LambdaInvocation) ctx.newClass().newInstance();
		} catch (Exception e) {
			// unused
			throw new IllegalStateException(e);
		}
		
		return new MethodInvokerLambdaImpl(method.getDeclaringClass(), method.getModifiers(),
				method.getName(), method.getReturnType(), method.getParameterTypes(), invocation);
	}
	
	public final Class<?> getDeclaringClass()
	{
		return declaringClass;
	}
	
	public final String getName()
	{
		return name;
	}
	
	public Class<?> getReturnType()
	{
		return returnType;
	}
	
	public Class<?>[] getArguments()
	{
		return arguments;
	}
	
	public int getModifier()
	{
		return modifier;
	}
	
	public abstract Object invoke(Object obj, Object... args);
	
	public String getDescriptor()
	{
		return descriptor;
	}
	
	final String descriptor;
	
	final int modifier;
	
	final Class<?> declaringClass;
	
	final String name;
	
	final Class<?> returnType;
	
	final Class<?>[] arguments;
}
