package org.kucro3.jam2.visitor;

import org.kucro3.jam2.opcode.Instruction;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class InstructionFlowVisitor extends InstructionVisitor {
	public InstructionFlowVisitor()
	{
	}
	
	public InstructionFlowVisitor(MethodVisitor mv)
	{
		super(mv);
	}
	
	@Override
	public void clear()
	{
		super.clear();
		this.maxStacks = 0;
		this.maxLocals = 0;
	}
	
	@Override
	public void visitMaxs(int maxStacks, int maxLocals)
	{
		super.visitMaxs(maxStacks, maxLocals);
		this.maxStacks = maxStacks;
		this.maxLocals = maxLocals;
	}
	
	@Override
	public void visitLabel(Label label)
	{
		super.visitLabel(label);
		super.container.append(new MetadataVisitLabel(label));
	}
	
	@Override
	public void visitTryCatchBlock(Label start, Label end, Label handler, String type)
	{
		super.visitTryCatchBlock(start, end, handler, type);
		super.container.append(new MetadataVisitTryCatchBlock(start, end, handler, type));
	}
	
	public int getMaxStacks()
	{
		return maxStacks;
	}
	
	public int getMaxLocals()
	{
		return maxLocals;
	}
	
	public void setMaxStacks(int maxStacks)
	{
		this.maxStacks = maxStacks;
	}
	
	public void setMaxLocals(int maxLocals)
	{
		this.maxLocals = maxLocals;
	}
	
	int maxStacks;
	
	int maxLocals;
	
	public static class MetadataVisitLabel extends Instruction
	{
		public MetadataVisitLabel(Label label)
		{
			this.label = label;
		}

		@Override
		public void visit(MethodVisitor mv) 
		{
			mv.visitLabel(label);
		}
		
		public Label getLabel()
		{
			return label;
		}
		
		protected Label label;
	}
	
	public static class MetadataVisitTryCatchBlock extends Instruction
	{
		public MetadataVisitTryCatchBlock(Label start, Label end, Label handler, String type)
		{
			this.start = start;
			this.end = end;
			this.handler = handler;
			this.type = type;
		}
		
		@Override
		public void visit(MethodVisitor mv) 
		{
			mv.visitTryCatchBlock(start, end, handler, type);
		}
		
		public Label getStart()
		{
			return start;
		}
		
		public Label getEnd()
		{
			return end;
		}
		
		public Label getHandler()
		{
			return handler;
		}
		
		public String getType()
		{
			return type;
		}
		
		protected Label start;
		
		protected Label end;
		
		protected Label handler;
		
		protected String type;
	}
}