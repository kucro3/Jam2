package org.kucro3.jam2.util.context.visitable;

import org.kucro3.jam2.util.ClassContext;
import org.objectweb.asm.ClassVisitor;

public class VisitableClassContextRestrictedModifiableCompound extends VisitableClassContextCompound
		implements ClassContext.RestrictedModifiable {
	public VisitableClassContextRestrictedModifiableCompound(ClassContext ref)
	{
		super(ref);
	}
	
	public VisitableClassContextRestrictedModifiableCompound(ClassContext ref, ClassVisitor cv)
	{
		super(ref, cv);
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
}
