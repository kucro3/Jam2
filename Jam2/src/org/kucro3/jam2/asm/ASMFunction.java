package org.kucro3.jam2.asm;

public class ASMFunction {
	public ASMFunction()
	{
	}
	
	public Object function(Object... args)
	{
		throw new UnsupportedOperationException("Not initialized");
	}
	
	public final Class<?>[] getArguments()
	{
		return arguments;
	}
	
	public final String[] getArgumentDescriptors()
	{
		return argumentDescriptors;
	}
	
	Class<?>[] arguments;
	
	String[] argumentDescriptors;
}
