package org.kucro3.jam2.visitor;

import org.objectweb.asm.MethodVisitor;

public class MethodCacheVisitor extends InstructionVisitor implements CacheVisitor<MethodVisitor> {
	@Override
	public void clear() 
	{
	}

	@Override
	public void revisit(MethodVisitor t) 
	{
		
	}
}