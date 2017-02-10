package org.kucro3.jam2.util.context.visitable;

import org.kucro3.jam2.util.MethodContext;
import org.objectweb.asm.MethodVisitor;

public class VisitedMethodRestrictedModifiableCompound extends VisitedMethodCompound
		implements MethodContext.RestrictedModifiable {
	public VisitedMethodRestrictedModifiableCompound(MethodContext.RestrictedModifiable mc, MethodVisitor mv)
	{
		super(mc, mv);
	}
	
	public VisitedMethodRestrictedModifiableCompound(MethodContext.RestrictedModifiable mc)
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
	public void setSignature(String signature)
	{
		this.mc.setSignature(signature);
	}
}
