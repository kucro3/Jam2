package org.kucro3.jam2.util;

public class LazyDescriptorAnalyzer {
	public LazyDescriptorAnalyzer()
	{
	}
	
	public LazyDescriptorAnalyzer(String descriptor)
	{
		setDescriptor(descriptor);
	}
	
	public void setDescriptor(String descriptor)
	{
		this.current = descriptor;
		this.iter = new MethodDescriptorIterator(current);
	}
	
	public String getDescriptor()
	{
		return current;
	}
	
	public String getReturnType()
	{
		return iter.getReturnType();
	}
	
	public String[] getArguments()
	{
		if(!iter.isCompleted())
			iter.complete();
		return iter.getArguments();
	}
	
	private String current;
	
	private MethodDescriptorIterator iter;
}
