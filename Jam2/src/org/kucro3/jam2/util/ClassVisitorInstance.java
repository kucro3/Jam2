package org.kucro3.jam2.util;

import org.objectweb.asm.ClassVisitor;

public class ClassVisitorInstance extends ClassVisitor {
	public ClassVisitorInstance(int api)
	{
		super(api);
	}
	
	public ClassVisitorInstance(int api, ClassVisitor cv) 
	{
		super(api, cv);
	}
	
	public ClassVisitor getClassVisitor()
	{
		return super.cv;
	}
	
	public void setClassVisitor(ClassVisitor cv)
	{
		super.cv = cv;
	}
	
	public int getAPI()
	{
		return super.api;
	}
}
