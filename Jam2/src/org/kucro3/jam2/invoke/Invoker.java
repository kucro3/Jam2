package org.kucro3.jam2.invoke;

import org.kucro3.jam2.invoke.FieldInvokerLambdaImpl.LambdaGet;
import org.kucro3.jam2.invoke.FieldInvokerLambdaImpl.LambdaSet;
import org.kucro3.jam2.util.ClassContext;
import org.kucro3.jam2.util.ConstructorContext;
import org.kucro3.jam2.util.FieldContext;
import org.kucro3.jam2.util.Jam2Util;
import org.kucro3.jam2.util.LambdaContext;
import org.kucro3.jam2.util.MethodContext;
import org.kucro3.jam2.util.Jam2Util.CallingType;
import org.objectweb.asm.Opcodes;

import java.util.HashMap;
import java.util.Map;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Constructor;

@SuppressWarnings("rawtypes")
public class Invoker implements Opcodes {
	private Invoker(Class<?> owner)
	{
		this.owner = owner;
		this._init();
	}
	
	public static Invoker newInvoker(Class<?> clz)
	{
		return new Invoker(clz);
	}
	
	public Class<?> getOwner()
	{
		return owner;
	}
	
	private void _init()
	{
		Class<?> clz = owner;
		String initClassName = parseInitializerName(this);
		
		BLOCK: {
			Initializer initializer;
			
			if((initializer = instances.get(owner)) == null)
				break BLOCK;
			
			this.initClass = initializer.getClass();
			initializer.initializeConstructors(this, owner.getConstructors());
			initializer.initializeMethods(this, owner.getMethods());
			initializer.initializeFields(this, owner.getFields());
			return;
		}

		ClassContext initClassCtx = 
				new ClassContext(V1_8, ACC_PUBLIC, initClassName, null, "org/kucro3/jam2/invoke/Initializer", null);
		
		MethodContext mInitFields 
				= initClassCtx.addMethod(ACC_PUBLIC, "initializeFields", void.class, new Class<?>[] {Invoker.class, Field[].class}, null);
		MethodContext mInitMethods
				= initClassCtx.addMethod(ACC_PUBLIC, "initializeMethods", void.class, new Class<?>[] {Invoker.class, Method[].class}, null);
		MethodContext mInitConstructors
				= initClassCtx.addMethod(ACC_PUBLIC, "initializeConstructors", void.class, new Class<?>[] {Invoker.class, Constructor[].class}, null);
		MethodContext mInitConstructor
				= initClassCtx.addConstructor(ACC_PUBLIC, new Class<?>[] {}, null);
		MethodContext mInitInstance
				= initClassCtx.addMethod(ACC_PUBLIC + ACC_STATIC, "getInstance", Initializer.class, new Class<?>[] {}, null);
		FieldContext fInstance
				= initClassCtx.addField(ACC_PRIVATE + ACC_STATIC, "instance", Initializer.class);
		
		mInitConstructor.visitCode();
		mInitConstructor.visitVarInsn(ALOAD, 0);
		mInitConstructor.visitInsn(DUP);
		mInitConstructor.visitMethodInsn(INVOKESPECIAL, "org/kucro3/jam2/invoke/Initializer", "<init>", "()V", false);
		mInitConstructor.visitFieldInsn(PUTSTATIC, initClassName, fInstance.getFieldName(), fInstance.getDescriptor());
		mInitConstructor.visitInsn(RETURN);
		mInitConstructor.visitMaxs(0, 0);
		mInitConstructor.visitEnd();
		
		mInitInstance.visitCode();
		mInitInstance.visitFieldInsn(GETSTATIC, initClassName, fInstance.getFieldName(), fInstance.getDescriptor());
		mInitInstance.visitInsn(ARETURN);
		mInitInstance.visitMaxs(0, 0);
		mInitInstance.visitEnd();
		
		Field[] fields = clz.getFields();
		mInitFields.visitCode();
		for(int i = 0; i < fields.length; i++)
		{
			Field field = fields[i];
			FieldContext fCtx = FieldContext.newContext(field);
			
			mInitFields.visitVarInsn(ALOAD, 1);
			mInitFields.visitVarInsn(ALOAD, 2);
			mInitFields.visitIntInsn(SIPUSH, i);
			mInitFields.visitInsn(AALOAD);
			
			LambdaContext lCtx;
			
			Jam2Util.pushFunctionalLambda(initClassCtx, initClassCtx.getInternalName(), mInitFields,
					lCtx = newFieldGetterContext(), true);
			Jam2Util.pushFieldGetter(initClassCtx,  ACC_STATIC | ACC_PRIVATE | ACC_SYNTHETIC, lCtx.getInternalLambdaName(),
					fCtx, true, true);
			Jam2Util.pushFunctionalLambda(initClassCtx, initClassCtx.getInternalName(), mInitFields,
					lCtx = newFieldSetterContext(), true);
			Jam2Util.pushFieldSetter(initClassCtx,  ACC_STATIC | ACC_PRIVATE | ACC_SYNTHETIC, lCtx.getInternalLambdaName(),
					fCtx, true);
			
			mInitFields.visitMethodInsn(INVOKESTATIC, "org/kucro3/jam2/invoke/Initializer", "createInvoker",
					"("
					+ "Lorg/kucro3/jam2/invoke/Invoker;"
					+ "Ljava/lang/reflect/Field;"
					+ "Lorg/kucro3/jam2/invoke/FieldInvokerLambdaImpl$LambdaGet;"
					+ "Lorg/kucro3/jam2/invoke/FieldInvokerLambdaImpl$LambdaSet;"
					+ ")V", false);
		}
		mInitFields.visitInsn(RETURN);
		mInitFields.visitMaxs(0, 0);
		mInitFields.visitEnd();
		
		Method[] methods = clz.getMethods();
		mInitMethods.visitCode();
		for(int i = 0; i < methods.length; i++)
		{
			Method method = methods[i];
			MethodContext mCtx = MethodContext.newContext(method);
			
			mInitMethods.visitVarInsn(ALOAD, 1);
			mInitMethods.visitVarInsn(ALOAD, 2);
			mInitMethods.visitIntInsn(SIPUSH, i);
			mInitMethods.visitInsn(AALOAD);
			
			LambdaContext lCtx;
			
			Jam2Util.pushFunctionalLambda(initClassCtx, initClassCtx.getInternalName(), mInitMethods, 
					lCtx = newMethodInvokerContext(), true);
			Jam2Util.pushCaller(initClassCtx, ACC_STATIC | ACC_PRIVATE | ACC_SYNTHETIC, lCtx.getInternalLambdaName(),
					mCtx, Modifier.isStatic(mCtx.getModifier()) ? CallingType.STATIC : CallingType.VIRTUAL, true, true);
			
			mInitMethods.visitMethodInsn(INVOKESTATIC, "org/kucro3/jam2/invoke/Initializer", "createInvoker", 
					"("
					+ "Lorg/kucro3/jam2/invoke/Invoker;"
					+ "Ljava/lang/reflect/Method;"
					+ "Lorg/kucro3/jam2/invoke/MethodInvokerLambdaImpl$LambdaInvocation;"
					+ ")V", false);
		}
		mInitMethods.visitInsn(RETURN);
		mInitMethods.visitMaxs(0, 0);
		mInitMethods.visitEnd();
		
		Constructor[] constructors = clz.getConstructors();
		mInitConstructors.visitCode();
		for(int i = 0; i < constructors.length; i++)
		{
			Constructor constructor = constructors[i];
			ConstructorContext cCtx = ConstructorContext.newContext(constructor);
			
			mInitConstructors.visitVarInsn(ALOAD, 1);
			mInitConstructors.visitVarInsn(ALOAD, 2);
			mInitConstructors.visitIntInsn(SIPUSH, i);
			mInitConstructors.visitInsn(AALOAD);
			
			LambdaContext lCtx;
			
			Jam2Util.pushFunctionalLambda(initClassCtx, initClassCtx.getInternalName(), mInitConstructors,
					lCtx = newConstructorInvokerContext(), true);
			Jam2Util.pushNewInstance(initClassCtx, ACC_STATIC | ACC_PRIVATE | ACC_SYNTHETIC, lCtx.getInternalLambdaName(),
					cCtx, true, true);
			
			mInitConstructors.visitMethodInsn(INVOKESTATIC, "org/kucro3/jam2/invoke/Initializer", "createInvoker",
					"("
					+ "Lorg/kucro3/jam2/invoke/Invoker;"
					+ "Ljava/lang/reflect/Constructor;"
					+ "Lorg/kucro3/jam2/invoke/ConstructorInvokerLambdaImpl$LambdaInvocation;"
					+ ")V", false);
		}
		mInitConstructors.visitInsn(RETURN);
		mInitConstructors.visitMaxs(0, 0);
		mInitConstructors.visitEnd();
		
		this.initClass = initClassCtx.newClass();
		
		try {
			Initializer initializer = (Initializer) initClass.newInstance();
			
			initializer.initializeConstructors(this, constructors);
			initializer.initializeMethods(this, methods);			
			initializer.initializeFields(this, fields);
			
			instances.put(owner, initializer);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
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
	
	void putConstructor(ConstructorInvoker invoker)
	{
		constructors.put(invoker.getDescriptor(), invoker);
	}
	
	static FieldInvoker createInvoker(Field field, LambdaGet getter, LambdaSet setter)
	{
		return new FieldInvokerLambdaImpl(field.getDeclaringClass(), field.getModifiers(),
				field.getName(), field.getType(), getter, setter);
	}
	
	static MethodInvoker createInvoker(Method method, 
			org.kucro3.jam2.invoke.MethodInvokerLambdaImpl.LambdaInvocation invocation)
	{
		return new MethodInvokerLambdaImpl(method.getDeclaringClass(), method.getModifiers(),
				method.getName(), method.getReturnType(), method.getParameterTypes(), invocation);
	}
	
	static ConstructorInvoker createInvoker(Constructor constructor,
			org.kucro3.jam2.invoke.ConstructorInvokerLambdaImpl.LambdaInvocation invocation)
	{
		return new ConstructorInvokerLambdaImpl(constructor.getDeclaringClass(), constructor.getModifiers(),
				constructor.getParameterTypes(), invocation);
	}
	
	private static LambdaContext newFieldGetterContext()
	{
		return LambdaContext.newContext(LambdaGet.class, "get", Object.class, new Class<?>[] {Object.class});
	}
	
	private static LambdaContext newFieldSetterContext()
	{
		return LambdaContext.newContext(LambdaSet.class, "set", void.class, new Class<?>[] {Object.class, Object.class});
	}
	
	private static LambdaContext newMethodInvokerContext()
	{
		return LambdaContext.newContext(org.kucro3.jam2.invoke.MethodInvokerLambdaImpl.LambdaInvocation.class,
				"invoke", Object.class, new Class<?>[] {Object.class, Object[].class});
	}
	
	private static LambdaContext newConstructorInvokerContext()
	{
		return LambdaContext.newContext(org.kucro3.jam2.invoke.ConstructorInvokerLambdaImpl.LambdaInvocation.class,
				"newInstance", Object.class, new Class<?>[] {Object[].class});
	}
	
	private static String parseInitializerName(Invoker invoker)
	{
		return new StringBuilder()
				.append("Initializer_")
				.append(invoker.owner.getCanonicalName().replace(".", "_"))
				.append("$")
				.append(Jam2Util.generateUUIDForClassName())
				.toString();
	}
	
	private final Map<String, ConstructorInvoker> constructors = new HashMap<>();
	
	private final Map<String, MethodInvoker> methods = new HashMap<>();
	
	private final Map<String, FieldInvoker> fields = new HashMap<>();
	
	private final Class<?> owner;
	
	Class<?> initClass;
	
	private static final Map<Class<?>, Initializer> instances = new HashMap<>();
}