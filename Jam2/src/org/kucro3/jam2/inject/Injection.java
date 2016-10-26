package org.kucro3.jam2.inject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.kucro3.jam2.invoke.ConstructorInvoker;
import org.kucro3.jam2.invoke.FieldInvoker;
import org.kucro3.jam2.invoke.MethodInvoker;

import static org.kucro3.jam2.util.Jam2Util.toDescriptor;
import static org.kucro3.jam2.util.Jam2Util.toConstructorDescriptor;

public class Injection {
	Injection(Class<?> injected)
	{
		this.injected = injected;
	}
	
	public Class<?> getInjectedClass()
	{
		return injected;
	}
	
	public ConstructorInvoker getConstructor(Class<?>[] arguments)
	{
		return constructors.get(toConstructorDescriptor(arguments));
	}
	
	public MethodInvoker getMethod(String name, Class<?> returnType, Class<?>[] arguments)
	{
		return methods.get(toDescriptor(name, returnType, arguments));
	}
	
	public FieldInvoker getField(String name)
	{
		return fields.get(name);
	}
	
	void initializeMethods(Map<Integer, Method> mappedMethods)
	{
		this.mappedMethods = mappedMethods;
	}
	
	void initializeConstructors(Map<Integer, Constructor<?>> mappedConstructors)
	{
		this.mappedConstructors = mappedConstructors;
	}
	
	final Injection pushMethod(int hash, MethodInjection injection) throws UnsatisfiedInjectionException
	{
		Method mthd;
		if((mthd = mappedMethods.get(hash)) == null)
			throw new UnsatisfiedInjectionException("method not found (hash: " + hash + ")");
		methods.put(toDescriptor(mthd), new MethodInjectionImpl(mthd, injection));
		return this;
	}
	
	final Injection pushConstructor(int hash, ConstructorInjection injection) throws UnsatisfiedInjectionException
	{
		Constructor<?> constructor;
		if((constructor = mappedConstructors.get(hash)) == null)
			throw new UnsatisfiedInjectionException("constructor not found (hash: " + hash + ")");
		constructors.put(toConstructorDescriptor(constructor), new ConstructorInjectionImpl(constructor, injection));
		return this;
	}
	
	final Injection pushFieldGetter(String hash, FieldGetterInjection injection)
	{
		getters.put(hash, injection);
		return this;
	}
	
	final Injection pushFieldSetter(String hash, FieldSetterInjection injection)
	{
		setters.put(hash, injection);
		return this;
	}
	
	Injection finish() throws UnsatisfiedInjectionException
	{
		FieldSetterInjection setter;
		String name;
		for(Map.Entry<String, FieldGetterInjection> e : this.getters.entrySet())
		{
			if((setter = setters.get(name = e.getKey())) == null)
				setter = new UnmodifiableField();
			try {
				fields.put(name, new FieldInjectionImpl(injected.getDeclaredField(name), setter, e.getValue()));
			} catch (NoSuchFieldException | SecurityException e1) {
				throw new UnsatisfiedInjectionException(e1);
			}
		}
		mappedMethods.clear();
		mappedConstructors.clear();
		getters.clear();
		setters.clear();
		return this;
	}
	
	Map<Integer, Method> mappedMethods;
	
	Map<Integer, Constructor<?>> mappedConstructors;
	
	private final Map<String, ConstructorInvoker> constructors = new HashMap<>();
	
	private final Map<String, MethodInvoker> methods = new HashMap<>();
	
	final Map<String, FieldGetterInjection> getters = new HashMap<>();
	
	final Map<String, FieldSetterInjection> setters = new HashMap<>();
	
	private final Map<String, FieldInvoker> fields = new HashMap<>();
	
	private final Class<?> injected;
	
	public static interface FieldGetterInjection
	{
		Object get(Object obj);
	}
	
	public static interface FieldSetterInjection
	{
		void set(Object obj, Object value);
	}
	
	class UnmodifiableField implements FieldSetterInjection
	{
		@Override
		public void set(Object obj, Object value) 
		{
			throw new UnsupportedOperationException("final field cannot be modified");
		}
	}
	
	public static interface ConstructorInjection
	{
		Object inject(Object... arguments);
	}
	
	public static interface MethodInjection
	{
		Object inject(Object obj, Object... arguments);
	}
	
	class FieldInjectionImpl extends FieldInvoker
	{
		FieldInjectionImpl(Field field, FieldSetterInjection setter, FieldGetterInjection getter)
		{
			this(field.getDeclaringClass(), field.getModifiers(), field.getName(), field.getType(), setter, getter);
		}
		
		FieldInjectionImpl(Class<?> declaringClass, int modifier, String name, Class<?> type, FieldSetterInjection setter,
				FieldGetterInjection getter) 
		{
			super(declaringClass, modifier, name, type);
			this.setter = setter;
			this.getter = getter;
		}

		@Override
		public void set(Object obj, Object ref)
		{
			setter.set(obj, ref);
		}

		@Override
		public Object get(Object obj) 
		{
			return getter.get(obj);
		}
		
		final FieldSetterInjection setter;
		
		final FieldGetterInjection getter;
	}
	
	class MethodInjectionImpl extends MethodInvoker
	{
		MethodInjectionImpl(Method method, MethodInjection injection)
		{
			this(method.getDeclaringClass(), method.getModifiers(), method.getName(),
					method.getReturnType(), method.getParameterTypes(), injection);
		}
		
		MethodInjectionImpl(Class<?> declaringClass, int modifier, String name, Class<?> returnType,
				Class<?>[] arguments, MethodInjection injection) {
			super(declaringClass, modifier, name, returnType, arguments);
			this.injection = injection;
		}

		@Override
		public Object invoke(Object obj, Object... args)
		{
			return injection.inject(obj, args);
		}
		
		final MethodInjection injection;
	}
	
	class ConstructorInjectionImpl extends ConstructorInvoker
	{
		ConstructorInjectionImpl(Constructor<?> constructor, ConstructorInjection injection)
		{
			this(constructor.getDeclaringClass(), constructor.getModifiers(), constructor.getParameterTypes(), injection);
		}
		
		ConstructorInjectionImpl(Class<?> declaringClass, int modifier, Class<?>[] arguments,
				ConstructorInjection injection)
		{
			super(declaringClass, modifier, arguments);
			this.injection = injection;
		}
		
		@Override
		public Object newInstance(Object... args) 
		{
			return injection.inject(args);
		}
		
		final ConstructorInjection injection;
	}
}
