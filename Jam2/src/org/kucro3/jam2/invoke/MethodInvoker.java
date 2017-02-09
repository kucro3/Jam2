package org.kucro3.jam2.invoke;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.kucro3.jam2.invoke.MethodInvokerLambdaImpl.LambdaInvocation;
import org.kucro3.jam2.util.Jam2Util;
import org.kucro3.jam2.util.Jam2Util.CallingType;
import org.kucro3.jam2.util.Contexts;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

public abstract class MethodInvoker implements Opcodes {
	protected MethodInvoker(Class<?> declaringClass, int modifier, String name, Class<?> returnType, Class<?>[] arguments)
	{
		this.declaringClass = declaringClass;
		this.name = name;
		this.returnType = returnType;
		this.arguments = arguments;
		this.modifier = modifier;
		this.descriptor = Jam2Util.toDescriptor(name, returnType, arguments);
	}
	
	public static MethodInvoker newInvoker(Method method)
	{
		if(!Modifier.isPublic(method.getModifiers()))
			throw new IllegalArgumentException("method unaccessable");
	
		LambdaInvocation invocation;
		
		String name;
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		cw.visit(
				V1_8,
				ACC_PUBLIC,
				name = "org/kucro3/jam2/invoke/MethodInvoker$" + Jam2Util.generateUUIDForClassName(),
				null,
				"java/lang/Object",
				new String[] {"org/kucro3/jam2/invoke/MethodInvokerLambdaImpl$LambdaInvocation"});
		Jam2Util.pushCaller(cw, ACC_PUBLIC, "invoke", Contexts.newMethodConstant(method), CallingType.VIRTUAL, true, true);
		Jam2Util.pushEmptyConstructor(cw, ACC_PUBLIC, Object.class);
		cw.visitEnd();
		
		try {
			invocation = (LambdaInvocation) Jam2Util.newClass(Jam2Util.fromInternalNameToCanonical(name), cw).newInstance();
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
