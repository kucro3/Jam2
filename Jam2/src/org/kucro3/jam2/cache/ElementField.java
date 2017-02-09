package org.kucro3.jam2.cache;

import org.kucro3.jam2.visitor.cache.ContainerHelper;
import org.kucro3.jam2.util.annotation.AnnotationContainer;
import org.kucro3.jam2.visitor.cache.ClassCacheVisitor.FieldContainer;

public class ElementField {
	ElementField(ElementClass owner, FieldContainer field)
	{
		this.owner = owner;
		this.field = field;
	}
	
	public int getAccess()
	{
		return field.access;
	}
	
	public void setAccess(int access)
	{
		field.access = access;
	}
	
	public String getDescriptor()
	{
		return field.desc;
	}
	
	public void setDescriptor(String desc)
	{
		field.desc = desc;
	}
	
	public String getSignature()
	{
		return field.signature;
	}
	
	public void setSignature(String signature)
	{
		field.signature = signature;
	}
	
	public Object getValue()
	{
		return field.value;
	}
	
	public void setValue(Object value)
	{
		field.value = value;
	}
	
	FieldContainer confirmRenaming(String name)
	{
		FieldContainer _new = owner.ccv.new FieldContainer(name),
					   _old = field;
		ContainerHelper.copyTo(_old, _new);
		this.field = _new;
		return _old;
	}
	
	public final ElementClass getOwner()
	{
		return owner;
	}
	
	public AnnotationContainer getAnnotations()
	{
		return field.fcv.getAnnotationContainer();
	}
	
	private FieldContainer field;
	
	private final ElementClass owner;
}
