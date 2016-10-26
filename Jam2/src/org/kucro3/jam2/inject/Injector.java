package org.kucro3.jam2.inject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.kucro3.jam2.inject.Injection.ConstructorInjection;
import org.kucro3.jam2.inject.Injection.FieldGetterInjection;
import org.kucro3.jam2.inject.Injection.FieldSetterInjection;
import org.kucro3.jam2.inject.Injection.MethodInjection;
import org.kucro3.jam2.util.Jam2Util;
import org.kucro3.jam2.util.LambdaContext;
import org.kucro3.jam2.util.builder.ClassBuilder;
import org.kucro3.jam2.util.builder.MethodBuilder;
import org.kucro3.jam2.util.builder.MethodBuilder.MethodCodeBuilder;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import static org.kucro3.jam2.util.Jam2Util.toDescriptor;
import static org.kucro3.jam2.util.Jam2Util.toConstructorDescriptor;

public abstract class Injector implements Opcodes {
	protected Injector()
	{
	}
	
	public static Injection getInjection(Class<?> clz) throws UnsatisfiedInjectionException
	{
		Map.Entry<?, Method> current = null;
		
		PROC: {
			ClassBuilder cb = newInjectionClass(clz);
			MethodBuilder mb = newInjectionInitializer(cb);
			MethodCodeBuilder mcb = mb.code();
			String internalName = cb.getContext().getInternalName();
			
			Jam2Util.pushEmptyConstructor(cb.getContext(), Opcodes.ACC_PUBLIC, Injector.class);
			
			Method[] mthds = clz.getDeclaredMethods();
			Constructor<?>[] constructors = clz.getDeclaredConstructors();
			
			Map<Integer, Method> cachedMethods = new HashMap<>();
			Map<Integer, Constructor<?>> cachedConstructors = new HashMap<>();
			
			Map<Integer, Method> mInjections = new HashMap<>();
			Map<String, Method> fInjections = new HashMap<>();
			
			String tmpName;
			Integer tmpHash;
			for(Method mthd : mthds)
				if((tmpName = mthd.getName()).startsWith("<jam2_inject>"))
					if((tmpHash = getInjectionHash(tmpName)) == null) // field injection
						fInjections.put(tmpName, mthd);
					else
						mInjections.put(tmpHash, mthd);
				else
					cachedMethods.put(toDescriptor(mthd).hashCode(), mthd);
			
			for(Constructor<?> constructor : constructors)
				cachedConstructors.put(toConstructorDescriptor(constructor).hashCode(), constructor);
			
			mcb.aload_1();
			
			Class<?>[] injectionParams;
			
			for(Map.Entry<Integer, Method> eInjection : mInjections.entrySet())
				if(cachedMethods.get((current = eInjection).getKey()) == null) // may be constructor injection
					if((injectionParams = eInjection.getValue().getParameterTypes()).length > 0)
						if(injectionParams[0] == Object[].class) // constructor injection
							pushConstructorInjection(internalName, mcb, eInjection.getValue(), eInjection.getKey());
						else
							break PROC;
					else
						break PROC;
				else // may be method injection
					if((injectionParams = eInjection.getValue().getParameterTypes()).length == 2)
						if(injectionParams[0] == Object.class && injectionParams[0] == Object[].class) // method injection
							pushMethodInjection(internalName, mcb, eInjection.getValue(), eInjection.getKey());
						else
							break PROC;
					else
						break PROC;
			
			for(Map.Entry<String, Method> eInjection : fInjections.entrySet())
				if((injectionParams = eInjection.getValue().getParameterTypes()).length == 1) // may be getter
					if(injectionParams[0] == Object.class) // getter
						pushFieldGetterInjection(internalName, mcb, eInjection.getValue(), eInjection.getKey());
					else
						break PROC;
				else if(injectionParams.length == 2) // may be setter
					if(injectionParams[0] == Object.class && injectionParams[1] == Object.class) // setter
						pushFieldSetterInjection(internalName, mcb, eInjection.getValue(), eInjection.getKey());
					else
						break PROC;
				else
					break PROC;
			
			mcb
				.areturn()
				.end(true)
				.end();
			
			Injection injection = new Injection(clz);
			
			injection.initializeConstructors(cachedConstructors);
			injection.initializeMethods(cachedMethods);
			
			Class<?> injectorClass = cb.buildAsClass();
			Injector injector = null;
			
			try {
				injector = (Injector) injectorClass.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new IllegalStateException(e);
			}
			
			injector.initialize(injection);
			
			return injection.finish();
		}
		
		if(current != null)
			throw new UnsatisfiedInjectionException(current.getValue());
		throw new IllegalStateException("unprocessed");
	}
	
	static String getInjectionBridgeName(String name)
	{
		return name.substring(12);
	}
	
	static Integer getInjectionHash(String name)
	{
		int index = name.lastIndexOf('$');
		if(index != -1)
			return Integer.parseInt(name.substring(index + 1));
		return null;
	}
	
	private static MethodBuilder newInjectionInitializer(ClassBuilder cb)
	{
		return cb.appendMethod(ACC_PUBLIC,
				"initialize", "(" + Type.getDescriptor(Method[].class) + ")" + Type.getDescriptor(Injection.class),
				null, new String[] {UNSATISFIED_EXCEPTION_INTERNAL_NAME});
	}
	
	private static ClassBuilder newInjectionClass(Class<?> clz)
	{
		String clzName = newInjectionClassName(clz);
		ClassBuilder cb = ClassBuilder
				.builder()
					.version(V1_8)
					.access(ACC_PUBLIC)
					.name(clzName)
					.superclass(INJECTOR_INTERNAL_NAME)
				.build();
		Jam2Util.pushEmptyConstructor(cb.getContext(), ACC_PUBLIC, INJECTOR_INTERNAL_NAME);
		return cb;
	}
	
	private static String newInjectionClassName(Class<?> clz)
	{
		return new StringBuilder()
				.append("org/kucro3/jam2/inject/Injection$")
				.append(clz.getCanonicalName().replace(".", "_"))
				.append("$")
				.append(Jam2Util.generateUUIDForClassName())
				.toString();
	}
	
	private static final void pushFieldGetterInjection(String internalName, MethodCodeBuilder mcb, Method mthd, String name)
	{
		mcb.ldc(name);
		Jam2Util.pushFunctionStaticReference(mcb, internalName, mthd.getName(), LAMBDA_CONTEXT_FIELD_GETTER);
		mcb.invokevirtual(INJECTION_INTERNAL_NAME, "pushFieldGetter", DESCRIPTOR_PUSH_FIELD_GETTER);
	}
	
	private static final void pushFieldSetterInjection(String internalName, MethodCodeBuilder mcb, Method mthd, String name)
	{
		mcb.ldc(name);
		Jam2Util.pushFunctionStaticReference(mcb, internalName, mthd.getName(), LAMBDA_CONTEXT_FIELD_SETTER);
		mcb.invokevirtual(INJECTION_INTERNAL_NAME, "pushFieldSetter", DESCRIPTOR_PUSH_FIELD_SETTER);
	}
	
	private static final void pushMethodInjection(String internalName, MethodCodeBuilder mcb, Method mthd, Integer hash)
	{
		mcb.ldc(hash);
		Jam2Util.pushFunctionStaticReference(mcb, internalName, mthd.getName(), LAMBDA_CONTEXT_METHOD_INJECT);
		mcb.invokevirtual(INJECTION_INTERNAL_NAME, "pushMethod", DESCRIPTOR_PUSH_METHOD);
	}
	
	private static final void pushConstructorInjection(String internalName, MethodCodeBuilder mcb, Method mthd, Integer hash)
	{
		mcb.ldc(hash);
		Jam2Util.pushFunctionStaticReference(mcb, internalName, mthd.getName(), LAMBDA_CONTEXT_CONSTRUCTOR_INJECT);
		mcb.invokevirtual(INJECTION_INTERNAL_NAME, "pushConstructor", DESCRIPTOR_PUSH_CONSTRUCTOR);
	}
	
	public abstract Injection initialize(Injection uninitialized) throws UnsatisfiedInjectionException;
	
	private static final LambdaContext LAMBDA_CONTEXT_FIELD_GETTER
			= LambdaContext.newContext(FieldGetterInjection.class, "get", Object.class, new Class<?> [] {Object.class});
	
	private static final LambdaContext LAMBDA_CONTEXT_FIELD_SETTER
			= LambdaContext.newContext(FieldSetterInjection.class, "set", void.class, new Class<?>[] {Object.class, Object.class});
	
	private static final LambdaContext LAMBDA_CONTEXT_METHOD_INJECT
			= LambdaContext.newContext(MethodInjection.class, "inject", Object.class, new Class<?>[] {Object.class, Object[].class});
	
	private static final LambdaContext LAMBDA_CONTEXT_CONSTRUCTOR_INJECT
			= LambdaContext.newContext(ConstructorInjection.class, "inject", Object.class, new Class<?>[] {Object[].class});
	
	private static final String DESCRIPTOR_PUSH_FIELD_GETTER
			= Jam2Util.toDescriptor(null, Injection.class, new Class<?>[] {String.class, FieldGetterInjection.class});
	
	private static final String DESCRIPTOR_PUSH_FIELD_SETTER
			= Jam2Util.toDescriptor(null, Injection.class, new Class<?>[] {String.class, FieldSetterInjection.class});
	
	private static final String DESCRIPTOR_PUSH_METHOD
			= Jam2Util.toDescriptor(null, Injection.class, new Class<?>[] {int.class, MethodInjection.class});
	
	private static final String DESCRIPTOR_PUSH_CONSTRUCTOR
			= Jam2Util.toDescriptor(null, Injection.class, new Class<?>[] {int.class, ConstructorInjection.class});
	
	private static final String INJECTION_INTERNAL_NAME = Type.getInternalName(Injection.class);
	
	private static final String INJECTOR_INTERNAL_NAME = Type.getInternalName(Injector.class);
	
	private static final String UNSATISFIED_EXCEPTION_INTERNAL_NAME = Type.getInternalName(UnsatisfiedInjectionException.class);
}
