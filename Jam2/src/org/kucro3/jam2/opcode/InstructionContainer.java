package org.kucro3.jam2.opcode;

import java.util.ArrayList;

import org.kucro3.jam2.util.Version;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.kucro3.jam2.opcode.Instruction.*;

public class InstructionContainer extends MethodVisitor implements Opcodes {
	public InstructionContainer()
	{
		this(null);
	}
	
	public InstructionContainer(MethodVisitor mv)
	{
		super(Version.getASMVersion(), mv);
	}
	
	public int count()
	{
		return insns.size();
	}
	
	@Override
	public int hashCode()
	{
		int result = 0;
		
		for(Instruction insn : insns)
			result = result * 31 + insn.getOpcode().getCode();
		
		return result;
	}
	
	public void append(Instruction insn)
	{
		insns.add(insn);
		if(nextFrame != null)
		{
			insn.preFrame(nextFrame);
			insn.setLabel(label);
			nextFrame = null;
		}
	}
	
	public void appendAndVisit(Instruction insn)
	{
		if(super.mv != null)
			insn.visit(super.mv);
		append(insn);
	}

	public void remove(int i)
	{
		insns.remove(i);
	}
	
	public Instruction get(int i)
	{
		return insns.get(i);
	}
	
	public int length()
	{
		return insns.size();
	}
	
	public void clear()
	{
		insns.clear();
	}
	
	public Instruction last()
	{
		if(insns.size() == 0)
			return null;
		return insns.get(insns.size() - 1);
	}
	
	public InstructionContainer filterAndClone(InstructionFilter filter, MethodVisitor mv)
	{
		InstructionContainer ic = new InstructionContainer();
		for(Instruction insn : this.insns)
			if(filter.accept(insn))
				insn.visit(ic);
		return ic;
	}
	
	public InstructionContainer filterAndClone(InstructionFilter filter)
	{
		return filterAndClone(filter, null);
	}
	
	public InstructionContainer filterAndCopy(InstructionFilter filter, MethodVisitor mv)
	{
		InstructionContainer ic = new InstructionContainer();
		for(Instruction insn : this.insns)
			if(filter.accept(insn))
				ic.append(insn);
		return ic;
	}
	
	public InstructionContainer filterAndCopy(InstructionFilter filter)
	{
		return filterAndClone(filter, null);
	}
	
	public InstructionContainer filterAndRevisit(InstructionFilter filter, MethodVisitor mv)
	{
		InstructionContainer ic = new InstructionContainer();
		for(Instruction insn : this.insns)
			if(filter.accept(insn))
				ic.appendAndVisit(insn);
		return ic;
	}
	
	public InstructionContainer filterAndRevisit(InstructionFilter filter)
	{
		return filterAndClone(filter, null);
	}
	
	public synchronized void filterThis(InstructionFilter filter)
	{
		Instruction[] insns = toInstructions();
		this.insns.clear();
		for(Instruction insn : insns)
			if(filter.accept(insn))
				this.insns.add(insn);
	}
	
	@Override
	public void visitInsn(int insn)
	{
		super.visitInsn(insn);
		append(new InstructionVoid(Opcode.get(insn)));
	}
	
	@Override
	public void visitIntInsn(int insn, int operand)
	{
		super.visitIntInsn(insn, operand);
		append(new InstructionInt(Opcode.get(insn), operand));
	}
	
	@Override
	public void visitVarInsn(int insn, int var)
	{
		super.visitVarInsn(insn, var);
		append(new InstructionVar(Opcode.get(insn), var));
	}
	
	@Override
	public void visitTypeInsn(int insn, String type)
	{
		super.visitTypeInsn(insn, type);
		append(new InstructionType(Opcode.get(insn), type));
	}
	
	@Override
	public void visitFieldInsn(int insn, String owner, String name, String descriptor)
	{
		super.visitFieldInsn(insn, owner, name, descriptor);
		append(new InstructionField(Opcode.get(insn), owner, name, descriptor));
	}
	
	@Deprecated
	@Override
	public void visitMethodInsn(int insn, String owner, String name, String descriptor)
	{
		super.visitMethodInsn(insn, owner, name, descriptor);
		append(new InstructionMethod(Opcode.get(insn), owner, name, descriptor));
	}
	
	@Override
	public void visitMethodInsn(int insn, String owner, String name, String descriptor, boolean ifInterface)
	{
		super.visitMethodInsn(insn, owner, name, descriptor, ifInterface);
		append(new InstructionMethod(Opcode.get(insn), owner, name, descriptor, ifInterface));
	}
	
	@Override
	public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethod, Object... bootstrapArguments)
	{
		super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethod, bootstrapArguments);
		append(new InstructionInvokeDynamic(Opcode.get(INVOKEDYNAMIC), name, descriptor, bootstrapMethod, bootstrapArguments));
	}
	
	@Override
	public void visitJumpInsn(int insn, Label label)
	{
		super.visitJumpInsn(insn, label);
		append(new InstructionJump(Opcode.get(insn), label));
	}
	
	@Override
	public void visitLdcInsn(Object cst)
	{
		super.visitLdcInsn(cst);
		append(new InstructionLdc(Opcode.get(LDC), cst));
	}
	
	@Override
	public void visitIincInsn(int var, int increment)
	{
		super.visitIincInsn(var, increment);
		append(new InstructionIinc(Opcode.get(IINC), var, increment));
	}
	
	@Override
	public void visitTableSwitchInsn(int min, int max, Label labelDefault, Label... labels)
	{
		super.visitTableSwitchInsn(min, max, labelDefault, labels);
		append(new InstructionTableSwitch(Opcode.get(TABLESWITCH), min, max, labelDefault, labels));
	}
	
	@Override
	public void visitLookupSwitchInsn(Label labelDefault, int[] keys, Label[] labels)
	{
		super.visitLookupSwitchInsn(labelDefault, keys, labels);
		append(new InstructionLookupSwitch(Opcode.get(LOOKUPSWITCH), labelDefault, keys, labels));
	}
	
	@Override
	public void visitMultiANewArrayInsn(String type, int dimension)
	{
		super.visitMultiANewArrayInsn(type, dimension);
		append(new InstructionMultiANewArray(Opcode.get(MULTIANEWARRAY), type, dimension));
	}
	
	@Override
	public void visitLabel(Label label)
	{
		super.visitLabel(label);
		this.label = label;
	}
	
	@Override
	public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack)
	{
		super.visitFrame(type, nLocal, local, nStack, stack);
		setNextFrame(new Frame(type, nLocal, local, nStack, stack));
	}
	
	public Instruction[] toInstructions()
	{
		return insns.toArray(new Instruction[0]);
	}
	
	public void revisit(MethodVisitor mv)
	{
		for(Instruction insn : insns)
			insn.visitFully(mv);
	}
	
	public void setNextFrame(Frame frame)
	{
		nextFrame = frame;
	}
	
	private Label label;
	
	private Frame nextFrame;
	
	private final ArrayList<Instruction> insns = new ArrayList<>();
}
