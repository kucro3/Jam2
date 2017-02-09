package org.kucro3.jam2.util;

public interface MethodContext extends AccessableContext {
	String[] getExceptions();
	
	String getDeclaringClass();
	
	String getReturnType();
	
	String[] getArguments();
	
	default void setExceptions(String[] exceptions) {throw new UnsupportedOperationException();}
	
	interface Modifiable extends MethodContext, AccessableContext.Modifiable
	{
		@Override
		void setExceptions(String[] exceptions);
	}
	
	interface RestrictedModifiable extends Modifiable, AccessableContext.RestrictedModifiable
	{
	}
	
	interface FullyModifiable extends Modifiable, AccessableContext.FullyModifiable
	{
	}
}
