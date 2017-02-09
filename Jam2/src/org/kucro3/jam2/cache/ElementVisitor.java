package org.kucro3.jam2.cache;

import org.kucro3.jam2.visitor.cache.ClassCacheVisitor;

public class ElementVisitor extends ClassCacheVisitor {
	ElementVisitor(ElementClass owner)
	{
		this.owner = owner;
	}
	
	@Override
	public FieldContainer newField(int access, String name, String desc, String signature, Object value)
	{
		return _putField(super.newField(access, name, desc, signature, value));
	}
	
	@Override
	public MethodContainer newMethod(int access, String name, String desc, String signature, String[] exceptions)
	{
		return _putMethod(super.newMethod(access, name, desc, signature, exceptions));
	}
	
	MethodContainer _putMethod(MethodContainer mc)
	{
		owner.putMethod(mc);
		return mc;
	}
	
	FieldContainer _putField(FieldContainer fc)
	{
		owner.putField(fc);
		return fc;
	}
	
	private final ElementClass owner;
}