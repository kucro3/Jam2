package org.kucro3.jam2.invoke;

class MethodInvokerLambdaImpl extends MethodInvoker {
	MethodInvokerLambdaImpl(Class<?> declaringClass, int modifier, String name, Class<?> returnType,
			Class<?>[] arguments, LambdaInvocation invocation)
	{
		super(declaringClass, modifier, name, returnType, arguments);
		this.invocation = invocation;
	}
	
	@Override
	public Object invoke(Object obj, Object... args)
	{
		return invocation.invoke(obj, args);
	}
	
	final LambdaInvocation invocation;
	
	public static interface LambdaInvocation
	{
		public Object invoke(Object obj, Object... arguments);
	}
}
