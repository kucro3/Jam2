package org.kucro3.jam2.invoke;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.kucro3.jam2.invoke.ConstructorInvokerLambdaImpl.LambdaInvocation;
import org.kucro3.jam2.util.*;
import org.objectweb.asm.Opcodes;

public abstract class ConstructorInvoker implements Opcodes {
	protected ConstructorInvoker(Class<?> declaringClass, int modifier, Class<?>[] arguments)
	{
		this.declaringClass = declaringClass;
		this.modifier = modifier;
		this.arguments = arguments;
		this.descriptor = toDescriptor(arguments);
	}
	
	public static ConstructorInvoker newInvoker(Constructor<?> constructor)
	{
		if(!Modifier.isPublic(constructor.getModifiers()))
			throw new IllegalArgumentException("constructor unaccessable");
		
		ClassContext ctx = new ClassContext(V1_8, ACC_PUBLIC, 
				"org/kucro3/jam2/invoke/ConstructorInvoker$" + Jam2Util.generateUUIDForClassName(),
				null, "java/lang/Object", new String[] {"org/kucro3/jam2/invoke/ConstructorInvokerLambdaImpl$LambdaInvocation"});
		
		Jam2Util.pushEmptyConstructor(ctx, ACC_PUBLIC, Object.class);
		Jam2Util.pushNewInstance(ctx, ACC_PUBLIC | ACC_VARARGS, "newInstance", ConstructorContext.newContext(constructor), true, true);
		
		LambdaInvocation invocation;
		try {
			invocation = (LambdaInvocation) ctx.newClass().newInstance();
		} catch (Exception e) {
			// unused
			throw new IllegalStateException(e);
		}
		
		return new ConstructorInvokerLambdaImpl(constructor.getDeclaringClass(), constructor.getModifiers(),
				constructor.getParameterTypes(), invocation);
	}
	
	static String toDescriptor(Class<?>[] arguments)
	{
		return MethodInvoker.toDescriptor("<init>", void.class, arguments);
	}
	
	public String getDescriptor()
	{
		return descriptor;
	}
	
	public int getModifier()
	{
		return modifier;
	}
	
	public Class<?>[] getArguments()
	{
		return arguments;
	}
	
	public abstract Object newInstance(Object... args);
	
	final String descriptor;
	
	final Class<?> declaringClass;
	
	final int modifier;
	
	final Class<?>[] arguments;
}
