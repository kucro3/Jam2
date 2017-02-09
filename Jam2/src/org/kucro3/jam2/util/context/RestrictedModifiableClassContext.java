package org.kucro3.jam2.util.context;

import org.kucro3.jam2.util.ClassContext;
import org.kucro3.jam2.util.ClassMemberMap;
import org.kucro3.jam2.util.FieldContext;
import org.kucro3.jam2.util.MethodContext;

public class RestrictedModifiableClassContext extends NormalMappedClassContext implements ClassContext.RestrictedModifiable
{
	public RestrictedModifiableClassContext(String name)
	{
		super(name);
	}
	
	public RestrictedModifiableClassContext(String name,
			ClassMemberMap<MethodContext, FieldContext> map)
	{
		super(name, map);
	}
	
	@Override
	public FieldContext.RestrictedModifiable newField(int modifier, String name, String descriptor, String signature, Object value) 
	{
		RestrictedModifiableFieldContext rmfc = new RestrictedModifiableFieldContext(getName(),
				modifier, name, descriptor, signature, value);
		super.map.putByField(name, rmfc);
		return rmfc;
	}
	
	@Override
	public MethodContext.RestrictedModifiable newMethod(int modifier, String name, String descriptor, String signature,
			String[] exceptions) 
	{
		RestrictedModifiableMethodContext rmmc = new RestrictedModifiableMethodContext(getName(),
				modifier, name, descriptor, signature, exceptions);
		super.map.putByMethod(name, descriptor, rmmc);
		return rmmc;
	}
	
	@Override
	public void setSource(String source) 
	{
		super.source = source;
	}

	@Override
	public void setDebug(String debug) 
	{
		super.debug = debug;
	}

	@Override
	public void setSuperClass(String superClass) 
	{
		super.superName = superClass;
	}

	@Override
	public void setInterfaces(String[] interfaces) 
	{
		super.interfaces = interfaces;
	}
	
	@Override
	public void setEnclosingClass(String enclosingClass) 
	{
		super.enclosingClass = enclosingClass;
	}

	@Override
	public void setEnclosingMethodName(String name) 
	{
		super.enclosingMethodName = name;
	}

	@Override
	public void setEnclosingMethodDescriptor(String descriptor) 
	{
		super.enclosingMethodDescriptor = descriptor;
	}

	@Override
	public void setVersion(int version) 
	{
		super.version = version;
	}

	@Override
	public void setModifier(int modifier) 
	{
		super.modifier = modifier;
	}

	@Override
	public void setSignature(String signature) 
	{
		super.signature = signature;
	}

	@Override
	public void clearFields() 
	{
		map.clearFieldMapped();
	}

	@Override
	public void removeField(String name) 
	{
		map.removeByField(name);
	}

	@Override
	public void clearMethods() 
	{
		map.clearMethodMapped();
	}
	
	@Override
	public void removeMethod(String fullDescriptor) 
	{
		map.removeByMethod(fullDescriptor);
	}
}