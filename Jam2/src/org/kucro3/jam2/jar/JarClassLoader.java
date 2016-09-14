package org.kucro3.jam2.jar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class JarClassLoader extends URLClassLoader {
	public JarClassLoader(File file) throws MalformedURLException
	{
		super(new URL[] {file.toURI().toURL()});
	}
	
	public JarClassLoader(File file, ClassLoader cl) throws MalformedURLException
	{
		super(new URL[] {file.toURI().toURL()}, cl);
	}
	
	static byte[] getBytes(URL url)
	{
		if(url == null)
			throw new IllegalStateException("Null url");
		
		try {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			InputStream is = url.openStream();
			
			int byt;
			while((byt = is.read()) != -1)
				buffer.write(byt);
			
			is.close();
			return buffer.toByteArray();
		} catch (IOException e) {
			// unused
			throw new IllegalStateException(e);
		}
	}
	
	Class<?> nextClass(String location, ByteArrayCallback callback, boolean loadClass) throws IOException
	{
		String className = JarFile.toClassName(location);
		Class<?> result;
		
		if(loadClass)
			try {
				result = super.loadClass(className);
			} catch (ClassNotFoundException e) {
				// unused
				throw new IllegalStateException(e);
			}
		else
			result = CLASS_NOT_REQUIRED;
		
		if(callback != null)
			callback.callback(getBytes(super.getResource(location)));
		
		return result;
	}
	
	static interface ByteArrayCallback
	{
		void callback(byte[] byts);
	}
	
	public static final int MAGIC_VALUE = 0xCAFEBABE;
	
	public static final Class<?> CLASS_NOT_REQUIRED = ClassNotRequired.class;
	
	private class ClassNotRequired {}
}
