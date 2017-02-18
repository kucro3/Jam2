package org.kucro3.jam2.jar;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.kucro3.jam2.jar.Jar.ClassFile;
import org.kucro3.jam2.jar.Jar.Resource;
import org.kucro3.jam2.jar.Jar.Resource.ResourceInputStream;
import org.kucro3.jam2.jar.Jar.ResourceState;
import org.kucro3.jam2.util.Jam2Util;

public final class JarPacker {
	private JarPacker()
	{
	}
	
	public static void packTo(Jar jar, File file) throws IOException
	{
		ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
		
		Manifest mf = jar.getManifest();
		zos.putNextEntry(new ZipEntry(Jar.MANIFEST_NAME));
		mf.write(zos);
		zos.closeEntry();
		
		for(ClassFile clz : jar.getClasses())
		{
			zos.putNextEntry(new ZipEntry(Jam2Util.fromInternalNameToResource(clz.getName())));
			zos.write(clz.toBytes());
			zos.closeEntry();
		}
		
		for(Resource res : jar.getResources())
		{
			zos.putNextEntry(new ZipEntry(res.getResourceName()));
			ResourceState oldState = res.getState();
			res.ensureStateSolid();
			
			ResourceInputStream ris = res.getInputStream();
			int b;
			while((b = ris.read()) != -1)
				zos.write(b);
			
			res.setState(oldState);
			zos.closeEntry();
		}
		
		zos.flush();
		zos.close();
	}
	
	public static void packTo(Jar jar, String file) throws IOException
	{
		File f = new File(file);
		if(!f.exists())
			f.createNewFile();
		packTo(jar, f);
	}
}