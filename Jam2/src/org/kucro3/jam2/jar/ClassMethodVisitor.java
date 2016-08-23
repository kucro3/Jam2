package org.kucro3.jam2.jar;

import org.kucro3.jam2.opcode.InstructionContainer;
import org.kucro3.jam2.util.ClassContext;
import org.objectweb.asm.MethodVisitor;

public class ClassMethodVisitor extends MethodVisitor {
	ClassMethodVisitor(ClassMethod cm, MethodVisitor mv, InstructionContainer container)
	{
		super(ClassContext.API, container == null ? mv : container);
		this.container = container;
		this.owner = cm;
	}
	
	public ClassMethod getOwner()
	{
		return owner;
	}
	
	@Override
	public void visitEnd()
	{
		this.owner.insns = container.toInstructions();
	}
	
	private final InstructionContainer container;
	
	private final ClassMethod owner;
}
