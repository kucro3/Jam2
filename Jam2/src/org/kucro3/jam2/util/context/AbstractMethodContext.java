package org.kucro3.jam2.util.context;

import org.kucro3.jam2.util.MethodContext;

public abstract class AbstractMethodContext extends AbstractAccessableContext implements MethodContext {
	@Override
	public abstract String getDeclaringClass();
	
	@Override
	public String[] getExceptions()
	{
		return exceptions;
	}
	
	protected String[] exceptions;
}
