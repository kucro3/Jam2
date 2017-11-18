package org.kucro3.jam2.util.context;

import org.kucro3.jam2.util.ClassMemberMap;
import org.kucro3.jam2.util.FieldContext;
import org.kucro3.jam2.util.MethodContext;

public class ConstantClassContext extends NormalMappedClassContext {
	public ConstantClassContext(String name)
	{
		super(name);
	}
	
	public ConstantClassContext(String name, ClassMemberMap<MethodContext, FieldContext> map)
	{
		super(name, map);
	}
	
	public ConstantClassContext(int version, int modifier, String name, String signature,
			String superName, String[] interfaces)
	{
		this(version, modifier, name, signature, superName, interfaces, null);
	}
	
	public ConstantClassContext(int version, int modifier, String name, String signature,
			String superName, String[] interfaces,
			String enclosingClass)
	{
		this(version, modifier, name, signature, superName, interfaces, enclosingClass, null, null);
	}
	
	public ConstantClassContext(int version, int modifier, String name, String signature,
			String superName, String[] interfaces, 
			String enclosingClass,
			String enclosingMethodName, String enclosingMethodDescriptor)
	{
		this(version, modifier, name, signature, superName, interfaces, enclosingClass, 
				enclosingMethodName, enclosingMethodDescriptor, null, null);
	}
	
	public ConstantClassContext(int version, int modifier, String name, String signature,
			String superName, String[] interfaces, 
			String enclosingClass,
			String enclosingMethodName, String enclosingMethodDescriptor,
			String source, String debug)
	{
		super(name);
		this.version = version;
		this.modifier = modifier;
		this.signature = signature;
		this.superName = superName;
		this.interfaces = interfaces;
		this.enclosingClass = enclosingClass;
		this.enclosingMethodName = enclosingMethodName;
		this.enclosingMethodDescriptor = enclosingMethodDescriptor;
		this.source = source;
		this.debug = debug;
	}

//	@Override
//	public FieldContext newField(int modifier, String name, String descriptor, String signature,
//			Object value)
//	{
//		ConstantFieldContext fc = new ConstantFieldContext(
//				getName(), modifier, name, descriptor, signature, value);
//		map.putByField(name, fc);
//		return fc;
//	}
//
//	@Override
//	public MethodContext newMethod(int modifier, String name, String descriptor, String signature,
//			String[] exceptions)
//	{
//		ConstantMethodContext mc = new ConstantMethodContext(
//				getName(), modifier, name, descriptor, signature, exceptions);
//		map.putByMethod(name, descriptor, mc);
//		return mc;
//	}
}