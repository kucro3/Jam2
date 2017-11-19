package org.kucro3.jam2.util.context;

import java.util.Objects;

import org.kucro3.jam2.util.MethodContext;

public abstract class MethodCompound implements MethodContext.Compound {
	public static MethodCompound newCompound(MethodContext ctx)
	{
		if(ctx instanceof MethodContext.RestrictedModifiable)
			return new MethodRestrictedModifiableCompound((MethodContext.RestrictedModifiable) ctx);
		else if(ctx instanceof MethodContext.FullyModifiable)
			return new MethodFullyModifiableCompound((MethodContext.FullyModifiable) ctx);
		else
			return new MethodConstantCompound(ctx);
	}
	
	public MethodCompound(MethodContext ctx)
	{
		this.ref = Objects.requireNonNull(ctx);
	}
	
	@Override
	public String[] getExceptions() 
	{
		return ref.getExceptions();
	}

	@Override
	public String getDeclaringType()
	{
		return ref.getDeclaringType();
	}

	@Override
	public String getReturnType() 
	{
		return ref.getReturnType();
	}

	@Override
	public String[] getArguments() 
	{
		return ref.getArguments();
	}

	@Override
	public int getModifier() 
	{
		return ref.getModifier();
	}

	@Override
	public String getName() 
	{
		return ref.getName();
	}

	@Override
	public String getDescriptor() 
	{
		return ref.getDescriptor();
	}

	@Override
	public String getSignature() 
	{
		return ref.getSignature();
	}

	@Override
	public MethodContext getContext() 
	{
		return ref;
	}
	
	protected final MethodContext ref;
}
