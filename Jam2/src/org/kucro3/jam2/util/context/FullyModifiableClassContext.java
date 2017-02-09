package org.kucro3.jam2.util.context;

import java.util.Collection;

import org.kucro3.jam2.util.ClassContext;
import org.kucro3.jam2.util.ClassMemberMap;
import org.kucro3.jam2.util.FieldContext;
import org.kucro3.jam2.util.MethodContext;

public class FullyModifiableClassContext extends AbstractClassContext implements ClassContext.FullyModifiable {
	public FullyModifiableClassContext()
	{
		this(null, new ClassMemberMap<>());
	}
	
	public FullyModifiableClassContext(String name)
	{
		this(name, new ClassMemberMap<>());
	}
	
	public FullyModifiableClassContext(String name,
			ClassMemberMap<MethodContext, FieldContext> map)
	{
		this.name = name;
		this.map = map;
	}

	@Override
	public FieldContext.FullyModifiable newField(int modifier, String name, String descriptor, String signature, Object value)
	{
		FullyModifiableFieldContext fmfc = new FullyModifiableFieldContext(getName(),
				modifier, name, descriptor, signature, value);
		map.putByField(name, fmfc);
		return fmfc;
	}

	@Override
	public MethodContext.FullyModifiable newMethod(int modifier, String name, String descriptor, String signature,
			String[] exceptions)
	{
		FullyModifiableMethodContext fmmc = new FullyModifiableMethodContext(getName(),
				modifier, name, descriptor, signature, exceptions);
		map.putByMethod(name, descriptor, fmmc);
		return fmmc;
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
		this.signature = signature;
	}

	@Override
	public void setName(String name) 
	{
		this.name = name;
	}

	@Override
	public String getName() 
	{
		return name;
	}

	@Override
	public boolean containsField(String name)
	{
		return map.mappedByField(name);
	}

	@Override
	public boolean containsMethod(String descriptor) 
	{
		return map.mappedByMethod(descriptor);
	}

	@Override
	public FieldContext getField(String name)
	{
		return map.getByField(name);
	}

	@Override
	public MethodContext getMethod(String descriptor) 
	{
		return map.getByMethod(descriptor);
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
	
	@Override
	public Collection<FieldContext> getFields() 
	{
		return map.byFields();
	}

	@Override
	public Collection<MethodContext> getMethods() 
	{
		return map.byMethods();
	}
	
	protected String name;
	
	protected ClassMemberMap<MethodContext, FieldContext> map;
}
