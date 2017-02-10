package org.kucro3.jam2.util.context.visitable;

import org.kucro3.jam2.util.MethodContext;
import org.objectweb.asm.MethodVisitor;

public class VisitedMethodFullyModifiableCompound extends VisitedMethodCompound
		implements MethodContext.FullyModifiable {
	public VisitedMethodFullyModifiableCompound(MethodContext.FullyModifiable mc, MethodVisitor mv) 
	{
		super(mc, mv);
	}
	
	public VisitedMethodFullyModifiableCompound(MethodContext.FullyModifiable mc) 
	{
		super(mc);
	}
	
	@Override
	public void setExceptions(String[] exceptions) 
	{
		this.mc.setExceptions(exceptions);
	}
	
	@Override
	public void setModifier(int modifier)
	{
		this.mc.setModifier(modifier);
	}
	
	@Override
	public void setName(String name)
	{
		this.mc.setName(name);
	}
	
	@Override
	public void setDescriptor(String descriptor)
	{
		this.mc.setDescriptor(descriptor);
	}
	
	@Override
	public void setSignature(String signature)
	{
		this.mc.setSignature(signature);
	}
}
