package org.kucro3.jam2.invoke;

class ConstructorInvokerLambdaImpl extends ConstructorInvoker {
	ConstructorInvokerLambdaImpl(Class<?> declaringClass, int modifier, Class<?>[] arguments,
			LambdaInvocation invocation)
	{
		super(declaringClass, modifier, arguments);
		this.invocation = invocation;
	}
	
	@Override
	public Object newInstance(Object... args)
	{
		return invocation.newInstance(args);
	}
	
	final LambdaInvocation invocation;
	
	public static interface LambdaInvocation
	{
		public Object newInstance(Object... args);
	}
}
