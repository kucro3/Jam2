package org.kucro3.jam2.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MethodDescriptorIterator implements Iterator<String>, Iterable<String> {	
	public MethodDescriptorIterator(String descriptor)
	{
		this.descriptor = descriptor;
		this.returnType = toReturnType();
		this.argumentsCount = -1;
		this.tempList = new ArrayList<>();
	}
	
	public MethodDescriptorIterator(String returnType, String[] arguments)
	{
		this.returnType = returnType;
		this.arguments = arguments;
		this.argumentsCount = arguments.length;
		this.descriptor = Jam2Util._toDescriptor(returnType, arguments);
		this.completed = true;
	}
	
	@Override
	public boolean hasNext()
	{
		if(hasNextCalled)
			return hasNext;
		
		boolean result;
		if(completed)
			result = indexJ < argumentsCount;
		else if(tempNext != null)
			result = true;
		else
			result = computeNext() != null;
		
		hasNextCalled = true;
		return hasNext = result;
	}

	@Override
	public String next()
	{
		String next;
		
		if(!hasNext())
			return null;
		else
			if(completed)
				next = arguments[indexJ++];
			else
			{
				next = tempNext;
				computeNext();
				this.indexJ++;
			}
		
		hasNextCalled = false;
		return next;
	}
	
	public void complete()
	{
		if(completed)
			return;
		
		while(computeNext() != null);
		completed();
	}
	
	private String computeNext()
	{
		if(completed)
			return null;
		
		String next = nextDescriptor();
		if(next == null)
		{
			completed();
			this.tempNext = null;
			return null;
		}
		
		this.tempNext = next;
		this.tempList.add(next);
		
		return next;
	}
	
	private void completed()
	{
		this.completed = true;
		if(this.tempList != null)
		{
			this.arguments = tempList.toArray(new String[0]);
			this.argumentsCount = arguments.length;
			this.tempList.clear();
			this.tempList = null;
		}
	}
	
	private String toReturnType()
	{
		return Jam2Util._getReturnDescriptor(descriptor);
	}
	
	private String nextDescriptor()
	{
		char c;
		int start, end;
		PARSING_BLOCK: {
			LOOP_BLOCK: while(true)
				if(indexI < descriptor.length())
					switch(c = descriptor.charAt(start = indexI++))
					{
					case ')':
						break PARSING_BLOCK;
					case '(':
						continue;
					case '[':
						while(indexI < descriptor.length())
							switch(descriptor.charAt(indexI++))
							{
							case '[':
								continue;
							case 'L':
								break LOOP_BLOCK;
							default:
								return descriptor.substring(start, indexI);
							}
						throw new IllegalArgumentException("Ilegal descriptor");
					case 'L':
						break LOOP_BLOCK;
					default:
						return String.valueOf(c);
					}
				else break PARSING_BLOCK;
		
			end = descriptor.indexOf(';', start);
			if(end < 0)
				throw new IllegalArgumentException("Ilegal descriptor");
			indexI = end + 1;
			return descriptor.substring(start, indexI);
		}
		return null;
	}
	
	public boolean isCompleted()
	{
		return completed;
	}
	
	public String getReturnType()
	{
		return returnType;
	}
	
	public String[] getArguments()
	{
		return arguments;
	}
	
	public int getArgumentsCount()
	{
		return argumentsCount;
	}
	
	public String getDescriptor()
	{
		return descriptor;
	}
	
	@Override
	public Iterator<String> iterator() 
	{
		return this;
	}
	
	private String tempNext;
	
	private List<String> tempList;
	
	private boolean completed;
	
	private int indexJ = 0;
	
	private int indexI = 0;
	
	private String[] arguments;
	
	private int argumentsCount;
	
	private String returnType;
	
	private final String descriptor;
	
	private boolean hasNextCalled;
	
	private boolean hasNext;
}
