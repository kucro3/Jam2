package org.kucro3.jam2.util.context.visitable;

import java.util.Collection;
import java.util.Objects;

import org.kucro3.jam2.util.ClassContext;
import org.kucro3.jam2.util.FieldContext;
import org.kucro3.jam2.util.MethodContext;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

public abstract class VisitableClassContextCompound extends VisitableClassContext implements ClassContext.Compound {
	public static VisitableClassContextCompound newCompound(ClassContext ref)
	{
		return newCompound(ref, null);
	}
	
	public static VisitableClassContextCompound newCompound(ClassContext ref, ClassVisitor cv)
	{
		if(ref instanceof ClassContext.RestrictedModifiable)
			return new VisitableClassContextRestrictedModifiableCompound((ClassContext.RestrictedModifiable) ref, cv);
		else if(ref instanceof ClassContext.FullyModifiable)
			return new VisitableClassContextFullyModifiableCompound((ClassContext.FullyModifiable) ref, cv);
		else
			return new VisitableClassContextConstantCompound(ref, cv);
	}
	
	VisitableClassContextCompound(ClassContext ref)
	{
		this.ref = Objects.requireNonNull(ref);
	}
	
	VisitableClassContextCompound(ClassContext ref, ClassVisitor cv)
	{
		super(cv);
		this.ref = Objects.requireNonNull(ref);
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
	public final VisitedFieldCompound newField(int modifier, String name, String descriptor, String signature, Object value) 
	{
		FieldVisitor fv = super.visitField(modifier, name, descriptor, signature, value);
		FieldContext fc = ref.newField(modifier, name, descriptor, signature, value);
		return newFieldCompound(fc, fv);
	}
	
	@Override
	public final VisitedFieldCompound visitField(int modifier, String name, String descriptor, String signature, Object value)
	{
		return this.newField(modifier, name, descriptor, signature, value);
	}
	
	protected VisitedFieldCompound newFieldCompound(FieldContext ctx, FieldVisitor fv)
	{
		return VisitedFieldCompound.newCompound(ctx, fv);
	}
	
	@Override
	public final VisitedMethodCompound newMethod(int modifier, String name, String descriptor, String signature,
			String[] exceptions)
	{
		MethodVisitor mv = super.visitMethod(modifier, name, descriptor, signature, exceptions);
		MethodContext mc = ref.newMethod(modifier, name, descriptor, signature, exceptions);
		return newMethodCompound(mc, mv);
	}
	
	@Override
	public final VisitedMethodCompound visitMethod(int modifier, String name, String descriptor, String signature,
			String[] exceptions)
	{
		return this.newMethod(modifier, name, descriptor, signature, exceptions);
	}
	
	protected VisitedMethodCompound newMethodCompound(MethodContext ctx, MethodVisitor mv)
	{
		return VisitedMethodCompound.newCompound(ctx, mv);
	}
	
	@Override
	public ClassContext getContext()
	{
		return ref;
	}
	
	protected final ClassContext ref;
}
