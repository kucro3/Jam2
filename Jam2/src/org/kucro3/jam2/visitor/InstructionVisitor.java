package org.kucro3.jam2.visitor;

import org.kucro3.jam2.opcode.InstructionContainer;
import org.kucro3.jam2.util.Version;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class InstructionVisitor extends ClassVisitor {
	public InstructionVisitor(ClassVisitor cv) 
	{
		super(Version.getASMVersion(), cv);
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions)
	{
		MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
		return new InstructionContainer(mv);
	}
}
