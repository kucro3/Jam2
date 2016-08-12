package org.kucro3.jam2.asm;

import org.objectweb.asm.MethodVisitor;

public class ASMCodeBuilder extends ASMCodeBuilderRoot<ASMCodeBuilder> {
	public ASMCodeBuilder(MethodVisitor mv) 
	{
		super(mv);
	}
}
