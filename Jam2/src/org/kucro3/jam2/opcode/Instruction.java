package org.kucro3.jam2.opcode;

import java.util.List;
import java.util.ArrayList;

import org.kucro3.jam2.opcode.Opcode;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.TypePath;

public abstract class Instruction {
	protected Instruction(Opcode opcode)
	{
		this.opcode = opcode;
		
		if(opcode.getForm() == OpcodeForm.VAR_CST)
			throw new IllegalArgumentException("Opcode \"" + opcode.getName() + "\" is not supported in ASM");
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
	
	public void preFrame(Frame frame)
	{
		this.frame = frame;
	}
	
	public void setLabel(Label label)
	{
		this.label =  label;
	}
	
	public void addInsnAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		insnAnnos.add(new InsnAnnotation(typeRef, typePath, desc, visible));
	}
	
	public void addInsnAnnotation(InsnAnnotation ia)
	{
		insnAnnos.add(ia);
	}
	
	public final void visitFully(MethodVisitor mv)
	{
		if(frame != null)
			frame.visit(mv);
		visit(mv);
		if(label != null)
			mv.visitLabel(label);
		for(InsnAnnotation ia : insnAnnos)
			ia.visit(mv);
	}
	
	public abstract void visit(MethodVisitor mv);
	
	private final Opcode opcode;
	
	private Frame frame;
	
	private final List<InsnAnnotation> insnAnnos = new ArrayList<>();
	
	private Label label;
	
	public static class Frame
	{
		public Frame(int type, int nLocal, Object[] local, int nStack, Object[] stack)
		{
			this.type = type;
			this.nLocal = nLocal;
			this.local = local;
			this.nStack = nStack;
			this.stack = stack;
		}
		
		public void visit(MethodVisitor mv)
		{
			mv.visitFrame(type, nLocal, local, nStack, stack);
		}
		
		protected int type;
		
		protected int nLocal;
		
		protected Object[] local;
		
		protected int nStack;
		
		protected Object[] stack;
	}
	
	public static class InsnAnnotation
	{
		public InsnAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
		{
			this.typeRef = typeRef;
			this.typePath = typePath;
			this.desc = desc;
			this.visible = visible;
		}
		
		public void visit(MethodVisitor mv)
		{
			mv.visitInsnAnnotation(typeRef, typePath, desc, visible);
		}
		
		protected int typeRef;
		
		protected TypePath typePath;
		
		protected String desc;
		
		protected boolean visible;
	}
	
	public static class InstructionInt extends Instruction
	{
		public InstructionInt(Opcode opcode, int operand) 
		{
			super(opcode);
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
		
		protected int operand;
	}
	
	public static class InstructionVar extends Instruction
	{
		public InstructionVar(Opcode opcode, int var) 
		{
			super(opcode);
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

		protected int var;
	}	
	
	public static class InstructionType extends Instruction
	{
		public InstructionType(Opcode opcode, String type) 
		{
			super(opcode);
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
		
		protected String type;
	}
	
	public static class InstructionField extends Instruction
	{
		public InstructionField(Opcode opcode, String owner, String name, String descriptor)
		{
			super(opcode);
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
		
		protected String owner;
		
		protected String name;
		
		protected String descriptor;
	}
	
	public static class InstructionMethod extends Instruction
	{
		public InstructionMethod(Opcode opcode, String owner, String name, String descriptor, boolean ifInterface)
		{
			super(opcode);
			this.owner = owner;
			this.name = name;
			this.descriptor = descriptor;
			this.ifInterface = ifInterface;
		}
		
		InstructionMethod( Opcode opcode, String owner, String name, String descriptor)
		{
			this(opcode, owner, name, descriptor, false);
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
		
		protected boolean ifInterface;
		
		protected String owner;
		
		protected String name;
		
		protected String descriptor;
	}
	
	public static class InstructionInvokeDynamic extends Instruction
	{
		public InstructionInvokeDynamic(Opcode opcode, String name, String descriptor,
				Handle bootstrapMethod, Object[] bootstrapArguments)
		{
			super(opcode);
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
		
		protected String name;
		
		protected String descriptor;
		
		protected Handle bootstrapMethod;
		
		protected Object[] bootstrapArguments;
	}
	
	public static class InstructionJump extends Instruction
	{
		public InstructionJump(Opcode opcode, Label label)
		{
			super(opcode);
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
		
		protected Label label;
	}
	
	public static class InstructionLdc extends Instruction
	{
		public InstructionLdc(Opcode opcode, Object constant)
		{
			super(opcode);
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
		
		protected Object constant;
	}
	
	public static class InstructionIinc extends Instruction
	{
		public InstructionIinc(Opcode opcode, int var, int increment)
		{
			super(opcode);
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
		
		protected int var;
		
		protected int increment;
	}
	
	public static class InstructionTableSwitch extends Instruction
	{
		public InstructionTableSwitch(Opcode opcode, int min, int max,
				Label labelDefault, Label[] labels)
		{
			super(opcode);
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
		
		protected int min;
		
		protected int max;
		
		protected Label labelDefault;
		
		protected Label[] labels;
	}
	
	public static class InstructionLookupSwitch extends Instruction
	{
		public InstructionLookupSwitch(Opcode opcode, Label labelDefault, int[] keys, Label[] labels)
		{
			super(opcode);
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
		
		protected Label labelDefault;
		
		protected int[] keys;
		
		protected Label[] labels;
	}
	
	public static class InstructionMultiANewArray extends Instruction
	{
		public InstructionMultiANewArray(Opcode opcode, String descriptor, int dimension)
		{
			super(opcode);
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
		
		protected String descriptor;
		
		protected int dimension;
	}
	
	public static class InstructionVoid extends Instruction
	{
		public InstructionVoid(Opcode opcode) 
		{
			super(opcode);
		}
		
		@Override
		public void visit(MethodVisitor mv)
		{
			mv.visitInsn(super.opcode.getCode());
		}
	}
}
