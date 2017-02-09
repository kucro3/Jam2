package org.kucro3.jam2.cache;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.kucro3.jam2.util.Jam2Util;
import org.kucro3.jam2.visitor.cache.ClassCacheVisitor.FieldContainer;
import org.kucro3.jam2.visitor.cache.ClassCacheVisitor.MethodContainer;
import org.kucro3.util.exception.ImplicitThrows;

public class ElementClass {
	public ElementClass()
	{
	}
	
	@ImplicitThrows(ElementDuplicatedException.class)
	public ElementField putField(FieldContainer fc)
	{
		ElementField ef = new ElementField(this, fc);
		if(fields.putIfAbsent(fc.name, ef) != null)
			throw elementDuplicated("field", fc.name);
		return ef;
	}
	
	@ImplicitThrows(ElementDuplicatedException.class)
	public ElementMethod putMethod(MethodContainer mc)
	{
		ElementMethod em = new ElementMethod(this, mc);
		if(methods.putIfAbsent(mc.fullDesc, em) != null)
			throw elementDuplicated("method", mc.fullDesc);
		return em;
	}
	
	public boolean containsField(String name)
	{
		return fields.containsKey(name);
	}
	
	public boolean containsMethod(String descriptor)
	{
		return methods.containsKey(descriptor);
	}
	
	public boolean containsMethod(String name, String returnType, String[] arguments)
	{
		return containsMethod(Jam2Util.toDescriptor(name, returnType, arguments));
	}
	
	public boolean containsMethod(String name, Class<?> returnType, Class<?>[] arguments)
	{
		return containsMethod(Jam2Util.toDescriptor(name, returnType, arguments));
	}
	
	public boolean removeField(String name)
	{
		return fields.remove(name) != null;
	}
	
	public boolean removeMethod(String descriptor)
	{
		return methods.remove(descriptor) != null;
	}
	
	public boolean removeMethod(String name, String returnType, String[] arguments)
	{
		return removeMethod(Jam2Util.toDescriptor(name, returnType, arguments));
	}
	
	public boolean removeMethod(String name, Class<?> returnType, Class<?>[] arguments)
	{
		return removeMethod(Jam2Util.toDescriptor(name, returnType, arguments));
	}
	
	public Collection<ElementField> getFields()
	{
		return Collections.unmodifiableCollection(fields.values());
	}
	
	public Collection<ElementMethod> getMethods()
	{
		return Collections.unmodifiableCollection(methods.values());
	}
	
	public ElementVisitor getVisitor()
	{
		return ccv;
	}
	
	static ElementDuplicatedException elementDuplicated(String type, String name)
	{
		return new ElementDuplicatedException("Duplicated " + type + ": " + name);
	}
	
	private final Map<String, ElementField> fields = new HashMap<>();
	
	private final Map<String, ElementMethod> methods = new HashMap<>();
	
	final ElementVisitor ccv = new ElementVisitor(this);
}