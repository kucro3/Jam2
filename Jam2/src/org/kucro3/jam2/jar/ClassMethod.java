package org.kucro3.jam2.jar;

public class ClassMethod {
	ClassMethod(int access, String name, String descriptor, String signature, String[] exceptions)
	{
		this.access = access;
		this.name = name;
		this.descriptor = descriptor;
		this.signature = signature;
		this.exceptions = exceptions;
	}
	
	public int getAccess()
	{
		return access;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getDescriptor()
	{
		return descriptor;
	}
	
	public String getSignature()
	{
		return signature;
	}
	
	public String[] getExceptions()
	{
		return exceptions;
	}
	
	private final int access;
	
	private final String name;
	
	private final String descriptor;
	
	private final String signature;
	
	private final String[] exceptions;
	
	// initialized by CMV
}
