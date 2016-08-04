package org.kucro3.jam2.invoke;

class FieldInvokerLambdaImpl extends FieldInvoker {
	FieldInvokerLambdaImpl(Class<?> declaringClass, int modifier, String name, Class<?> type,
			LambdaGet getter, LambdaSet setter)
	{
		super(declaringClass, modifier, name, type);
		this.getter = getter;
		this.setter = setter;
	}
	
	@Override
	public Object get(Object obj)
	{
		return getter.get(obj);
	}
	
	@Override
	public void set(Object obj, Object args)
	{
		setter.set(obj, args);
	}
	
	final LambdaGet getter;
	
	final LambdaSet setter;
	
	public static interface LambdaGet
	{
		public Object get(Object obj);
	}
	
	public static interface LambdaSet
	{
		public void set(Object obj, Object args);
	}
}
