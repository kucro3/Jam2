package org.kucro3.jam2.jar;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.jar.Manifest;

public interface Jar {
	ClassFile forName(String name) throws ClassNotFoundException;
	
	ClassFile getClass(String name);
	
	Collection<ClassFile> getClasses();
	
	Manifest getManifest();
	
	Resource getResource(String name);
	
	InputStream getResourceAsStream(String name);
	
	Collection<String> getResources();
	
	ClassLoader getClassLoader();
	
	default boolean removeResource(String name) {throw new UnsupportedOperationException();}
	
	default boolean removeClass(String name) {throw new UnsupportedOperationException();}
	
	default boolean ensureResources() {throw new UnsupportedOperationException();}
	
	default boolean resourcesEnsured() {return true;}
	
	default ClassFile.Modifiable addClass(int version, int access, String name, String signature, Class<?> superClass, Class<?>[] interfaces)
			{throw new UnsupportedOperationException();}
	
	default Resource addResource(String name) {throw new UnsupportedOperationException();}
	
	public static interface Modifiable extends Jar
	{
		boolean removeResource(String name);
		
		boolean removeClass(String name);
		
		boolean ensureResources();
		
		boolean resourcesEnsured();
		
		ClassFile.Modifiable addClass(int version, int access, String name, String signature, Class<?> superClass, Class<?>[] interfaces);
		
		Resource addResource(String name);
	}
	
	public static interface Resource
	{
		default OutputStream getOutputStream() {throw new UnsupportedOperationException();}
		
		default void clear() {throw new UnsupportedOperationException();}
		
		default boolean ensured() {return true;}
		
		InputStream asInputStream();
		
		String getName();
		
		public static interface Modifiable extends Resource
		{
			void clear();
			
			boolean ensured();
		}
		
		public static interface Writeable extends Resource
		{
			OutputStream getOutputStream();
		}
	}
}
