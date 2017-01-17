package org.kucro3.jam2.opcode;

public class InstructionObjectCloner {
	private InstructionObjectCloner()
	{
	}
	
	public static InstructionObjectCloner newLocal()
	{
		return new InstructionObjectCloner();
	}
	
	public static InstructionObjectCloner newLocalSynchronized()
	{
		return new Synchronized();
	}
	
	public static InstructionObjectCloner getGlobal()
	{
		return global;
	}
	
	public Instruction clone(Instruction insn)
	{
		insn.visit(cloner);
		Instruction i = cloner.get(0);
		cloner.clear();
		return i;
	}
	
	private final Cloner cloner = new Cloner();
	
	private static final InstructionObjectCloner global = new Synchronized();
	
	private static class Synchronized extends InstructionObjectCloner
	{
		Synchronized()
		{
		}
		
		@Override
		public synchronized Instruction clone(Instruction insn)
		{
			return super.clone(insn);
		}
	}
	
	private static class Cloner extends InstructionContainer
	{
		Cloner()
		{
		}
	}
}
