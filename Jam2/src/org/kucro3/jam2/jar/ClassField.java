package org.kucro3.jam2.jar;

public class ClassField {
	ClassField(int access, String name, String desc, String signature, Object value)
	{
		this.access = access;
		this.name = name;
		this.descriptor = desc;
		this.signature = signature;
		this.value = value;
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
	
	public Object getValue()
	{
		return value;
	}
	
	private final int access;
	
	private final String name;
	
	private final String descriptor;
	
	private final String signature;
	
	private final Object value;
}
