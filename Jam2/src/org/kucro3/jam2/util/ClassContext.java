package org.kucro3.jam2.util;

import java.util.Collection;

public interface ClassContext {
	String getDebug();
	
	String getEnclosingClass();
	
	String getEnclosingMethodName();
	
	String getEnclosingMethodDescriptor();
	
	String[] getInterfaces();
	
	String getSignature();
	
	String getName();
	
	int getModifier();
	
	String getSource();
	
	String getSuperClass();
	
	int getVersion();
	
	boolean containsField(String name);
	
	boolean containsMethod(String descriptor);
	
	default void setSource(String source) {throw new UnsupportedOperationException();}
	
	default void setDebug(String debug) {throw new UnsupportedOperationException();}
	
	default void setSuperClass(String superClass) {throw new UnsupportedOperationException();}
	
	default void setInterfaces(String[] interfaces) {throw new UnsupportedOperationException();}
	
	default void setEnclosingClass(String enclosingClass) {throw new UnsupportedOperationException();}
	
	default void setEnclosingMethodName(String name) {throw new UnsupportedOperationException();}
	
	default void setEnclosingMethodDescriptor(String descriptor) {throw new UnsupportedOperationException();}
	
	default void setName(String name) {throw new UnsupportedOperationException();}
	
	default void setVersion(int version) {throw new UnsupportedOperationException();}
	
	default void setModifier(int modifier) {throw new UnsupportedOperationException();}
	
	default void setSignature(String signature) {throw new UnsupportedOperationException();}
	
	default void clearFields() {throw new UnsupportedOperationException();}
	
	default void removeField(String name) {throw new UnsupportedOperationException();}
	
	default void clearMethods() {throw new UnsupportedOperationException();}
	
	default void removeMethod(String fullDescriptor) {throw new UnsupportedOperationException();}
	
	boolean hasMethod();
	
	boolean hasField();
	
	default boolean containsMethod(String name, String descriptor)
	{
		return containsMethod(name + descriptor);
	}
	
	default boolean containsMethod(String name, String returnType, String[] arguments)
	{
		return containsMethod(Jam2Util.toDescriptor(name, returnType, arguments));
	}
	
	default boolean containsMethod(String name, Class<?> returnType, Class<?>[] arguments)
	{
		return containsMethod(Jam2Util.toDescriptor(name, returnType, arguments));
	}
	
	Collection<FieldContext> getFields();
	
	FieldContext getField(String name);
	
	Collection<MethodContext> getMethods();
	
	MethodContext getMethod(String descriptor);
	
	default MethodContext getMethod(String name, String descriptor)
	{
		return getMethod(name + descriptor);
	}
	
	default MethodContext getMethod(String name, String returnType, String[] arguments)
	{
		return getMethod(Jam2Util.toDescriptor(name, returnType, arguments));
	}
	
	default MethodContext getMethod(String name, Class<?> returnType, Class<?>[] arguments)
	{
		return getMethod(Jam2Util.toDescriptor(name, returnType, arguments));
	}
	
	default FieldContext newField(int modifier, String name, String descriptor, String signature, Object value)
	{
		throw new UnsupportedOperationException();
	}
	
	default FieldContext newField(FieldContext fc)
	{
		return newField(fc.getModifier(), fc.getName(), fc.getDescriptor(), fc.getSignature(), fc.getValue());
	}
	
	default FieldContext newField(int modifier, String name, String descriptor, String signature)
	{
		return newField(modifier, name, descriptor, signature, null);
	}
	
	default FieldContext newField(int modifier, String name, String descriptor)
	{
		return newField(modifier, name, descriptor, null, null);
	}
	
	default MethodContext newMethod(int modifier, String name, String descriptor, String signature, String[] exceptions)
	{
		throw new UnsupportedOperationException();
	}
	
	default MethodContext newMethod(MethodContext mc)
	{
		return newMethod(mc.getModifier(), mc.getName(), mc.getDescriptor(), mc.getSignature(), mc.getExceptions());
	}
	
	default MethodContext newMethod(int modifier, String name, String descriptor, String signature)
	{
		return newMethod(modifier, name, descriptor, signature, null);
	}
	
	default MethodContext newMethod(int modifier, String name, String descriptor)
	{
		return newMethod(modifier, name, descriptor, null, null);
	}
	
	default MethodContext newMethod(int modifier, String name, Class<?> returnType, Class<?>[] arguments)
	{
		return newMethod(modifier, name, returnType, arguments, null, null);
	}
	
	default MethodContext newMethod(int modifier, String name, Class<?> returnType, Class<?>[] arguments,
			Class<?>[] throwings)
	{
		return newMethod(modifier, name, returnType, arguments, throwings, null);
	}
	
	default MethodContext newMethod(int modifier, String name, Class<?> returnType, Class<?>[] arguments,
			String signature)
	{
		return newMethod(modifier, name, returnType, arguments, null, signature);
	}
	
	default MethodContext newMethod(int modifier, String name, Class<?> returnType, Class<?>[] arguments, 
			Class<?>[] throwings, String signature)
	{
		return newMethod(modifier,
				name,
				Jam2Util.toDescriptor("", returnType, arguments),
				signature,
				Jam2Util.toInternalNames(throwings));
	}
	
	default MethodContext newConstructor(int modifier, Class<?>[] arguments)
	{
		return newConstructor(modifier, arguments, null, null);
	}
	
	default MethodContext newConstructor(int modifier, Class<?>[] arguments, Class<?>[] throwings)
	{
		return newConstructor(modifier, arguments, throwings, null);
	}
	
	default MethodContext newConstructor(int modifier, Class<?>[] arguments, String signature)
	{
		return newConstructor(modifier, arguments, null, signature);
	}
	
	default MethodContext newConstructor(int modifier, Class<?>[] arguments, Class<?>[] throwings, String signature)
	{
		return newMethod(modifier,
				"<init>",
				Jam2Util.toConstructorDescriptor(arguments),
				signature,
				Jam2Util.toInternalNames(throwings));
	}
	
	interface Modifiable extends ClassContext
	{
		@Override
		void setSource(String source);
		
		@Override
		void setDebug(String debug);
		
		@Override
		void setSuperClass(String superClass);
		
		@Override
		void setInterfaces(String[] interfaces);
		
		@Override
		void setEnclosingClass(String enclosingClass);
		
		@Override
		void setEnclosingMethodName(String name);
		
		@Override
		void setEnclosingMethodDescriptor(String descriptor);
		
		@Override
		void setVersion(int version);
		
		@Override
		void setModifier(int modifier);
		
		@Override
		void setSignature(String signature);
		
		@Override
		void clearFields();
		
		@Override
		void removeField(String name);

		@Override
		FieldContext newField(int modifier, String name, String descriptor, String signature, Object value);

		@Override
		void clearMethods();
		
		@Override
		void removeMethod(String fullDescriptor);

		@Override
		MethodContext newMethod(int modifier, String name, String descriptor, String signature, String[] exceptions);
		
		default void removeMethod(String name, String descriptor)
		{
			removeMethod(name + descriptor);
		}
		
		default void removeMethod(String name, String returnType, String[] arguments)
		{
			removeMethod(Jam2Util.toDescriptor(name, returnType, arguments));
		}
		
		default void removeMethod(String name, Class<?> returnType, Class<?>[] arguments)
		{
			removeMethod(Jam2Util.toDescriptor(name, returnType, arguments));
		}
	}
	
	interface RestrictedModifiable extends Modifiable
	{
	}
	
	interface FullyModifiable extends Modifiable
	{
		@Override
		void setName(String name);
	}
	
	interface Compound extends ClassContext
	{
		ClassContext getContext();
	}
	
	interface Visitable extends ClassContext
	{
	}
}