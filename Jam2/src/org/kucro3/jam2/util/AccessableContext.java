package org.kucro3.jam2.util;

public interface AccessableContext
{
	int getModifier();
	
	String getName();
	
	String getDescriptor();
	
	String getSignature();
	
	default void setModifier(int modifier) {throw new UnsupportedOperationException();}
	
	default void setSignature(String signature)  {throw new UnsupportedOperationException();}
	
	default void setName(String name)  {throw new UnsupportedOperationException();}
	
	default void setDescriptor(String descriptor)  {throw new UnsupportedOperationException();}
	
	interface Modifiable extends AccessableContext
	{
		@Override
		void setModifier(int modifier);
		
		@Override
		void setSignature(String signature);
	}
	
	interface RestrictedModifiable extends Modifiable
	{
	}
	
	interface FullyModifiable extends Modifiable
	{
		@Override
		void setName(String name);
		
		@Override
		void setDescriptor(String descriptor);
	}
}
