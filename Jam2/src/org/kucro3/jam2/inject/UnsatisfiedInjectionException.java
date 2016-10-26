package org.kucro3.jam2.inject;

import java.lang.reflect.Method;

import org.objectweb.asm.Type;

public class UnsatisfiedInjectionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -238867022756984685L;
	
	public UnsatisfiedInjectionException(String message)
	{
		super(message);
	}
	
	public UnsatisfiedInjectionException(Method injection)
	{
		super("Injection method: " + Type.getMethodDescriptor(injection));
	}
	
	public UnsatisfiedInjectionException(Exception e)
	{
		super(e);
	}
}
