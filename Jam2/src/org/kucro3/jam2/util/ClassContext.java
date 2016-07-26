package org.kucro3.jam2.util;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import static org.kucro3.jam2.util.Jam2Util.*;

public class ClassContext extends ClassVisitor
{
	public ClassContext(int version, int access, String name, String signature, String superName, String[] interfaces)
	{
		this(ClassWriter.COMPUTE_FRAMES, version, access, name, signature, superName, interfaces);
	}
	
	public ClassContext(int flags, int version, int access, String name, String signature, String superName, String[] interfaces)
	{
		super(flags, new ClassWriter(0));
		this.version = version;
		this.access = access;
		this.internalName = name;
		this.signature = signature;
		this.superClass = superName;
		this.interfaces = interfaces;
	}
	
	public MethodContext addMethod(int modifier, String name, Class<?> returnType, Class<?>[] arguments, Class<?>[] throwings)
	{
		return addMethod(MethodContext.newContext(internalName, modifier, name, returnType, arguments, throwings));
	}
	
	public MethodContext addMethod(int modifier, String name, String returnType, String[] arguments, String[] throwings)
	{
		return addMethod(MethodContext.newContext(internalName, modifier, name, returnType, arguments, throwings));
	}
	
	public MethodContext addMethod(MethodContext ctx)
	{
		String key;
		this.checkDuplicationForMethod(key = toMethodKey(ctx));
		mappedMethods.put(key, ctx);
		ctx.bind(this, superBridge);
		return ctx;
	}
	
	public MethodContext addMethodCopy(MethodContext ctx)
	{
		return addMethod(ctx.copyAsMethod());
	}
	
	public ConstructorContext addConstructor(int modifier, Class<?>[] arguments, Class<?>[] throwings)
	{
		return addConstructor(ConstructorContext.newContext(internalName, modifier, arguments, throwings));
	}
	
	public ConstructorContext addConstructor(int modifier, String[] arguments, String[] throwings)
	{
		return addConstructor(ConstructorContext.newContext(internalName, modifier, arguments, throwings));
	}
	
	public ConstructorContext addConstructor(ConstructorContext ctx)
	{
		String key;
		this.checkDuplicationForConstructor(key = toConstructorKey(ctx));
		mappedMethods.put(key, ctx);
		ctx.bind(this, superBridge);
		return ctx;
	}
	
	public ConstructorContext addConstructorCopy(ConstructorContext ctx)
	{
		return addConstructorCopy(ctx.copyAsConstructor());
	}
	
	public FieldContext addField(int modifier, String name, Class<?> type)
	{
		return addField(FieldContext.newContext(internalName, modifier, name, type));
	}
	
	public FieldContext addField(int modifier, String name, String type)
	{
		return addField(FieldContext.newContext(internalName, modifier, name, type));
	}
	
	public FieldContext addField(FieldContext ctx)
	{
		String key;
		this.checkDuplicationForField(key = toFieldKey(ctx));
		mappedFields.put(key, ctx);
		ctx.bind(this, superBridge);
		return ctx;
	}
	
	public FieldContext addFieldCopy(FieldContext ctx)
	{
		return addField(ctx.copyAsField());
	}
	
	public MethodContext getMethod(String name, Class<?> returnType, Class<?>... arguments)
	{
		return getMethod(name, Type.getDescriptor(returnType), _toDescriptors(arguments));
	}
	
	public MethodContext getMethod(String name, String returnType, String... arguments)
	{
		return getMethod(toMethodKey(name, returnType, arguments));
	}
	
	public ConstructorContext getConstructor(Class<?>... arguments)
	{
		return getConstructor(_toDescriptors(arguments));
	}
	
	public ConstructorContext getConstructor(String... arguments)
	{
		return getConstructor(toConstructorKey(arguments));
	}
	
	MethodContext getMethod(String signature)
	{
		return mappedMethods.get(signature);
	}
	
	public FieldContext getField(String name)
	{
		return mappedFields.get(toFieldKey(name));
	}
	
	ConstructorContext getConstructor(String signature)
	{
		try {
			return (ConstructorContext) mappedMethods.get(signature);
		} catch (ClassCastException e) {
			return null;
		}
	}
	
	static void checkDuplication(Map<String, ?> map, String key, String msg)
	{
		if(map.containsKey(key))
			throw new IllegalArgumentException(String.format(msg, key));
	}
	
	final void checkDuplicationForMethod(String key)
	{
		checkDuplication(mappedMethods, key, "Method duplicated: %s");
	}
	
	final void checkDuplicationForField(String key)
	{
		checkDuplication(mappedFields, key, "Field duplicated: %s");
	}
	
	final void checkDuplicationForConstructor(String key)
	{
		checkDuplication(mappedMethods, key, "Constructor duplicated: %s");
	}
	
	static String toConstructorKey(ConstructorContext ctx)
	{
		return toMethodKey(ctx);
	}
	
	static String toConstructorKey(String[] arguments)
	{
		return "<init>" + _toConstructorDescriptor(arguments);
	}
	
	static String toMethodKey(MethodContext ctx)
	{
		return toMethodKey(ctx.getMethodName(), ctx.getReturnTypeDescriptor(), ctx.getArgumentDescriptors());
	}
	
	static String toMethodKey(String name, String returnType, String[] arguments)
	{
		return name + _toDescriptor(returnType, arguments);
	}
	
	static String toFieldKey(FieldContext ctx)
	{
		return toFieldKey(ctx.getFieldName());
	}
	
	static String toFieldKey(String name)
	{
		return name;
	}
	
	public int getAccess()
	{
		return access;
	}
	
	public String getSignature()
	{
		return signature;
	}
	
	public String getSuperClass()
	{
		return superClass;
	}
	
	public String[] getInterfaces()
	{
		return interfaces;
	}
	
	public int getVersion()
	{
		return version;
	}
	
	public byte[] toByteArray()
	{
		return ((ClassWriter)super.cv).toByteArray();
	}
	
	@Override
	public FieldContext visitField(int access, String name, String desc, String signature, Object value)
	{
		return addField(access, name, desc);
	}
	
	@Override
	public MethodContext visitMethod(int access, String name, String desc, String signature, String[] exceptions)
	{
		return addMethod(MethodContext.newContext(internalName, access, name, desc, exceptions));
	}
	
	final FieldVisitor _super_visitField(int access, String name, String desc, String signature, Object value)
	{
		return super.visitField(access, name, desc, signature, value);
	}
	
	final MethodVisitor _super_visitMethod(int access, String name, String desc, String signature, String[] exceptions)
	{
		return super.visitMethod(access, name, desc, signature, exceptions);
	}
	
	private final String signature;
	
	private final String superClass;
	
	private final String[] interfaces;
	
	private final int version;
	
	private final String internalName;
	
	private final int access;
	
	private final Map<String, MethodContext> mappedMethods = new HashMap<>();
	
	private final Map<String, FieldContext> mappedFields = new HashMap<>();
	
	private final ClassVisitor superBridge = new ClassContextSuperBridge();
	
	class ClassContextSuperBridge extends ClassVisitor
	{
		ClassContextSuperBridge()
		{
			super(0, ClassContext.this);
		}
		
		@Override
		public FieldVisitor visitField(int access, String name, String desc, String signature, Object value)
		{
			return _super_visitField(access, name, desc, signature, value);
		}
		
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signture, String[] exceptions)
		{
			return _super_visitMethod(access, name, desc, signature, exceptions);
		}
	}
}