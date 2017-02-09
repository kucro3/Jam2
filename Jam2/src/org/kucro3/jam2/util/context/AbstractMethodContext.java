package org.kucro3.jam2.util.context;

import org.kucro3.jam2.util.LazyDescriptorAnalyzer;
import org.kucro3.jam2.util.MethodContext;
import org.kucro3.util.anno.OverridingRequired;

public abstract class AbstractMethodContext extends AbstractAccessableContext implements MethodContext {
	@Override
	@OverridingRequired
	public abstract String getDeclaringClass();
	
	@Override
	public String[] getExceptions()
	{
		return exceptions;
	}
	
	@Override
	public String[] getArguments()
	{
		String desc;
		if((desc = getDescriptor()) == null)
			return null;
		return new LazyDescriptorAnalyzer(desc).getArguments();
	}
	
	@Override
	public String getReturnType()
	{
		String desc;
		if((desc = getDescriptor()) == null)
			return null;
		return new LazyDescriptorAnalyzer(desc).getReturnType();
	}
	
	protected String[] exceptions;
}
