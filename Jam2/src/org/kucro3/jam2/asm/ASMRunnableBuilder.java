package org.kucro3.jam2.asm;

import org.kucro3.jam2.util.Jam2Util;
import org.kucro3.jam2.util.Version;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

public class ASMRunnableBuilder extends ASMCodeBuilderRoot<ASMRunnableBuilder> implements Opcodes {
	public static ASMRunnableBuilder newBuilder()
	{
		String name;
		ClassWriter ctx = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		ctx.visit(
				Version.getClassVersion(), 
				ACC_PUBLIC, 
				name = "org/kucro3/jam2/asm/ASMRunnable_" + Jam2Util.generateUUIDForClassName(),
				null,
				"java/lang/Object",
				new String[] {"java/lang/Runnable"});
		return new ASMRunnableBuilder(ctx, name, "run", "()V");
	}
	
	ASMRunnableBuilder(ClassWriter ctx, String name, String methodName, String descriptor)
	{
		super(ctx.visitMethod(ACC_PUBLIC, methodName, descriptor, null, null));
		this.name = name;
		Jam2Util.pushEmptyConstructor(ctx, ACC_PUBLIC, Object.class);
		this.ctx = ctx;
	}
	
	public Runnable build()
	{
		try {
			if(cached != null)
				return (Runnable) cached.newInstance();
			end(true);
			Class<?> clz = Jam2Util.newClass(Jam2Util.fromInternalNameToCanonical(name), ctx);
			return (Runnable)(cached = clz).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// unused
			throw new IllegalStateException(e);
		}
	}
	
	private Class<?> cached;
	
	private final String name;
	
	private final ClassWriter ctx;
}
