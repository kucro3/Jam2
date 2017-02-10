package org.kucro3.jam2.util.context.visitable;

import java.util.Collection;
import java.util.Objects;

import org.kucro3.jam2.util.ClassContext;
import org.kucro3.jam2.util.FieldContext;
import org.kucro3.jam2.util.MethodContext;
import org.objectweb.asm.ClassVisitor;

public abstract class VisitableClassContextCompound extends VisitableClassContext {
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
	public void setSource(String source) 
	{
		ref.setSource(source);
	}

	@Override
	public void setDebug(String debug) 
	{
		ref.setDebug(debug);
	}

	@Override
	public void setSuperClass(String superClass) 
	{
		ref.setSuperClass(superClass);
	}

	@Override
	public void setInterfaces(String[] interfaces) 
	{
		ref.setInterfaces(interfaces);
	}

	@Override
	public void setEnclosingClass(String enclosingClass) 
	{
		ref.setEnclosingClass(enclosingClass);
	}

	@Override
	public void setEnclosingMethodName(String name) 
	{
		ref.setEnclosingMethodName(name);
	}

	@Override
	public void setEnclosingMethodDescriptor(String descriptor) 
	{
		ref.setEnclosingMethodDescriptor(descriptor);
	}

	@Override
	public void setVersion(int version) 
	{
		ref.setVersion(version);
	}

	@Override
	public void setModifier(int modifier) 
	{
		ref.setModifier(modifier);
	}

	@Override
	public void setSignature(String signature) 
	{
		ref.setSignature(signature);
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
	public void clearFields() 
	{
		ref.clearFields();
	}

	@Override
	public void removeField(String name) 
	{
		ref.removeField(name);
	}

	@Override
	public void clearMethods() 
	{
		ref.clearMethods();
	}

	@Override
	public void removeMethod(String fullDescriptor) 
	{
		ref.removeMethod(fullDescriptor);
	}

	@Override
	public void setName(String name) 
	{
		ref.setName(name);
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
	
	public ClassContext getContext()
	{
		return ref;
	}
	
	protected final ClassContext ref;
}
