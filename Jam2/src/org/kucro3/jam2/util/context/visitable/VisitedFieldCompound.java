package org.kucro3.jam2.util.context.visitable;

import java.util.Objects;

import org.kucro3.jam2.util.FieldContext;
import org.objectweb.asm.FieldVisitor;

public abstract class VisitedFieldCompound extends AbstractVisitableFieldContext
		implements FieldContext.Compound, FieldContext.Visited {
	public static VisitedFieldCompound newCompound(FieldContext fc, FieldVisitor fv)
	{
		if(fc instanceof FieldContext.RestrictedModifiable)
			return new VisitedFieldRestrictedModifiableCompound((FieldContext.RestrictedModifiable) fc, fv);
		else if(fc instanceof FieldContext.FullyModifiable)
			return new VisitedFieldFullyModifiableCompound((FieldContext.FullyModifiable) fc, fv);
		else
			return new VisitedFieldConstantCompound(fc, fv);
	}
	
	public VisitedFieldCompound(FieldContext fc, FieldVisitor fv)
	{
		super(fv);
		this.fc = Objects.requireNonNull(fc);
	}
	
	public VisitedFieldCompound(FieldContext fc)
	{
		super();
		this.fc = Objects.requireNonNull(fc);
	}
	
	@Override
	public Object getValue() 
	{
		return this.fc.getValue();
	}

	@Override
	public String getDeclaringClass() 
	{
		return this.fc.getDeclaringClass();
	}

	@Override
	public int getModifier() 
	{
		return this.fc.getModifier();
	}

	@Override
	public String getName()
	{
		return this.fc.getName();
	}

	@Override
	public String getDescriptor() 
	{
		return this.fc.getDescriptor();
	}

	@Override
	public String getSignature() 
	{
		return this.fc.getSignature();
	}
	
	@Override
	public FieldVisitor getVisitor()
	{
		return super.fv;
	}
	
	@Override
	public FieldContext getContext()
	{
		return fc;
	}
	
	protected final FieldContext fc;
}
