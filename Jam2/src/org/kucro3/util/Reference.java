package org.kucro3.util;

public class Reference<T> {
	public Reference(T ref)
	{
		this.ref = ref;
	}
	
	public T get()
	{
		return ref;
	}
	
	public void set(T ref)
	{
		this.ref = ref;
	}
	
	private T ref;
}
