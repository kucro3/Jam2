package org.kucro3.jam2.jar;

public interface ClassField {
	int getAccess();
	
	String getDescriptor();
	
	String getName();
	
	String getSignature();
	
	Object getValue();
	
	default void setAccess(int access) {throw new UnsupportedOperationException();}
	
	default void setDescriptor(String desc) {throw new UnsupportedOperationException();}
	
	default void setSignature(String signature) {throw new UnsupportedOperationException();}
	
	default void setValue(Object value) {throw new UnsupportedOperationException();}
	
	public static interface Modifiable extends ClassField
	{
		void setAccess(int access);
		
		void setDescriptor(String desc);
		
		void setSignature(String signature);
		
		void setValue(Object value);
	}
}
