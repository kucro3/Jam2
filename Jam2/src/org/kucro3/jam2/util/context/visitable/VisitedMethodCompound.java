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
	
	static VisitedMethodCompound asVisitable(MethodContext mc)
	{
		if(mc instanceof VisitedMethodCompound)
			return (VisitedMethodCompound) mc;
		return null;
	}
	
	public VisitedMethodCompound(MethodContext mc, MethodVisitor mv)
	{
		super(mv);
		this.mc = Objects.requireNonNull(mc);
		
		VisitedMethodCompound visitableRef = asVisitable(mc);
		if(visitableRef != null)
			if(mv != null)
				throw new IllegalStateException("Duplicated method visitor");
			else
				super.mv = visitableRef;
	}
	
	public VisitedMethodCompound(MethodContext mc)
	{
		this(mc, null);
	}

	@Override
	public String[] getExceptions() 
	{
		return this.mc.getExceptions();
	}

	@Override
	public String getDeclaringType()
	{
		return this.mc.getDeclaringType();
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
