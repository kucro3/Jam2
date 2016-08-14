package org.kucro3.jam2.asm;

import org.kucro3.jam2.util.ClassContext;
import org.kucro3.jam2.util.Jam2Util;
import org.objectweb.asm.Opcodes;

public class ASMRunnableBuilder extends ASMCodeBuilderRoot<ASMRunnableBuilder> implements Opcodes {
	public static ASMRunnableBuilder newBuilder()
	{
		ClassContext ctx = new ClassContext(V1_8, ACC_PUBLIC, 
				"org/kucro3/jam2/asm/ASMRunnable_" + Jam2Util.generateUUIDForClassName(),
				null, "java/lang/Object",  new String[] {"java/lang/Runnable"});
		return new ASMRunnableBuilder(ctx, "run", "V", null);
	}
	
	ASMRunnableBuilder(ClassContext ctx, String methodName, String returnType, String[] arguments)
	{
		super(ctx.addMethod(ACC_PUBLIC, methodName, returnType, arguments, null));
		Jam2Util.pushEmptyConstructor(ctx, ACC_PUBLIC, Object.class);
		this.ctx = ctx;
	}
	
	public Runnable build()
	{
		if(cached != null)
			return cached;
		super.end();
		Class<?> clz = ctx.newClass();
		try {
			return cached = (Runnable) clz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// unused
			throw new IllegalStateException(e);
		}
	}
	
	private Runnable cached;
	
	private final ClassContext ctx;
}
