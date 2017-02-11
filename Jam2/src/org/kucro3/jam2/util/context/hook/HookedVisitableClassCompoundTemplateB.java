package org.kucro3.jam2.util.context.hook;

import org.kucro3.jam2.util.ClassContext;
import org.objectweb.asm.ClassVisitor;

import static org.kucro3.jam2.util.context.hook.Action.*;

abstract class HookedVisitableClassCompoundTemplateB extends HookedVisitableClassCompoundTemplateA {
	HookedVisitableClassCompoundTemplateB(ClassContext ref, ClassVisitor cv)
	{
		super(ref, cv);
	}
	
	HookedVisitableClassCompoundTemplateB(ClassContext ref)
	{
		super(ref);
	}

	@Override
	public void setSource(String source) 
	{
		ref.setSource((String) hooks.fire(ref, null, null, CC_setSource, source)[0]);
	}

	@Override
	public void setDebug(String debug) 
	{
		ref.setDebug((String) hooks.fire(ref, null, null, CC_setDebug, debug)[0]);
	}

	@Override
	public void setSuperClass(String superClass) 
	{
		ref.setSuperClass((String) hooks.fire(ref, null, null, CC_setSuperClass, superClass)[0]);
	}

	@Override
	public void setInterfaces(String[] interfaces) 
	{
		ref.setInterfaces((String[]) hooks.fire(ref, null, null, CC_setSource, (Object) interfaces)[0]);
	}

	@Override
	public void setEnclosingClass(String enclosingClass) 
	{
		ref.setEnclosingClass((String) hooks.fire(ref, null, null, CC_setEnclosingClass, enclosingClass)[0]);
	}

	@Override
	public void setEnclosingMethodName(String name) 
	{
		ref.setEnclosingMethodName((String) hooks.fire(ref, null, null, CC_setEnclosingMethodName, name)[0]);
	}

	@Override
	public void setEnclosingMethodDescriptor(String descriptor) 
	{
		ref.setEnclosingMethodDescriptor((String) hooks.fire(ref, null, null, CC_setEnclosingMethodDescriptor, descriptor)[0]);
	}

	@Override
	public void setVersion(int version)
	{
		ref.setVersion((Integer) hooks.fire(ref, null, null, CC_setVersion, version)[0]);	
	}

	@Override
	public void setModifier(int modifier) 
	{
		ref.setModifier((Integer) hooks.fire(ref, null, null, CC_setModifier, modifier)[0]);	
	}

	@Override
	public void setSignature(String signature) 
	{
		ref.setSignature((String) hooks.fire(ref, null, null, CC_setSignature, signature)[0]);
	}

	@Override
	public void clearFields() 
	{
		hooks.fire(ref, null, null, CC_clearFields, (Object[]) null);
		ref.clearFields();
	}

	@Override
	public void removeField(String name) 
	{
		ref.removeField((String) hooks.fire(ref, null, null, CC_removeField, name)[0]);
	}

	@Override
	public void clearMethods() 
	{
		hooks.fire(ref, null, null, CC_clearMethods, (Object[]) null);
		ref.clearMethods();
	}

	@Override
	public void removeMethod(String fullDescriptor) 
	{
		ref.removeMethod((String) hooks.fire(ref, null, null, CC_removeMethod, fullDescriptor)[0]);
	}
}
