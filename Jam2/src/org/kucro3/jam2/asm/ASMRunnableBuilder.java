package org.kucro3.jam2.asm;

import org.kucro3.jam2.util.ClassContext;
import org.kucro3.jam2.util.Jam2Util;
import org.objectweb.asm.Opcodes;

public class ASMRunnableBuilder extends ASMCodeBuilderRoot<ASMRunnableBuilder> implements Opcodes {
	public static ASMRunnableBuilder newBuilder()
	{
		ClassContext ctx = new ClassContext(V1_8, ACC_PUBLIC | ACC_SUPER, 
				"ASMRunnable_" + Jam2Util.generateUUIDForClassName(), null, "java/lang/Object",  new String[] {"java/lang/Runnable"});
		return new ASMRunnableBuilder(ctx, "run", "V", null, null);
	}
	
	ASMRunnableBuilder(ClassContext ctx, String methodName, String returnType, String[] arguments, String[] throwings)
	{
		super(ctx.addMethod(ACC_PUBLIC, methodName, returnType, arguments, throwings));
		Jam2Util.pushEmptyConstructor(ctx, ACC_PUBLIC, Object.class);
		this.classCtx = ctx;
	}
	
	public Runnable build()
	{
		super.end();
		if(cached != null)
			return cached;
		Class<?> clz = classCtx.newClass();
		try {
			return cached = (Runnable) clz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// unused
			throw new IllegalStateException(e);
		}
	}
	
	private Runnable cached;
	
	private final ClassContext classCtx;
}
