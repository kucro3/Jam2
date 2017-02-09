package org.kucro3.jam2.util.context;

import org.kucro3.jam2.util.ClassContext;

public abstract class AbstractClassContext implements ClassContext {
	@Override
	public String getSource()
	{
		return source;
	}
	
	@Override
	public String getDebug()
	{
		return debug;
	}
	
	@Override
	public int getVersion()
	{
		return version;
	}
	
	@Override
	public String getSuperClass()
	{
		return superName;
	}
	
	@Override
	public String[] getInterfaces()
	{
		return interfaces;
	}
	
	@Override
	public String getEnclosingClass()
	{
		return enclosingClass;
	}
	
	@Override
	public String getEnclosingMethodName()
	{
		return enclosingMethodName;
	}
	
	@Override
	public String getEnclosingMethodDescriptor()
	{
		return enclosingMethodDescriptor;
	}
	
	@Override
	public int getModifier()
	{
		return modifier;
	}
	
	@Override
	public String getSignature()
	{
		return signature;
	}
	
	protected int modifier;
	
	protected String source;
	
	protected String debug;
	
	protected int version;
	
	protected String signature;
	
	protected String superName;
	
	protected String[] interfaces;
	
	protected String enclosingClass;
	
	protected String enclosingMethodName;
	
	protected String enclosingMethodDescriptor;
}