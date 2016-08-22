package org.kucro3.jam2.jar;

import org.kucro3.jam2.util.ClassContext;
import org.objectweb.asm.MethodVisitor;

public class ClassMethodVisitor extends MethodVisitor {
	ClassMethodVisitor(ClassMethod cm, MethodVisitor mv)
	{
		super(ClassContext.API, mv);
		this.owner = cm;
	}
	
	public ClassMethod getOwner()
	{
		return owner;
	}
	
	private final ClassMethod owner;
}
