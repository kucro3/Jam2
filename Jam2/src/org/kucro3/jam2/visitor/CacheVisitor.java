package org.kucro3.jam2.visitor;

public interface CacheVisitor<T> {
	public void clear();
	
	public void revisit(T t);
}