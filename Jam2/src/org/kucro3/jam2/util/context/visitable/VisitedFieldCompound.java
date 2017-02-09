package org.kucro3.jam2.util.context.visitable;

import org.kucro3.jam2.util.FieldContext;
import org.kucro3.jam2.util.Version;
import org.objectweb.asm.FieldVisitor;

public class VisitedFieldCompound extends FieldVisitor implements FieldContext {
	public VisitedFieldCompound(FieldContext fc, FieldVisitor fv)
	{
		super(Version.getASMVersion(), fv);
		this.fc = fc;
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
	public void setDescriptor(String descriptor) 
	{
		this.fc.setDescriptor(descriptor);
	}
	
	@Override
	public void setSignature(String signature)
	{
		this.fc.setSignature(signature);
	}
	
	@Override
	public void setModifier(int modifier)
	{
		this.fc.setModifier(modifier);
	}
	
	@Override
	public void setName(String name)
	{
		this.fc.setName(name);
	}
	
	@Override
	public void setValue(Object value)
	{
		this.fc.setValue(value);
	}
	
	public FieldVisitor getVisitor()
	{
		return super.fv;
	}
	
	protected FieldContext fc;
}
