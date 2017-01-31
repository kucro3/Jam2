package org.kucro3.jam2.visitor.cache;

import org.kucro3.jam2.util.annotation.AnnotationContainer;
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
	
	public AnnotationContainer getAnnotationContainer()
	{
		return super.annotations;
	}
	
	public AnnotationContainer getLocalAnnotationContainer()
	{
		return super.localAnnotations;
	}
}