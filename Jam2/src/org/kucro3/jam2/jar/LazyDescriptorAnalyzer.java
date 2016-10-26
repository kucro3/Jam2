package org.kucro3.jam2.jar;

import org.kucro3.jam2.util.MethodDescriptorIterator;

class LazyDescriptorAnalyzer {
	LazyDescriptorAnalyzer()
	{
	}
	
	LazyDescriptorAnalyzer(String descriptor)
	{
		setDescriptor(descriptor);
	}
	
	void setDescriptor(String descriptor)
	{
		this.current = descriptor;
		this.iter = new MethodDescriptorIterator(current);
	}
	
	String getDescriptor()
	{
		return current;
	}
	
	String getReturnType()
	{
		return iter.getReturnType();
	}
	
	String[] getArguments()
	{
		if(!iter.isCompleted())
			iter.complete();
		return iter.getArguments();
	}
	
	private String current;
	
	private MethodDescriptorIterator iter;
}
