package org.kucro3.jam2.util.context.visitable;

import org.kucro3.jam2.util.MethodContext;
import org.kucro3.jam2.util.Version;
import org.objectweb.asm.MethodVisitor;

public class VisitedMethodCompound extends MethodVisitor implements MethodContext {
	public VisitedMethodCompound(MethodContext mc, MethodVisitor mv)
	{
		super(Version.getASMVersion(), mv);
		this.mc = mc;
	}

	@Override
	public String[] getExceptions() 
	{
		return this.mc.getExceptions();
	}

	@Override
	public String getDeclaringClass() 
	{
		return this.mc.getDeclaringClass();
	}

	@Override
	public String getReturnType() 
	{
		return this.mc.getReturnType();
	}

	@Override
	public String[] getArguments() 
	{
		return this.mc.getArguments();
	}

	@Override
	public int getModifier() 
	{
		return this.mc.getModifier();
	}

	@Override
	public String getName() 
	{
		return this.mc.getName();
	}

	@Override
	public String getDescriptor() 
	{
		return this.mc.getDescriptor();
	}

	@Override
	public String getSignature() 
	{
		return this.mc.getSignature();
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
	
	public MethodVisitor getVisitor()
	{
		return super.mv;
	}
	
	protected MethodContext mc;
}
