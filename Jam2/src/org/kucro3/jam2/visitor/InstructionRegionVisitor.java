package org.kucro3.jam2.visitor;

import org.kucro3.jam2.opcode.Instruction;
import org.kucro3.util.Reference;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.TypePath;

public class InstructionRegionVisitor extends InstructionFlowVisitor {
	public InstructionRegionVisitor()
	{
	}
	
	public InstructionRegionVisitor(MethodVisitor mv)
	{
		super(mv);
	}
	
	@Override
	public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack)
	{
		super.visitFrame(type, nLocal, local, nStack, stack);
		super.container.append(new MetadataVisitFrame(type, nLocal, local, nStack, stack));
	}
	
	@Override
	public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		AnnotationVisitor av = super.visitInsnAnnotation(typeRef, typePath, desc, visible);
		Reference<AnnotationVisitor> ref = new Reference<>(av);
		Instruction insn = newVisitInsnAnnotation(typeRef, typePath, desc, visible, ref);
		if(insn != null)
			super.container.append(insn);
		return ref.get();
	}
	
	protected Instruction newVisitInsnAnnotation(int typeRef, TypePath typePath, String desc, boolean visible,
			Reference<AnnotationVisitor> av)
	{
		return new MetadataVisitInsnAnnotation(typeRef, typePath, desc, visible);
	}
	
	@Override
	public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		AnnotationVisitor av = super.visitTryCatchAnnotation(typeRef, typePath, desc, visible);
		Reference<AnnotationVisitor> ref = new Reference<>(av);
		Instruction insn = newVisitTryCatchAnnotation(typeRef, typePath, desc, visible, ref);
		if(insn != null)
			super.container.append(insn);
		return ref.get();
	}
	
	protected Instruction newVisitTryCatchAnnotation(int typeRef, TypePath typePath, String desc, boolean visible,
			Reference<AnnotationVisitor> av)
	{
		return new MetadataVisitTryCatchAnnotation(typeRef, typePath, desc, visible);
	}
	
	@Override
	public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index)
	{
		super.visitLocalVariable(name, desc, signature, start, end, index);
		super.container.append(new MetadataVisitLocalVariable(name, desc, signature, start, end, index));
	}
	
	@Override
	public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, 
				Label[] end, int[] index, String desc, boolean visible)
	{
		AnnotationVisitor av = super.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, desc, visible);
		Reference<AnnotationVisitor> ref = new Reference<>(av);
		Instruction insn = newVisitLocalVariableAnnotation(typeRef, typePath, start, end, index, desc, visible, ref);
		if(insn != null)
			super.container.append(insn);
		return ref.get();
	}
	
	protected Instruction newVisitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, 
				Label[] end, int[] index, String desc, boolean visible, Reference<AnnotationVisitor> av)
	{
		return new MetadataVisitLocalVariableAnnotation(typeRef, typePath, start, end, index, desc, visible);
	}
	
	@Override
	public void visitLineNumber(int line, Label start)
	{
		super.visitLineNumber(line, start);
		super.container.append(new MetadataVisitLineNumber(line, start));
	}
	
	@Override
	public void visitParameter(String name, int access)
	{
		super.visitParameter(name, access);
		super.container.append(new MetadataVisitParameter(name, access));
	}
	
	@Override
	public AnnotationVisitor visitAnnotationDefault()
	{
		AnnotationVisitor av = super.visitAnnotationDefault();
		Reference<AnnotationVisitor> ref = new Reference<>(av);
		Instruction insn = newVisitAnnotationDefault(ref);
		if(insn != null)
			super.container.append(insn);
		return ref.get();
	}
	
	protected Instruction newVisitAnnotationDefault(Reference<AnnotationVisitor> av)
	{
		AnnotationCacheVisitor acv = new AnnotationCacheVisitor(av.get());
		av.set(acv);
		return new MetadataVisitAnnotationDefault(acv);
	}
	
	public static class MetadataVisitFrame extends Instruction
	{
		public MetadataVisitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack)
		{
			this.type = type;
			this.nLocal = nLocal;
			this.local = local;
			this.nStack = nStack;
			this.stack = stack;
		}

		@Override
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
	
	public static class MetadataVisitInsnAnnotation extends MetadataTRAnnotationBase
	{
		public MetadataVisitInsnAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
		{
			super(typeRef, typePath, desc, visible);
		}
		
		@Override
		public void visit(MethodVisitor mv) 
		{
			mv.visitInsnAnnotation(typeRef, typePath, desc, visible);
		}
	}
	
	public static class MetadataVisitTryCatchAnnotation extends MetadataTRAnnotationBase
	{
		public MetadataVisitTryCatchAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
		{
			super(typeRef, typePath, desc, visible);
		}
		
		@Override
		public void visit(MethodVisitor mv) 
		{
			mv.visitTryCatchAnnotation(typeRef, typePath, desc, visible);
		}
	}
	
	public abstract static class MetadataTRAnnotationBase extends Instruction
	{
		protected MetadataTRAnnotationBase(int typeRef, TypePath typePath, String desc, boolean visible)
		{
			this.typeRef = typeRef;
			this.typePath = typePath;
			this.desc = desc;
			this.visible = visible;
		}
		
		protected int typeRef;
		
		protected TypePath typePath;
		
		protected String desc;
		
		protected boolean visible;
	}
	
	public static class MetadataVisitLocalVariable extends Instruction
	{
		public MetadataVisitLocalVariable(String name, String desc, String signature, Label start, Label end, int index)
		{
			this.name = name;
			this.desc = desc;
			this.signature = signature;
			this.start = start;
			this.end = end;
			this.index = index;
		}
		
		@Override
		public void visit(MethodVisitor mv)
		{
			mv.visitLocalVariable(name, desc, signature, start, end, index);
		}
		
		protected String name;
		
		protected String desc;
		
		protected String signature;
		
		protected Label start;
		
		protected Label end;
		
		protected int index;
	}
	
	public static class MetadataVisitLocalVariableAnnotation extends MetadataTRAnnotationBase
	{
		public MetadataVisitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, 
				Label[] end, int[] index, String desc, boolean visible)
		{
			super(typeRef, typePath, desc, visible);
			this.start = start;
			this.end = end;
			this.index = index;
		}

		@Override
		public void visit(MethodVisitor mv) 
		{
			mv.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, desc, visible);
		}
		
		protected Label[] start;
		
		protected Label[] end;
		
		protected int[] index;
	}
	
	public static class MetadataVisitLineNumber extends Instruction
	{
		public MetadataVisitLineNumber(int line, Label start)
		{
			this.line = line;
			this.start = start;
		}
		
		@Override
		public void visit(MethodVisitor mv) 
		{
			mv.visitLineNumber(line, start);
		}
		
		protected int line;
		
		protected Label start;
	}
	
	public static class MetadataVisitParameter extends Instruction
	{
		public MetadataVisitParameter(String name, int access)
		{
			this.name = name;
			this.access = access;
		}

		@Override
		public void visit(MethodVisitor mv) 
		{
			mv.visitParameter(name, access);
		}
		
		protected String name;
		
		protected int access;
	}
	
	public static class MetadataVisitAnnotationDefault extends Instruction
	{
		public MetadataVisitAnnotationDefault(AnnotationCacheVisitor acv)
		{
			this.acv = acv;
		}
		
		@Override
		public void visit(MethodVisitor mv) 
		{
			AnnotationVisitor av = mv.visitAnnotationDefault();
			this.acv.revisit(av);
		}
		
		protected final AnnotationCacheVisitor acv;
	}
}