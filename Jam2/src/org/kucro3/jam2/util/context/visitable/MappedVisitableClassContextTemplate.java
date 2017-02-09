package org.kucro3.jam2.util.context.visitable;

import org.objectweb.asm.ClassVisitor;

abstract class MappedVisitableClassContextTemplate extends MappedVisitableClassContext {
	MappedVisitableClassContextTemplate()
	{
		super();
	}
	
	MappedVisitableClassContextTemplate(ClassVisitor cv)
	{
		super(cv);
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
		super.map.clearFieldMapped();
	}

	@Override
	public void removeField(String name) 
	{
		super.map.removeByField(name);
	}

	@Override
	public void clearMethods()
	{
		super.map.clearMethodMapped();
	}

	@Override
	public void removeMethod(String fullDescriptor)
	{
		super.map.removeByMethod(fullDescriptor);
	}
}