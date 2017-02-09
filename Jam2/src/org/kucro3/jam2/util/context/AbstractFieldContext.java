package org.kucro3.jam2.util.context;

import org.kucro3.jam2.util.FieldContext;
import org.kucro3.util.anno.OverridingRequired;

public abstract class AbstractFieldContext extends AbstractAccessableContext implements FieldContext {
	@Override
	@OverridingRequired
	public abstract String getDeclaringClass();
	
	@Override
	public Object getValue() 
	{
		return value;
	}

	protected Object value;
}
