package org.kucro3.jam2.inject;

import java.lang.reflect.Modifier;
import java.util.Collection;

import org.kucro3.jam2.util.ClassContext;
import org.kucro3.jam2.util.ConstructorContext;
import org.kucro3.jam2.util.FieldContext;
import org.kucro3.jam2.util.Jam2Util;
import org.kucro3.jam2.util.Jam2Util.CallingType;
import org.kucro3.jam2.util.MethodContext;
import org.objectweb.asm.Opcodes;

public class InjectionInjector implements Opcodes {
	public InjectionInjector(ClassContext ctx)
	{
		this.ctx = ctx;
	}
	
	public void inject()
	{
		Collection<MethodContext> mc = ctx.getMethods();
		MethodContext[] methods = new MethodContext[mc.size()];
		mc.toArray(methods);
		for(MethodContext method : methods)
			if(method instanceof ConstructorContext)
				injectConstructor((ConstructorContext) method);
			else
				injectMethod(method);
		
		for(FieldContext field : ctx.getFields())
			injectField(field);
	}
	
	public ClassContext getContext()
	{
		return ctx;
	}
	
	private final void injectField(FieldContext fctx)
	{
		String name = generateFieldInjectionName(fctx);
		
		// getter
		Jam2Util.pushFieldGetter(ctx, ACC_PUBLIC + ACC_STATIC, name, fctx, true, true);
		
		// setter
		if(!Modifier.isFinal(fctx.getModifier()))
			Jam2Util.pushFieldSetter(ctx, ACC_PUBLIC + ACC_STATIC, name, fctx, true);
	}
	
	private static final String generateFieldInjectionName(FieldContext fctx)
	{
		return ("<jam2_inject>" + fctx.getFieldName());
	}
	
	private final void injectMethod(MethodContext mctx)
	{
		String name = generateMethodInjectionName(mctx);
		
		// inject
		Jam2Util.pushCaller(ctx, ACC_PUBLIC + ACC_STATIC, name, mctx, CallingType.fromMethod(ctx, mctx), true, true);
	}
	
	private static final String generateMethodInjectionName(MethodContext mctx)
	{
		return ("<jam2_inject>" + mctx.getMethodName() + "$" + mctx.getMethodDescriptor().hashCode());
	}
	
	private final void injectConstructor(ConstructorContext cctx)
	{
		String name = generateConstructorInjectionName(cctx);
		
		// inject
		Jam2Util.pushNewInstance(ctx, ACC_PUBLIC + ACC_STATIC, name, cctx, true, true);
	}
	
	private static final String generateConstructorInjectionName(ConstructorContext cctx)
	{
		return ("<jam2_inject>" + "$" + cctx.getMethodDescriptor().hashCode());
	}
	
	private final ClassContext ctx;
}