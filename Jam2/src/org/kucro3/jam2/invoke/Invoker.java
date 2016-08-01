package org.kucro3.jam2.invoke;

import java.util.Map;

import org.kucro3.jam2.invoke.FieldInvokerLambdaImpl.LambdaGet;
import org.kucro3.jam2.invoke.FieldInvokerLambdaImpl.LambdaSet;
import org.kucro3.jam2.util.ClassContext;
import org.kucro3.jam2.util.FieldContext;
import org.kucro3.jam2.util.Jam2Util;
import org.kucro3.jam2.util.LambdaContext;
import org.kucro3.jam2.util.MethodContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.HashMap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;

public class Invoker implements Opcodes {
	private Invoker(Class<?> owner)
	{
		this.owner = owner;
		this.init();
	}
	
	public static Invoker newInvoker(Class<?> clz)
	{
		return new Invoker(clz);
	}
	
	public Class<?> getOwner()
	{
		return owner;
	}
	
	private void init()
	{
		Class<?> clz = owner;
		String initClassName = parseName(this);
		try {
			if((this.initClass = Class.forName(initClassName)) != null)
				return;
		} catch (ClassNotFoundException e) {}
		ClassContext initClassCtx = 
				new ClassContext(V1_8, ACC_PUBLIC, initClassName, null, "java/lang/Object", null);
		
		MethodContext mInitializeFields 
				= initClassCtx.addMethod(modifier, name, returnType, arguments, null);
		
		String internalName = Type.getInternalName(clz);
		Method[] methods = clz.getMethods();
		MethodContext mctx;
		for(Method method : methods)
		{
			
		}
		
		Field[] fields = clz.getFields();
		FieldContext fctx;
		LambdaContext getterCtx, setterCtx;
		for(Field field : fields)
		{
			fctx = FieldContext.newContext(field);
			getterCtx = newFieldGetterContext();
			setterCtx = newFieldSetterContext();
			Jam2Util.pushFunctionalLambda();
			
		}
		
		this.initClass = initClassCtx.newClass();
	}
	
	public MethodInvoker getMethod(String name, Class<?> returnType, Class<?>[] arguments)
	{
		return methods.get(MethodInvoker.toDescriptor(name, returnType, arguments));
	}
	
	public FieldInvoker getField(String name)
	{
		return fields.get(name);
	}
	
	void putMethod(MethodInvoker invoker)
	{
		methods.put(invoker.getDescriptor(), invoker);
	}
	
	void putField(FieldInvoker invoker)
	{
		fields.put(invoker.getName(), invoker);
	}
	
	static FieldInvoker createInvoker(Field field, LambdaGet getter, LambdaSet setter)
	{
		return new FieldInvokerLambdaImpl(field.getDeclaringClass(), field.getModifiers(),
				field.getName(), field.getType(), getter, setter);
	}
	
	private static LambdaContext newFieldGetterContext()
	{
		return LambdaContext.newContext(LambdaGet.class, "get", Object.class, new Class<?>[] {Object.class});
	}
	
	private static LambdaContext newFieldSetterContext()
	{
		return LambdaContext.newContext(LambdaSet.class, "set", void.class, new Class<?>[] {Object.class, Object.class});
	}
	
	private static String parseName(Invoker invoker)
	{
		return new StringBuilder()
				.append("Invoker_")
				.append(invoker.owner.getSimpleName().replace(".", "_"))
				.toString();
	}
	
	private final Map<String, MethodInvoker> methods = new HashMap<>();
	
	private final Map<String, FieldInvoker> fields = new HashMap<>();
	
	private final Class<?> owner;
	
	Class<?> initClass;
}