package org.kucro3.jam2.jar;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class JarPacker {
	private JarPacker()
	{
	}
	
	public static void packTo(final Jar jar, File file/*, boolean classesOnly*/) throws IOException
	{
		if(!file.exists())
			if(!file.createNewFile())
				throw new IOException("Cannot create new file: " + file);
		
		packTo(jar, new BufferedOutputStream(new FileOutputStream(file)));
	}
	
	public static void packTo(final Jar jar, OutputStream os/*, boolean classesOnly*/) throws IOException
	{
		ZipOutputStream zos = new ZipOutputStream(os);
		
		for(String res : jar.getResources())
			writeEntry(res, zos, (eos) -> pourTo(jar.getResourceAsStream(res), eos));
		
//		if(!classesOnly)
//			for(String res : jar.getResourcesExpectClasses())
//				writeEntry(res, zos, (eos) -> pourTo(jar.getResourceAsStream(res), eos));
//		
//		for(ClassFile cf : jar.getClasses())
//			writeEntry(cf.getLocation(), zos, (eos) -> pourTo(jar.getResourceAsStream(cf.getLocation()), eos));
		
		zos.flush();
		zos.close();
	}
	
	static void pourTo(InputStream is, OutputStream os) throws IOException
	{
		int b;
		while((b = is.read()) != -1)
			os.write(b);
		
		is.close();
	}
	
	static void writeEntry(String entryName, ZipOutputStream os, Function func) throws IOException
	{
		os.putNextEntry(new ZipEntry(entryName));
		func.apply(os);
		os.closeEntry();
	}
	
	interface Function
	{
		void apply(OutputStream os) throws IOException;
	}
}