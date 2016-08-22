package org.kucro3.jam2.opcode;

import java.util.ArrayList;

import org.kucro3.jam2.util.ClassContext;
import org.objectweb.asm.MethodVisitor;

public class InstructionContainer extends MethodVisitor {
	public InstructionContainer()
	{
		this(null);
	}
	
	public InstructionContainer(MethodVisitor mv)
	{
		super(ClassContext.API, mv);
	}
	
	public int count()
	{
		return insns.size();
	}
	
	private final ArrayList<Instruction> insns = new ArrayList<>();
}
