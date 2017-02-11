package org.kucro3.jam2.util.context.hook;

import org.kucro3.jam2.util.ClassContext;
import org.kucro3.jam2.util.FieldContext;
import org.kucro3.jam2.util.MethodContext;
import org.kucro3.jam2.util.context.visitable.VisitedFieldCompound;
import org.kucro3.jam2.util.context.visitable.VisitedMethodCompound;
import org.objectweb.asm.ClassVisitor;

import static org.kucro3.jam2.util.context.hook.Action.*;

import java.util.Collection;

abstract class HookedVisitableClassCompoundTemplateA extends HookedVisitableClassCompound {
	HookedVisitableClassCompoundTemplateA(ClassContext ref, ClassVisitor cv)
	{
		super(ref, cv);
	}
	
	HookedVisitableClassCompoundTemplateA(ClassContext ref)
	{
		super(ref);
	}
	
	@Override
	public boolean containsField(String name)
	{
		return ref.containsField((String) hooks.fire(ref, null, null, CC_containsField, name)[0]);
	}
	
	@Override
	public boolean containsMethod(String descriptor)
	{
		return ref.containsMethod((String) hooks.fire(ref, null, null, CC_containsMethod, descriptor)[0]);
	}
	
	@Override
	public String getDebug()
	{
		hooks.fire(ref, null, null, CC_getDebug, (Object[]) null);
		return ref.getDebug();
	}
	
	@Override
	public String getEnclosingClass()
	{
		hooks.fire(ref, null, null, CC_getEnclosingClass, (Object[]) null);
		return ref.getEnclosingClass();
	}
	
	@Override
	public String getEnclosingMethodName()
	{
		hooks.fire(ref, null, null, CC_getEnclosingMethodName, (Object[]) null);
		return ref.getEnclosingMethodName();
	}
	
	@Override
	public String getEnclosingMethodDescriptor()
	{
		hooks.fire(ref, null, null, CC_getEnclosingMethodDescriptor, (Object[]) null);
		return ref.getEnclosingMethodDescriptor();
	}
	
	@Override
	public FieldContext getField(String name)
	{
		return ref.getField((String) hooks.fire(ref, null, null, CC_getField, name)[0]);
	}
	
	@Override
	public Collection<FieldContext> getFields()
	{
		hooks.fire(ref, null, null, CC_getFields, (Object[]) null);
		return ref.getFields();
	}
	
	@Override
	public String[] getInterfaces()
	{
		hooks.fire(ref, null, null, CC_getInterfaces, (Object[]) null);
		return ref.getInterfaces();
	}
	
	@Override
	public MethodContext getMethod(String descriptor)
	{
		return ref.getMethod((String) hooks.fire(ref, null, null, CC_getMethod, (Object[]) null)[0]);
	}
	
	@Override
	public Collection<MethodContext> getMethods()
	{
		hooks.fire(ref, null, null, CC_getMethods, (Object[]) null);
		return ref.getMethods();
	}
	
	@Override
	public String getSignature() 
	{
		hooks.fire(ref, null, null, CC_getSignature, (Object[]) null);
		return ref.getSignature();
	}

	@Override
	public String getName() 
	{
		hooks.fire(ref, null, null, CC_getName, (Object[]) null);
		return ref.getName();
	}

	@Override
	public int getModifier() 
	{
		hooks.fire(ref, null, null, CC_getModifier, (Object[]) null);
		return ref.getModifier();
	}

	@Override
	public String getSource() 
	{
		hooks.fire(ref, null, null, CC_getSource, (Object[]) null);
		return ref.getSource();
	}

	@Override
	public String getSuperClass() 
	{
		hooks.fire(ref, null, null, CC_getSuperClass, (Object[]) null);
		return ref.getSuperClass();
	}

	@Override
	public int getVersion() 
	{
		hooks.fire(ref, null, null, CC_getVersion, (Object[]) null);
		return ref.getVersion();
	}

	@Override
	public boolean hasMethod() 
	{
		hooks.fire(ref, null, null, CC_hasMethod, (Object[]) null);
		return ref.hasMethod();
	}

	@Override
	public boolean hasField() 
	{
		hooks.fire(ref, null, null, CC_hasField, (Object[]) null);
		return ref.hasField();
	}

	@Override
	public VisitedFieldCompound newField(int modifier, String name, String descriptor, String signature, Object value) 
	{
		Object[] ret = hooks.fire(ref, null, null, CC_newField,
				modifier, name, descriptor, signature, value);
		return super.newField(
				(Integer) 	ret[0],
				(String) 	ret[1],
				(String) 	ret[2],
				(String) 	ret[3],
				(Object)	ret[4]);
	}

	@Override
	public VisitedMethodCompound newMethod(int modifier, String name, String descriptor, String signature,
			String[] exceptions)
	{
		Object[] ret = hooks.fire(ref, null, null, CC_newMethod, 
				modifier, name, descriptor, signature, exceptions);
		return super.newMethod(
				(Integer) 	ret[0],
				(String) 	ret[1],
				(String) 	ret[2],
				(String) 	ret[3],
				(String[]) 	ret[4]);
	}
}
