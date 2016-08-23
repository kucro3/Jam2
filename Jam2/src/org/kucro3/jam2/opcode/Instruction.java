package org.kucro3.jam2.opcode;

import org.kucro3.jam2.opcode.Opcode;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;

public abstract class Instruction {
	Instruction(InstructionContainer container, Opcode opcode)
	{
		this.container = container;
		this.opcode = opcode;
	}
	
	public InstructionContainer getContainer()
	{
		return container;
	}
	
	public Opcode getOpcode()
	{
		return opcode;
	}
	
	public String toString()
	{
		return opcode.getName();
	}
	
	private final InstructionContainer container;
	
	private final Opcode opcode;
	
	public static class InstructionInt extends Instruction
	{
		InstructionInt(InstructionContainer container, Opcode opcode, int operand) 
		{
			super(container, opcode);
			this.operand = operand;
		}
		
		public int getOperand()
		{
			return operand;
		}
		
		public String toString()
		{
			return super.toString() + " " + operand;
		}
		
		private final int operand;
	}
	
	public static class InstructionVar extends Instruction
	{
		InstructionVar(InstructionContainer container, Opcode opcode, int var) 
		{
			super(container, opcode);
			this.var = var;
		}
		
		public int getVar()
		{
			return var;
		}
		
		public String toString()
		{
			return super.toString() + " " + var;
		}

		private final int var;
	}	
	
	public static class InstructionType extends Instruction
	{
		InstructionType(InstructionContainer container, Opcode opcode, String descriptor) 
		{
			super(container, opcode);
			this.descriptor = descriptor;
		}
		
		public String getDescriptor()
		{
			return descriptor;
		}
		
		public String toString()
		{
			return super.toString() + " " + descriptor;
		}
		
		private final String descriptor;
	}
	
	public static class InstructionField extends Instruction
	{
		InstructionField(InstructionContainer container, Opcode opcode, String owner, String name, String descriptor)
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
		
		private final String owner;
		
		private final String name;
		
		private final String descriptor;
		
		public String toString()
		{
			return super.toString() + " " + owner + "#" + name + ":" + descriptor;
		}
	}
	
	public static class InstructionMethod extends Instruction
	{
		InstructionMethod(InstructionContainer container, Opcode opcode, String owner, String name, String descriptor, boolean ifInterface)
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
		
		public String toString()
		{
			return super.toString() + " " + owner + "#" + name + ":" + descriptor;
		}
		
		private final boolean ifInterface;
		
		private final String owner;
		
		private final String name;
		
		private final String descriptor;
	}
	
	public static class InstructionInvokeDynamic extends Instruction
	{
		InstructionInvokeDynamic(InstructionContainer container, Opcode opcode, String name, String descriptor,
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
		
		public String toString()
		{
			return super.toString() + " " + name + ":" + descriptor + "(" + bootstrapMethod + ":" + bootstrapArguments.length + ")";
		}
		
		private final String name;
		
		private final String descriptor;
		
		private final Handle bootstrapMethod;
		
		private final Object[] bootstrapArguments;
	}
	
	public static class InstructionJump extends Instruction
	{
		InstructionJump(InstructionContainer container, Opcode opcode, Label label)
		{
			super(container, opcode);
			this.label = label;
		}
		
		public Label getLabel()
		{
			return label;
		}
		
		public String toString()
		{
			return super.toString() + " " + label;
		}
		
		private final Label label;
	}
	
	public static class InstructionLdc extends Instruction
	{
		InstructionLdc(InstructionContainer container, Opcode opcode, Object constant)
		{
			super(container, opcode);
			this.constant = constant;
		}
		
		public Object getConstant()
		{
			return constant;
		}
		
		public String toString()
		{
			return super.toString() + " " + constant;
		}
		
		private final Object constant;
	}
	
	public static class InstructionIinc extends Instruction
	{
		InstructionIinc(InstructionContainer container, Opcode opcode, int var, int increment)
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
		
		public String toString()
		{
			return super.toString() + " " + var + " " + increment;
		}
		
		private final int var;
		
		private final int increment;
	}
	
	public static class InstructionTableSwitch extends Instruction
	{
		InstructionTableSwitch(InstructionContainer container, Opcode opcode, int min, int max,
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
		
		public String toString()
		{
			return super.toString() + " [" + min + ", " + max + "]";
		}
		
		private final int min;
		
		private final int max;
		
		private final Label labelDefault;
		
		private final Label[] labels;
	}
	
	public static class InstructionLookupSwitch extends Instruction
	{
		InstructionLookupSwitch(InstructionContainer container, Opcode opcode, Label labelDefault, int[] keys, Label[] labels)
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
		
		private final Label labelDefault;
		
		private final int[] keys;
		
		private final Label[] labels;
	}
	
	public static class InstructionMultiANewArray extends Instruction
	{
		InstructionMultiANewArray(InstructionContainer container, Opcode opcode, String descriptor, int dimension)
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
		
		private final String descriptor;
		
		private final int dimension;
	}
	
	public static class InstructionVoid extends Instruction
	{
		InstructionVoid(InstructionContainer container, Opcode opcode) 
		{
			super(container, opcode);
		}
	}
}
