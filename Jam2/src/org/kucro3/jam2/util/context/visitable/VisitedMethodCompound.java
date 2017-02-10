package org.kucro3.jam2.util.context.visitable;

import java.util.Objects;

import org.kucro3.jam2.util.MethodContext;
import org.objectweb.asm.MethodVisitor;

public abstract class VisitedMethodCompound extends AbstractVisitableMethodContext 
		implements MethodContext.Compound, MethodContext.Visited {
	public static VisitedMethodCompound newCompound(MethodContext mc, MethodVisitor mv)
	{
		if(mc instanceof MethodContext.RestrictedModifiable)
			return new VisitedMethodRestrictedModifiableCompound((MethodContext.RestrictedModifiable) mc, mv);
		else if(mc instanceof MethodContext.FullyModifiable)
			return new VisitedMethodFullyModifiableCompound((MethodContext.FullyModifiable) mc, mv);
		else
			return new VisitedMethodConstantCompound(mc, mv);
	}
	
	public VisitedMethodCompound(MethodContext mc, MethodVisitor mv)
	{
		super(mv);
		this.mc = Objects.requireNonNull(mc);
	}
	
	public VisitedMethodCompound(MethodContext mc)
	{
		super();
		this.mc = Objects.requireNonNull(mc);
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
	public MethodVisitor getVisitor()
	{
		return super.mv;
	}
	
	@Override
	public MethodContext getContext() 
	{
		return mc;
	}
	
	protected final MethodContext mc;
}
