package org.kucro3.jam2.util;

import org.objectweb.asm.MethodVisitor;

import java.util.Optional;

public interface MethodContext extends AccessableContext {
	String[] getExceptions();

	default Optional<Class<?>[]> tryGetExceptionClasses()
	{
		return Jam2Util.tryFromInternalNamesToClasses(getExceptions());
	}
	
	String getDeclaringClass();

	default Optional<Class<?>> tryGetDeclaringClass()
	{
		return Jam2Util.tryFromInternalNameToClass(getDeclaringClass());
	}

	default String[] getArguments()
	{
		String desc;
		if((desc = getDescriptor()) == null)
			return null;
		return new LazyDescriptorAnalyzer(desc).getArguments();
	}

	default Optional<Class<?>[]> tryGetArgumentClasses()
	{
		return Jam2Util.tryFromDescriptorsToClasses(getArguments());
	}
	
	default String getReturnType()
	{
		String desc;
		if((desc = getDescriptor()) == null)
			return null;
		return new LazyDescriptorAnalyzer(desc).getReturnType();
	}

	default Optional<Class<?>> tryGetReturnTypeClass()
	{
		String returnType = this.getReturnType();

		try {
			Class<?> clazz = Class.forName(Jam2Util.fromDescriptorToCanonical(returnType));
			return Optional.of(clazz);
		} catch (ClassNotFoundException e) {
			return Optional.empty();
		}
	}
	
	default void setExceptions(String[] exceptions) {throw new UnsupportedOperationException();}
	
	interface Modifiable extends MethodContext, AccessableContext.Modifiable
	{
		@Override
		void setExceptions(String[] exceptions);
	}
	
	interface RestrictedModifiable extends Modifiable, AccessableContext.RestrictedModifiable
	{
	}
	
	interface FullyModifiable extends Modifiable, AccessableContext.FullyModifiable
	{
	}
	
	interface Compound extends MethodContext
	{
		MethodContext getContext();
	}
	
	interface Visited extends MethodContext
	{
		MethodVisitor getVisitor();
	}
}
