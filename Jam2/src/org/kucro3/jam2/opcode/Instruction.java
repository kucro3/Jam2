package org.kucro3.jam2.opcode;

import org.kucro3.jam2.opcode.Opcode;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public abstract class Instruction {
	Instruction(InstructionContainer container, Opcode opcode)
	{
		this.container = container;
		this.opcode = opcode;
		
		if(opcode.getForm() == OpcodeForm.VAR_CST)
			throw new IllegalArgumentException("Opcode \"" + opcode.getName() + "\" is not supported in ASM");
	}
	
	public InstructionContainer getContainer()
	{
		return container;
	}
	
	public Opcode getOpcode()
	{
		return opcode;
	}
	
	@Override
	public String toString()
	{
		return opcode.getName();
	}
	
	public abstract void visit(MethodVisitor mv);
	
	private final InstructionContainer container;
	
	private final Opcode opcode;
	
	public static class InstructionInt extends Instruction
	{
		public InstructionInt(InstructionContainer container, Opcode opcode, int operand) 
		{
			super(container, opcode);
			this.operand = operand;
		}
		
		public int getOperand()
		{
			return operand;
		}
		
		@Override
		public String toString()
		{
			return super.toString() + " " + operand;
		}
		
		@Override
		public void visit(MethodVisitor mv)
		{
			mv.visitIntInsn(super.opcode.getCode(), operand);
		}
		
		private final int operand;
	}
	
	public static class InstructionVar extends Instruction
	{
		public InstructionVar(InstructionContainer container, Opcode opcode, int var) 
		{
			super(container, opcode);
			this.var = var;
		}
		
		public int getVar()
		{
			return var;
		}
		
		@Override
		public String toString()
		{
			return super.toString() + " " + var;
		}
		
		@Override
		public void visit(MethodVisitor mv)
		{
			mv.visitVarInsn(super.opcode.getCode(), var);
		}

		private final int var;
	}	
	
	public static class InstructionType extends Instruction
	{
		public InstructionType(InstructionContainer container, Opcode opcode, String type) 
		{
			super(container, opcode);
			this.type = type;
		}
		
		public String getType()
		{
			return type;
		}
		
		@Override
		public String toString()
		{
			return super.toString() + " " + type;
		}
		
		@Override
		public void visit(MethodVisitor mv)
		{
			mv.visitTypeInsn(super.opcode.getCode(), type);
		}
		
		private final String type;
	}
	
	public static class InstructionField extends Instruction
	{
		public InstructionField(InstructionContainer container, Opcode opcode, String owner, String name, String descriptor)
		{
			super(container, opcode);
			this.owner = owner;
			this.name = name;
			this.descriptor = descriptor;
		}
		
		public String getOwner()
		{
			return owner;
		}
		
		public String getName()
		{
			return name;
		}
		
		public String getDescriptor()
		{
			return descriptor;
		}
		
		@Override
		public void visit(MethodVisitor mv)
		{
			mv.visitFieldInsn(super.opcode.getCode(), owner, name, descriptor);
		}
		
		@Override
		public String toString()
		{
			return super.toString() + " " + owner + "#" + name + ":" + descriptor;
		}
		
		private final String owner;
		
		private final String name;
		
		private final String descriptor;
	}
	
	public static class InstructionMethod extends Instruction
	{
		public InstructionMethod(InstructionContainer container, Opcode opcode, String owner, String name, String descriptor, boolean ifInterface)
		{
			super(container, opcode);
			this.owner = owner;
			this.name = name;
			this.descriptor = descriptor;
			this.ifInterface = ifInterface;
		}
		
		InstructionMethod(InstructionContainer container, Opcode opcode, String owner, String name, String descriptor)
		{
			this(container, opcode, owner, name, descriptor, false);
		}
		
		public boolean isInterface()
		{
			return ifInterface;
		}
		
		public String getOwner()
		{
			return owner;
		}
		
		public String getName()
		{
			return name;
		}
		
		public String getDescriptor()
		{
			return descriptor;
		}
		
		@Override
		public String toString()
		{
			return super.toString() + " " + owner + "#" + name + ":" + descriptor;
		}
		
		@Override
		public void visit(MethodVisitor mv)
		{
			mv.visitMethodInsn(super.opcode.getCode(), owner, name, descriptor, ifInterface);
		}
		
		private final boolean ifInterface;
		
		private final String owner;
		
		private final String name;
		
		private final String descriptor;
	}
	
	public static class InstructionInvokeDynamic extends Instruction
	{
		public InstructionInvokeDynamic(InstructionContainer container, Opcode opcode, String name, String descriptor,
				Handle bootstrapMethod, Object[] bootstrapArguments)
		{
			super(container, opcode);
			this.name = name;
			this.descriptor = descriptor;
			this.bootstrapMethod = bootstrapMethod;
			this.bootstrapArguments = bootstrapArguments;
		}
		
		public String getName()
		{
			return name;
		}
		
		public String getDescriptor()
		{
			return descriptor;
		}
		
		public Handle getBootstrapMethod()
		{
			return bootstrapMethod;
		}
		
		public Object[] getBootstrapArguments()
		{
			return bootstrapArguments;
		}
		
		@Override
		public String toString()
		{
			return super.toString() + " " + name + ":" + descriptor + "(" + bootstrapMethod + ":" + bootstrapArguments.length + ")";
		}
		
		@Override
		public void visit(MethodVisitor mv)
		{
			mv.visitInvokeDynamicInsn(name, descriptor, bootstrapMethod, bootstrapArguments);
		}
		
		private final String name;
		
		private final String descriptor;
		
		private final Handle bootstrapMethod;
		
		private final Object[] bootstrapArguments;
	}
	
	public static class InstructionJump extends Instruction
	{
		public InstructionJump(InstructionContainer container, Opcode opcode, Label label)
		{
			super(container, opcode);
			this.label = label;
		}
		
		public Label getLabel()
		{
			return label;
		}
		
		@Override
		public String toString()
		{
			return super.toString() + " " + label.toString();
		}
		
		@Override
		public void visit(MethodVisitor mv)
		{
			mv.visitJumpInsn(super.opcode.getCode(), label);
		}
		
		private final Label label;
	}
	
	public static class InstructionLdc extends Instruction
	{
		public InstructionLdc(InstructionContainer container, Opcode opcode, Object constant)
		{
			super(container, opcode);
			this.constant = constant;
		}
		
		public Object getConstant()
		{
			return constant;
		}
		
		@Override
		public String toString()
		{
			return super.toString() + " " + constant;
		}
		
		@Override
		public void visit(MethodVisitor mv)
		{
			mv.visitLdcInsn(constant);
		}
		
		private final Object constant;
	}
	
	public static class InstructionIinc extends Instruction
	{
		public InstructionIinc(InstructionContainer container, Opcode opcode, int var, int increment)
		{
			super(container, opcode);
			this.var = var;
			this.increment = increment;
		}
		
		public int getVar()
		{
			return var;
		}
		
		public int getIncrement()
		{
			return increment;
		}
		
		@Override
		public String toString()
		{
			return super.toString() + " " + var + " " + increment;
		}
		
		@Override
		public void visit(MethodVisitor mv)
		{
			mv.visitIincInsn(var, increment);
		}
		
		private final int var;
		
		private final int increment;
	}
	
	public static class InstructionTableSwitch extends Instruction
	{
		public InstructionTableSwitch(InstructionContainer container, Opcode opcode, int min, int max,
				Label labelDefault, Label[] labels)
		{
			super(container, opcode);
			this.min = min;
			this.max = max;
			this.labelDefault = labelDefault;
			this.labels = labels;
		}
		
		public int getMin()
		{
			return min;
		}
		
		public int getMax()
		{
			return max;
		}
		
		public Label getLabelDefault()
		{
			return labelDefault;
		}
		
		public Label[] getLabels()
		{
			return labels;
		}
		
		@Override
		public String toString()
		{
			return super.toString() + " [" + min + ", " + max + "]";
		}
		
		@Override
		public void visit(MethodVisitor mv)
		{
			mv.visitTableSwitchInsn(min, max, labelDefault, labels);
		}
		
		private final int min;
		
		private final int max;
		
		private final Label labelDefault;
		
		private final Label[] labels;
	}
	
	public static class InstructionLookupSwitch extends Instruction
	{
		public InstructionLookupSwitch(InstructionContainer container, Opcode opcode, Label labelDefault, int[] keys, Label[] labels)
		{
			super(container, opcode);
			this.labelDefault = labelDefault;
			this.keys = keys;
			this.labels = labels;
		}
		
		public Label getLabelDefault()
		{
			return labelDefault;
		}
		
		public int[] getKeys()
		{
			return keys;
		}
		
		public Label[] getLabels()
		{
			return labels;
		}
		
		@Override
		public void visit(MethodVisitor mv)
		{
			mv.visitLookupSwitchInsn(labelDefault, keys, labels);
		}
		
		private final Label labelDefault;
		
		private final int[] keys;
		
		private final Label[] labels;
	}
	
	public static class InstructionMultiANewArray extends Instruction
	{
		public InstructionMultiANewArray(InstructionContainer container, Opcode opcode, String descriptor, int dimension)
		{
			super(container, opcode);
			this.descriptor = descriptor;
			this.dimension = dimension;
		}
		
		public String getDescriptor()
		{
			return descriptor;
		}
		
		public int getDimension()
		{
			return dimension;
		}
		
		public String toString()
		{
			return super.toString() + " " + descriptor + ":" + dimension;
		}
		
		@Override
		public void visit(MethodVisitor mv)
		{
			mv.visitMultiANewArrayInsn(descriptor, dimension);
		}
		
		private final String descriptor;
		
		private final int dimension;
	}
	
	public static class InstructionVoid extends Instruction
	{
		public InstructionVoid(InstructionContainer container, Opcode opcode) 
		{
			super(container, opcode);
		}
		
		@Override
		public void visit(MethodVisitor mv)
		{
			mv.visitInsn(super.opcode.getCode());
		}
	}
}
