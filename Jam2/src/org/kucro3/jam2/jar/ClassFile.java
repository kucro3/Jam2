package org.kucro3.jam2.jar;

import java.util.Collection;

public interface ClassFile {
	boolean containsField(String name);
	
	boolean containsMethod(String name, Class<?> returnType, Class<?>... arguments);
	
	String getClassName();
	
	String getDebug();
	
	ClassField getField(String name);
	
	Collection<ClassField> getFields();
	
	Class<?> getLoadedClass();
	
	ClassMethod getMethod(String name, Class<?> returnType, Class<?>... arguments);
	
	ClassMethod getMethod(String name, String returnType, String... arguments);
	
	ClassMethod getMethod(String name, String descriptor);
	
	Collection<ClassMethod> getMethods();
	
	String getSource();
	
	Class<?> forceLoad();
	
	default boolean removeMethod(String name, Class<?> returnType, Class<?>... arguments) {throw new UnsupportedOperationException();}
	
	default boolean removeField(String name) {throw new UnsupportedOperationException();}
	
	default ClassMethod addMethod(String name, Class<?> returnType, Class<?>... arguments) {throw new UnsupportedOperationException();}
	
	default ClassMethod addMethod(String name, String returnType, String... arguments) {throw new UnsupportedOperationException();}
	
	default ClassMethod addMethod(String name, String descriptor) {throw new UnsupportedOperationException();}
	
	default ClassField addField(String name)  {throw new UnsupportedOperationException();}
	
	default void setDebug(String debug) {throw new UnsupportedOperationException();}
	
	default void setSource(String source) {throw new UnsupportedOperationException();}
	
	public static interface Modifiable extends ClassFile
	{
		boolean removeMethod(String name, Class<?> returnType, Class<?>... arguments);
		
		boolean removeField(String name);
		
		ClassMethod.Modifiable addMethod(String name, Class<?> returnType, Class<?>... arguments);
		
		ClassMethod.Modifiable addMethod(String name, String returnType, String... arguments);
		
		ClassMethod.Modifiable addMethod(String name, String descriptor);
		
		ClassField.Modifiable addField(String name);
		
		void setDebug(String debug);
		
		void setSource(String source);
	}
}