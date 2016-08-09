package org.kucro3.jam2.invoke;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.kucro3.jam2.invoke.FieldInvokerLambdaImpl.LambdaGet;
import org.kucro3.jam2.invoke.FieldInvokerLambdaImpl.LambdaSet;

@SuppressWarnings("rawtypes")
public abstract class Initializer {
	protected Initializer()
	{
	}
	
	public abstract void initializeFields(Invoker invoker, Field[] fields);
	
	public abstract void initializeMethods(Invoker invoker, Method[] methods);
	
	public abstract void initializeConstructors(Invoker invoker, Constructor[] constructor);
	
	protected static void createInvoker(Invoker invoker, Field field, LambdaGet getter, LambdaSet setter)
	{
		invoker.putField(Invoker.createInvoker(field, getter, setter));
	}
	
	protected static void createInvoker(Invoker invoker, Method method, 
			org.kucro3.jam2.invoke.MethodInvokerLambdaImpl.LambdaInvocation invocation)
	{
		invoker.putMethod(Invoker.createInvoker(method, invocation));
	}
	
	protected static void createInvoker(Invoker invoker, Constructor constructor,
			org.kucro3.jam2.invoke.ConstructorInvokerLambdaImpl.LambdaInvocation invocation)
	{
		invoker.putConstructor(Invoker.createInvoker(constructor, invocation));
	}
	
	/**
	 * public static Initializer getInstance();
	 * 
	 * private static final Initializer instance;
	 * 
	 * * These elements will be added when generating the invoker class.
	 */
}