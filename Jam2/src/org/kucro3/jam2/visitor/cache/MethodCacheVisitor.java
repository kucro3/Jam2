package org.kucro3.jam2.visitor.cache;

import org.objectweb.asm.MethodVisitor;

public class MethodCacheVisitor extends InstructionRegionVisitor implements CacheVisitor {
	public MethodCacheVisitor()
	{
		super();
	}
	
	public MethodCacheVisitor(MethodVisitor mv)
	{
		super(mv);
	}
}