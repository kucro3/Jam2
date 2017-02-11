package org.kucro3.jam2.util.context;

import java.util.Collection;
import java.util.Objects;

import org.kucro3.jam2.util.ClassContext;
import org.kucro3.jam2.util.FieldContext;
import org.kucro3.jam2.util.MethodContext;

public abstract class ClassCompound implements ClassContext.Compound {
	public static ClassCompound newCompound(ClassContext ctx)
	{
		if(ctx instanceof ClassContext.RestrictedModifiable)
			return new ClassRestrictedModifiableCompound((ClassContext.RestrictedModifiable) ctx);
		else if(ctx instanceof ClassContext.FullyModifiable)
			return new ClassFullyModifiableCompound((ClassContext.FullyModifiable) ctx);
		else
			return new ClassConstantCompound(ctx);
	}
	
	public ClassCompound(ClassContext ctx)
	{
		this.ref = Objects.requireNonNull(ctx);
	}
	
	@Override
	public boolean hasField()
	{
		return ref.hasField();
	}
	
	@Override
	public boolean hasMethod()
	{
		return ref.hasMethod();
	}
	
	@Override
	public String getDebug() 
	{
		return ref.getDebug();
	}

	@Override
	public String getEnclosingClass() 
	{
		return ref.getEnclosingClass();
	}

	@Override
	public String getEnclosingMethodName() 
	{
		return ref.getEnclosingMethodName();
	}

	@Override
	public String getEnclosingMethodDescriptor() 
	{
		return ref.getEnclosingMethodDescriptor();
	}

	@Override
	public String[] getInterfaces() 
	{
		return ref.getInterfaces();
	}

	@Override
	public String getSignature() 
	{
		return ref.getSignature();
	}

	@Override
	public String getName() 
	{
		return ref.getName();
	}

	@Override
	public int getModifier() 
	{
		return ref.getModifier();
	}

	@Override
	public String getSource() 
	{
		return ref.getSource();
	}

	@Override
	public String getSuperClass() 
	{
		return ref.getSuperClass();
	}

	@Override
	public int getVersion() 
	{
		return ref.getVersion();
	}

	@Override
	public boolean containsField(String name) 
	{
		return ref.containsField(name);
	}

	@Override
	public boolean containsMethod(String descriptor) 
	{
		return ref.containsMethod(descriptor);
	}

	@Override
	public Collection<FieldContext> getFields() 
	{
		return ref.getFields();
	}

	@Override
	public FieldContext getField(String name) 
	{
		return ref.getField(name);
	}

	@Override
	public Collection<MethodContext> getMethods() 
	{
		return ref.getMethods();
	}

	@Override
	public MethodContext getMethod(String descriptor) 
	{
		return ref.getMethod(descriptor);
	}

	@Override
	public FieldContext newField(int modifier, String name, String descriptor, String signature, Object value) 
	{
		return ref.newField(modifier, name, descriptor, signature, value);
	}

	@Override
	public MethodContext newMethod(int modifier, String name, String descriptor, String signature,
			String[] exceptions) 
	{
		return ref.newMethod(modifier, name, descriptor, signature, exceptions);
	}

	@Override
	public ClassContext getContext() 
	{
		return ref;
	}
	
	protected final ClassContext ref;
}
