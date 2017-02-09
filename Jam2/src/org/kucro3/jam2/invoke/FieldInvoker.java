package org.kucro3.jam2.invoke;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.kucro3.jam2.invoke.FieldInvokerLambdaImpl.LambdaGet;
import org.kucro3.jam2.invoke.FieldInvokerLambdaImpl.LambdaSet;
import org.kucro3.jam2.util.FieldContext;
import org.kucro3.jam2.util.Jam2Util;
import org.kucro3.jam2.util.Contexts;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

public abstract class FieldInvoker implements Opcodes {
	protected FieldInvoker(Class<?> declaringClass, int modifier, String name, Class<?> type)
	{
		this.declaringClass = declaringClass;
		this.modifier = modifier;
		this.name = name;
		this.type = type;
	}
	
	public static FieldInvoker newInvokerByLambda(Field field)
	{
		if(!Modifier.isPublic(field.getModifiers()))
			throw new IllegalArgumentException("field unaccessable");
		
		FieldContext fctx = Contexts.newFieldConstant(field);
		
		LambdaGet get;
		LambdaSet set;
		
		String getterName;
		String setterName;
		
		ClassWriter getter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		getter.visit(V1_8,
				ACC_PUBLIC,
				getterName = "org/kucro3/jam2/invoke/FieldGetter$" + Jam2Util.generateUUIDForClassName(),
				null,
				"java/lang/Object",
				new String[] {"org/kucro3/jam2/invoke/FieldInvokerLambdaImpl$LambdaGet"});
		Jam2Util.pushFieldGetter(getter, ACC_PUBLIC, "get", fctx, true, true);
		Jam2Util.pushEmptyConstructor(getter, ACC_PUBLIC, Object.class);
		getter.visitEnd();
		
		ClassWriter setter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		setter.visit(V1_8, 
				ACC_PUBLIC, 
				setterName = "org/kucro3/jam2/invoke/FieldSetter$" + Jam2Util.generateUUIDForClassName(),
				null, 
				"java/lang/Object",
				new String[] {"org/kucro3/jam2/invoke/FieldInvokerLambdaImpl$LambdaSet"});
		Jam2Util.pushFieldSetter(setter, ACC_PUBLIC, "set", fctx, true);
		Jam2Util.pushEmptyConstructor(setter, ACC_PUBLIC, Object.class);
		setter.visitEnd();
		
		try {
			get = (LambdaGet) Jam2Util.newClass(Jam2Util.fromInternalNameToCanonical(getterName), getter).newInstance();
			set = (LambdaSet) Jam2Util.newClass(Jam2Util.fromInternalNameToCanonical(setterName), setter).newInstance();
		} catch (Exception e) {
			// unused
			throw new IllegalStateException(e);
		}
		
		return new FieldInvokerLambdaImpl(field.getDeclaringClass(), field.getModifiers(),
				field.getName(), field.getType(), get, set);
	}
	
	public Class<?> getDeclaringClass()
	{
		return declaringClass;
	}
	
	public int getModifier()
	{
		return modifier;
	}
	
	public String getName()
	{
		return name;
	}
	
	public Class<?> getType()
	{
		return type;
	}
	
	public abstract void set(Object obj, Object ref);
	
	public abstract Object get(Object obj);
	
	final Class<?> declaringClass;
	
	final int modifier;
	
	final String name;
	
	final Class<?> type;
}
