package org.kucro3.jam2.asm;

import java.lang.reflect.Method;

import org.kucro3.jam2.util.ClassContext;
import org.kucro3.jam2.util.Jam2Util;
import org.kucro3.jam2.util.MethodContext;
import org.objectweb.asm.MethodVisitor;

public class ASMFunctionBuilder extends ASMCodeBuilderRoot<ASMFunctionBuilder> {
	public static ASMFunctionBuilder newBuilder(Method method)
	{
		return newBuilder(method.getParameterTypes());
	}
	
	public static ASMFunctionBuilder newBuilder(MethodContext method)
	{
		return newBuilder(method.getArgumentDescriptors());
	}
	
	public static ASMFunctionBuilder newBuilder(Class<?>[] arguments)
	{
		return newBuilder(arguments, Jam2Util.toInternalNames(arguments));
	}
	
	public static ASMFunctionBuilder newBuilder(String[] arguments)
	{
		return newBuilder(null, arguments);
	}
	
	static ASMFunctionBuilder newBuilder(Class<?>[] arguments, String[] argumentDescriptors)
	{
		ClassContext ctx = new ClassContext(V1_8, ACC_PUBLIC, "org/kucro3/jam2/asm/ASMFunction_" + Jam2Util.generateUUIDForClassName(),
				null, "org/kucro3/jam2/asm/ASMFunction", null);
		Jam2Util.pushEmptyConstructor(ctx, ACC_PUBLIC, "org/kucro3/jam2/asm/ASMFunction");
		MethodContext mctx = ctx.addMethod(
				ACC_PUBLIC | ACC_VARARGS, "function", "Ljava/lang/Object;", new String[] {"[Ljava/lang/Object;"}, null);
		int length;
		ASMFunctionBuilder asmfb = new ASMFunctionBuilder(ctx, mctx, arguments, argumentDescriptors)
				.aload_1();
		for(int i = 0, j = 1; i < (length = argumentDescriptors.length); i++, j++)
		{
			if(j < length)
				asmfb.dup();
			asmfb.sipush(i);
			asmfb.aaload();
			asmfb.checkcast(Jam2Util.fromDescriptorToInternalName(argumentDescriptors[i]));
			asmfb.astore(i);
		}
		return asmfb;
	}
	
	ASMFunctionBuilder(ClassContext ctx, MethodVisitor mv,
			Class<?>[] arguments, String[] argumentDescriptors)
	{
		super(mv);
		this.arguments = arguments;
		this.argumentDescriptors = argumentDescriptors;
		this.ctx = ctx;
	}
	
	public String[] getArgumentDescriptors()
	{
		return argumentDescriptors;
	}
	
	public Class<?>[] getArguments()
	{
		return arguments;
	}
	
	public ASMFunction build()
	{
		if(cached == null)
		{
			super.end();
			try {
				cached = (ASMFunction) ctx.newClass().newInstance();
				cached.arguments = arguments;
				cached.argumentDescriptors = argumentDescriptors;
			} catch (InstantiationException | IllegalAccessException e) {
				// unused
				throw new IllegalStateException(e);
			}
		}
		
		return cached;
	}
	
	private ASMFunction cached;
	
	private final ClassContext ctx;
	
	private final String[] argumentDescriptors;
	
	private final Class<?>[] arguments;
}
