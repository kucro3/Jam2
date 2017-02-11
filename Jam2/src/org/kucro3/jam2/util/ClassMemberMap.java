package org.kucro3.jam2.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.kucro3.util.exception.ImplicitThrows;

public class ClassMemberMap<_tMethodMapped, _tFieldMapped> {
	public ClassMemberMap()
	{
	}
	
	@ImplicitThrows(IllegalArgumentException.class)
	public void putByMethod(String fullDescriptor, _tMethodMapped value)
	{
		checkDuplicationForMethod(fullDescriptor);
		methodMapped.put(fullDescriptor, value);
	}
	
	@ImplicitThrows(IllegalArgumentException.class)
	public void putByMethod(String name, String descriptor, _tMethodMapped value)
	{
		putByMethod(name + descriptor, value);
	}
	
	@ImplicitThrows(IllegalArgumentException.class)
	public void putByMethod(String name, String returnType, String[] arguments, _tMethodMapped value)
	{
		putByMethod(Jam2Util.toDescriptor(name, returnType, arguments), value);
	}
	
	@ImplicitThrows(IllegalArgumentException.class)
	public void putByMethod(String name, Class<?> returnType, Class<?>[] arguments, _tMethodMapped value)
	{
		putByMethod(Jam2Util.toDescriptor(name, returnType, arguments), value);
	}
	
	@ImplicitThrows(IllegalArgumentException.class)
	public void putByField(String name, _tFieldMapped value)
	{
		checkDuplicationForField(name);
		fieldMapped.put(name, value);
	}
	
	public void overrideByMethod(String fullDescriptor, _tMethodMapped value)
	{
		methodMapped.put(fullDescriptor, value);
	}
	
	public void overrideByMethod(String name, String descriptor, _tMethodMapped value)
	{
		overrideByMethod(name + descriptor, value);
	}
	
	public void overrideByMethod(String name, String returnType, String[] arguments, _tMethodMapped value)
	{
		overrideByMethod(Jam2Util.toDescriptor(name, returnType, arguments), value);
	}
	
	public void overrideByMethod(String name, Class<?> returnType, Class<?>[] arguments, _tMethodMapped value)
	{
		overrideByMethod(Jam2Util.toDescriptor(name, returnType, arguments), value);
	}
	
	public void overrideByField(String name, _tFieldMapped value)
	{
		fieldMapped.put(name, value);
	}
	
	public _tMethodMapped removeByMethod(String fullDescriptor)
	{
		return methodMapped.remove(fullDescriptor);
	}
	
	public _tMethodMapped removeByMethod(String name, String descriptor)
	{
		return removeByMethod(name + descriptor);
	}
	
	public _tMethodMapped removeByMethod(String name, String returnType, String[] arguments)
	{
		return removeByMethod(Jam2Util.toDescriptor(name, returnType, arguments));
	}
	
	public _tMethodMapped removeByMethod(String name, Class<?> returnType, Class<?>[] arguments)
	{
		return removeByMethod(Jam2Util.toDescriptor(name, returnType, arguments));
	}
	
	public _tFieldMapped removeByField(String name)
	{
		return fieldMapped.remove(name);
	}
	
	public Collection<_tFieldMapped> byFields()
	{
		return Collections.unmodifiableCollection(fieldMapped.values());
	}
	
	public Collection<_tMethodMapped> byMethods()
	{
		return Collections.unmodifiableCollection(methodMapped.values());
	}
	
	public boolean mappedByField(String name)
	{
		return fieldMapped.containsKey(name);
	}
	
	public boolean mappedByMethod(String fullDescriptor)
	{
		return methodMapped.containsKey(fullDescriptor);
	}
	
	public _tMethodMapped getByMethod(String fullDescriptor)
	{
		return methodMapped.get(fullDescriptor);
	}
	
	public _tMethodMapped getByMethod(String name, String descriptor)
	{
		return getByMethod(name + descriptor);
	}
	
	public _tMethodMapped getByMethod(String name, String returnType, String[] arguments)
	{
		return getByMethod(Jam2Util.toDescriptor(name, returnType, arguments));
	}
	
	public _tMethodMapped getByMethod(String name, Class<?> returnType, Class<?>[] arguments)
	{
		return getByMethod(Jam2Util.toDescriptor(name, returnType, arguments));
	}
	
	public _tFieldMapped getByField(String name)
	{
		return fieldMapped.get(name);
	}
	
	public void clearAll()
	{
		clearFieldMapped();
		clearMethodMapped();
	}
	
	public void clearFieldMapped()
	{
		fieldMapped.clear();
	}
	
	public void clearMethodMapped()
	{
		methodMapped.clear();
	}
	
	public boolean hasField()
	{
		return fieldMapped.isEmpty();
	}
	
	public boolean hasMethod()
	{
		return methodMapped.isEmpty();
	}
	
	@ImplicitThrows(IllegalArgumentException.class) 
	static void checkDuplication(Map<String, ?> map, String key, String msg) 
	{
		if(map.containsKey(key))
			throw new IllegalArgumentException(String.format(msg, key));
	}
	
	@ImplicitThrows(IllegalArgumentException.class)
	protected final void checkDuplicationForMethod(String key)
	{
		checkDuplication(methodMapped, key, "Method duplicated: %s");
	}
	
	@ImplicitThrows(IllegalArgumentException.class) 
	protected final void checkDuplicationForField(String key)
	{
		checkDuplication(fieldMapped, key, "Field duplicated: %s");
	}
	
	private final Map<String, _tMethodMapped> methodMapped = new HashMap<>();
	
	private final Map<String, _tFieldMapped> fieldMapped = new HashMap<>();
}