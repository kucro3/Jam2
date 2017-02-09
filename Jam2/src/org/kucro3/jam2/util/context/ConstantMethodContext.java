package org.kucro3.jam2.util.context;

public class ConstantMethodContext extends NormalMethodContext {
	public ConstantMethodContext(String declaringClass,
			int modifier, String name, String descriptor, String signature, String[] exceptions)
	{
		super(declaringClass);
		super.modifier = modifier;
		super.name = name;
		super.descriptor = descriptor;
		super.signature = signature;
		super.exceptions = exceptions;
	}
}
