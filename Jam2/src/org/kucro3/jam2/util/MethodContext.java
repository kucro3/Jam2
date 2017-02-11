package org.kucro3.jam2.util;

import org.objectweb.asm.MethodVisitor;

public interface MethodContext extends AccessableContext {
	String[] getExceptions();
	
	String getDeclaringClass();
	
	default String[] getArguments()
	{
		String desc;
		if((desc = getDescriptor()) == null)
			return null;
		return new LazyDescriptorAnalyzer(desc).getArguments();
	}
	
	default String getReturnType()
	{
		String desc;
		if((desc = getDescriptor()) == null)
			return null;
		return new LazyDescriptorAnalyzer(desc).getReturnType();
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
