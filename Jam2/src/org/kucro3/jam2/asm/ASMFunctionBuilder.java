package org.kucro3.jam2.asm;

import java.lang.reflect.Method;

import org.kucro3.jam2.util.Jam2Util;
import org.kucro3.jam2.util.MethodContext;
import org.kucro3.jam2.util.Version;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

public class ASMFunctionBuilder extends ASMCodeBuilderRoot<ASMFunctionBuilder> {
	public static ASMFunctionBuilder newBuilder(Method method)
	{
		return newBuilder(method.getParameterTypes());
	}
	
	public static ASMFunctionBuilder newBuilder(MethodContext method)
	{
		return newBuilder(method.getArguments());
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
		String name;
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		
		cw.visit(
				Version.getClassVersion(),
				ACC_PUBLIC,
				name = "org/kucro3/jam2/asm/ASMFunction_" + Jam2Util.generateUUIDForClassName(),
				null,
				"org/kucro3/jam2/asm/ASMFunction",
				null);
		
		Jam2Util.pushEmptyConstructor(cw, ACC_PUBLIC, "org/kucro3/jam2/asm/ASMFunction");
		MethodVisitor mv = cw.visitMethod(
				ACC_PUBLIC | ACC_VARARGS, "function", "([Ljava/lang/Object;)Ljava/lang/Object;", null, null);
		int length;
		ASMFunctionBuilder asmfb = new ASMFunctionBuilder(cw, mv, name, arguments, argumentDescriptors)
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
	
	ASMFunctionBuilder(ClassWriter ctx, MethodVisitor mv,
			String name, Class<?>[] arguments, String[] argumentDescriptors)
	{
		super(mv);
		this.name = name;
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
			super.end(true);
			try {
				cached = (ASMFunction) Jam2Util.newClass(Jam2Util.fromInternalNameToCanonical(name), ctx).newInstance();
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
	
	private final String name;
	
	private final ClassWriter ctx;
	
	private final String[] argumentDescriptors;
	
	private final Class<?>[] arguments;
}
