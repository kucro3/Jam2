package org.kucro3.jam2.visitor.cache;

import org.kucro3.jam2.visitor.InstructionRegionVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.MethodVisitor;

public class MethodCacheVisitor extends InstructionRegionVisitor implements CacheVisitor<MethodVisitor> {
	public MethodCacheVisitor()
	{
		super();
	}
	
	public MethodCacheVisitor(MethodVisitor mv)
	{
		super(mv);
	}
	
	@Override
	public void clear() 
	{
		super.container.clear();
		this.ac.clear();
	}

	@Override
	public void revisit(MethodVisitor t) 
	{
		this.ac.revisit(t);
		super.container.revisit(t);
	}
	
	@Override
	public void visitAttribute(Attribute attr)
	{
		ac.visitAttribute(attr);
	}
	
	private final AttributeCache ac = new AttributeCache();
}