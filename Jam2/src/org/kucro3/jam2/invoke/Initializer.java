package org.kucro3.jam2.invoke;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public abstract class Initializer {
	protected Initializer()
	{
	}
	
	public abstract void initializeFields(Field[] fields);
	
	public abstract void initializeMethods(Method[] methods);
	
	@SuppressWarnings("rawtypes")
	public abstract void initializeConstructors(Constructor[] constructor);
	
	/**
	 * public static InvocationProvider getInstance();
	 * 
	 * private static final InvocationProvider instance;
	 * 
	 * * These elements will be added when generating the invoker class.
	 */
}
