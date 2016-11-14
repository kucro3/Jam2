package org.kucro3.jam2.visitor;

import org.kucro3.jam2.opcode.InstructionContainer;
import org.kucro3.jam2.util.Version;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class InstructionVisitor extends MethodVisitor {
	public InstructionVisitor()
	{
		super(Version.getASMVersion());
		this.container = new InstructionContainer();
	}
	
	public InstructionVisitor(MethodVisitor mv)
	{
		super(Version.getASMVersion(), mv);
		this.container = new InstructionContainer(mv);
	}
	
	public final InstructionContainer getContainer()
	{
		return container;
	}
	
	@Override
	public void visitFieldInsn(int insn, String owner, String name, String descriptor)
	{
		container.visitFieldInsn(insn, owner, name, descriptor);
	}
	
	@Override
	public void visitIincInsn(int var, int increment)
	{
		container.visitIincInsn(var, increment);
	}
	
	@Override
	public void visitInsn(int insn)
	{
		container.visitInsn(insn);
	}
	
	@Override
	public void visitIntInsn(int insn, int operand)
	{
		container.visitIntInsn(insn, operand);
	}
	
	@Override
	public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs)
	{
		container.visitInvokeDynamicInsn(name, desc,bsm, bsmArgs);
	}
	
	@Override
	public void visitJumpInsn(int insn, Label label)
	{
		container.visitJumpInsn(insn, label);
	}
	
	@Override
	public void visitLdcInsn(Object cst)
	{
		container.visitLdcInsn(cst);
	}
	
	@Override
	public void visitLookupSwitchInsn(Label labelDefault, int[] keys, Label[] labels)
	{
		container.visitLookupSwitchInsn(labelDefault, keys, labels);
	}
	
	@Deprecated
	@Override
	public void visitMethodInsn(int insn, String owner, String name, String descriptor)
	{
		container.visitMethodInsn(insn, owner, name, descriptor);
	}
	
	@Override
	public void visitMethodInsn(int insn, String owner, String name, String descriptor, boolean ifInterface)
	{
		container.visitMethodInsn(insn, owner, name, descriptor, ifInterface);
	}
	
	@Override
	public void visitMultiANewArrayInsn(String type, int dimension)
	{
		container.visitMultiANewArrayInsn(type, dimension);
	}
	
	@Override
	public void visitTableSwitchInsn(int min, int max, Label labelDefault, Label... labels)
	{
		container.visitTableSwitchInsn(min, max, labelDefault, labels);
	}
	
	@Override
	public void visitTypeInsn(int insn, String type)
	{
		container.visitTypeInsn(insn, type);
	}
	
	@Override
	public void visitVarInsn(int insn, int var)
	{
		container.visitVarInsn(insn, var);
	}
	
	final InstructionContainer container;
}