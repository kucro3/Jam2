package org.kucro3.jam2.util.context;

import org.kucro3.jam2.util.FieldContext;

public abstract class AbstractFieldContext extends AbstractAccessableContext implements FieldContext {
	@Override
	public abstract String getDeclaringType();
	
	@Override
	public Object getValue() 
	{
		return value;
	}

	protected Object value;
}
