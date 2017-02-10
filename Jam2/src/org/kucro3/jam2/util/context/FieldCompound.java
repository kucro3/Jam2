package org.kucro3.jam2.util.context;

import java.util.Objects;

import org.kucro3.jam2.util.FieldContext;

public abstract class FieldCompound implements FieldContext.Compound {
	public static FieldCompound newCompound(FieldContext context)
	{
		if(context instanceof FieldContext.RestrictedModifiable)
			return new FieldRestrictedModifiableCompound((FieldContext.RestrictedModifiable) context);
		else if(context instanceof FieldContext.FullyModifiable)
			return new FieldFullyModifiableCompound((FieldContext.FullyModifiable) context);
		else
			return new FieldConstantCompound(context);
	}
	
	public FieldCompound(FieldContext ctx)
	{
		this.ref = Objects.requireNonNull(ctx);
	}
	
	@Override
	public Object getValue() 
	{
		return ref.getValue();
	}

	@Override
	public String getDeclaringClass() 
	{
		return ref.getDeclaringClass();
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
	public FieldContext getContext() 
	{
		return ref;
	}
	
	protected final FieldContext ref;
}
