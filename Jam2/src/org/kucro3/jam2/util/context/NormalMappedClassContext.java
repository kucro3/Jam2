package org.kucro3.jam2.util.context;

import java.util.Collection;

import org.kucro3.jam2.util.ClassMemberMap;
import org.kucro3.jam2.util.FieldContext;
import org.kucro3.jam2.util.MethodContext;

public abstract class NormalMappedClassContext extends NormalClassContext {
	public NormalMappedClassContext(String name) 
	{
		this(name, new ClassMemberMap<>());
	}
	
	public NormalMappedClassContext(String name, ClassMemberMap<MethodContext, FieldContext> map)
	{
		super(name);
		this.map = map;
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
	public Collection<FieldContext> getFields()
	{
		return map.byFields();
	}
	
	@Override
	public Collection<MethodContext> getMethods()
	{
		return map.byMethods();
	}
	
	protected final ClassMemberMap<MethodContext, FieldContext> map;
}
