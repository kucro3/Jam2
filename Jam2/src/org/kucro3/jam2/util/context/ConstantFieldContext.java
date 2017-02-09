package org.kucro3.jam2.util.context;

public class ConstantFieldContext extends NormalFieldContext {
	public ConstantFieldContext(String declaringClass, 
			int modifier, String name, String descriptor, String signature, Object value)
	{
		super(declaringClass);
		super.modifier = modifier;
		super.name = name;
		super.descriptor = descriptor;
		super.signature = signature;
		super.value = value;
	}
}