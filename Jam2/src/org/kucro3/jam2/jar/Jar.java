package org.kucro3.jam2.jar;

import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.jar.Manifest;

public interface Jar {
	ClassFile forName(String name) throws ClassNotFoundException;
	
	ClassFile getClass(String name);
	
	Collection<ClassFile> getClasses();
	
	Manifest getManifest();
	
	InputStream getResourceAsStream(String name);
	
	Collection<String> getResources();
	
	URL getResource(String name);
	
	ClassLoader getClassLoader();
	
	Collection<String> getResourcesExpectClasses();
	
	boolean removeResource(String name);
	
	boolean removeClass(String name);
}
