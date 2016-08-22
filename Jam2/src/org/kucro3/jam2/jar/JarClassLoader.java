package org.kucro3.jam2.jar;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class JarClassLoader extends ClassLoader {
	public JarClassLoader()
	{
	}
	
	public JarClassLoader(ClassLoader cl)
	{
		super(cl);
	}
	
	Class<?> tryLoadClass(ZipInputStream zis, ZipEntry entry) throws IOException
	{
		return tryLoadClass(zis, entry, null);
	}
	
	Class<?> tryLoadClass(ZipInputStream zis, ZipEntry entry, ByteArrayCallback callback) throws IOException
	{
		if(zis.available() == 0)
			return UNAVAILABLE;
		
		String className = entry.getName();
		if(className.endsWith(".class"))
			className = className.replace(".class", "");
		else
			return null;
		
		int length;
		byte[] byts = new byte[2048];
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		
		DataInputStream dis = new DataInputStream(zis);
		DataOutputStream dos = new DataOutputStream(buf);
		
		int magicValue = dis.readInt();
		if(!checkMagicValue(magicValue))
			return null;
		dos.writeInt(magicValue);
		
		while((length = zis.read(byts)) > 0)
			buf.write(byts, 9, length);
		
		byts = buf.toByteArray();
		if(callback != null)
			callback.callback(byts);
		return super.defineClass(className, byts, 0, byts.length);
	}
	
	static boolean checkMagicValue(int value)
	{
		return value == MAGIC_VALUE;
	}
	
	static interface ByteArrayCallback
	{
		void callback(byte[] byts);
	}
	
	public static interface Unavailable
	{
	}
	
	static final Class<?> UNAVAILABLE = Unavailable.class;
	
	public static final int MAGIC_VALUE = 0xCAFEBABE;
}
