package org.kucro3.jam2.visitor;

import org.kucro3.jam2.opcode.Instruction;
import org.kucro3.jam2.visitor.cache.AnnotationCacheVisitor;
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
		AnnotationCacheVisitor acv = new AnnotationCacheVisitor(av.get());
		av.set(acv);
		return new MetadataVisitInsnAnnotation(typeRef, typePath, desc, visible, acv);
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
		AnnotationCacheVisitor acv = new AnnotationCacheVisitor(av.get());
		av.set(acv);
		return new MetadataVisitTryCatchAnnotation(typeRef, typePath, desc, visible, acv);
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
		AnnotationCacheVisitor acv = new AnnotationCacheVisitor(av.get());
		av.set(acv);
		return new MetadataVisitLocalVariableAnnotation(typeRef, typePath, start, end, index, desc, visible, acv);
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
	
	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible)
	{
		AnnotationVisitor av = super.visitAnnotation(desc, visible);
		Reference<AnnotationVisitor> ref = new Reference<>(av);
		Instruction insn = newVisitAnnotation(desc, visible, ref);
		if(insn != null)
			super.container.append(insn);
		return ref.get();
	}
	
	protected Instruction newVisitAnnotation(String desc, boolean visible, Reference<AnnotationVisitor> av)
	{
		AnnotationCacheVisitor acv = new AnnotationCacheVisitor(av.get());
		av.set(acv);
		return new MetadataVisitAnnotation(desc, visible, acv);
	}
	
	@Override
	public AnnotationVisitor visitParameterAnnotation(int index, String desc, boolean visible)
	{
		AnnotationVisitor av = super.visitParameterAnnotation(index, desc, visible);
		Reference<AnnotationVisitor> ref = new Reference<>(av);
		Instruction insn = newVisitParameterAnnotation(index, desc, visible, ref);
		if(insn != null)
			super.container.append(insn);
		return ref.get();
	}
	
	protected Instruction newVisitParameterAnnotation(int index, String desc, boolean visible, Reference<AnnotationVisitor> av)
	{
		AnnotationCacheVisitor acv = new AnnotationCacheVisitor(av.get());
		av.set(acv);
		return new MetadataVisitParameterAnnotation(index, desc, visible, acv);
	}
	
	@Override
	public void visitEnd()
	{
		super.container.append(new MetadataVisitEnd());
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
		public MetadataVisitInsnAnnotation(int typeRef, TypePath typePath, String desc, boolean visible,
				AnnotationCacheVisitor acv)
		{
			super(typeRef, typePath, desc, visible, acv);
		}
		
		@Override
		public void visit(MethodVisitor mv) 
		{
			acv.revisitOptional(mv.visitInsnAnnotation(typeRef, typePath, desc, visible));;
		}
	}
	
	public static class MetadataVisitTryCatchAnnotation extends MetadataTRAnnotationBase
	{
		public MetadataVisitTryCatchAnnotation(int typeRef, TypePath typePath, String desc, boolean visible,
				AnnotationCacheVisitor acv)
		{
			super(typeRef, typePath, desc, visible, acv);
		}
		
		@Override
		public void visit(MethodVisitor mv) 
		{
			acv.revisitOptional(mv.visitTryCatchAnnotation(typeRef, typePath, desc, visible));
		}
	}
	
	public abstract static class MetadataAnnotationBase extends Instruction
	{
		protected MetadataAnnotationBase(String desc, boolean visible, AnnotationCacheVisitor acv)
		{
			this.acv = acv;
			this.desc = desc;
			this.visible = visible;
		}
		
		protected final AnnotationCacheVisitor acv;
		
		protected String desc;
		
		protected boolean visible;
	}
	
	public abstract static class MetadataTRAnnotationBase extends MetadataAnnotationBase
	{
		protected MetadataTRAnnotationBase(int typeRef, TypePath typePath, String desc, boolean visible,
				AnnotationCacheVisitor acv)
		{
			super(desc, visible, acv);
			this.typeRef = typeRef;
			this.typePath = typePath;
		}
		
		protected int typeRef;
		
		protected TypePath typePath;
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
				Label[] end, int[] index, String desc, boolean visible, AnnotationCacheVisitor acv)
		{
			super(typeRef, typePath, desc, visible, acv);
			this.start = start;
			this.end = end;
			this.index = index;
		}

		@Override
		public void visit(MethodVisitor mv) 
		{
			acv.revisitOptional(mv.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, desc, visible));
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
			this.acv.revisitOptional(mv.visitAnnotationDefault());
		}
		
		protected final AnnotationCacheVisitor acv;
	}
	
	public static class MetadataVisitAnnotation extends MetadataAnnotationBase
	{
		public MetadataVisitAnnotation(String desc, boolean visible, AnnotationCacheVisitor acv)
		{
			super(desc, visible, acv);
		}
		
		@Override
		public void visit(MethodVisitor mv)
		{
			this.acv.revisitOptional(mv.visitAnnotation(desc, visible));
		}
	}
	
	public static class MetadataVisitParameterAnnotation extends MetadataAnnotationBase
	{
		public MetadataVisitParameterAnnotation(int parameter, String desc, boolean visible, AnnotationCacheVisitor acv)
		{
			super(desc, visible, acv);
			this.parameter = parameter;
		}

		@Override
		public void visit(MethodVisitor mv) 
		{
			this.acv.revisitOptional(mv.visitParameterAnnotation(parameter, desc, visible));
		}
		
		protected int parameter;
	}
	
	public static class MetadataVisitTypeAnnotation extends MetadataTRAnnotationBase
	{
		public MetadataVisitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible, AnnotationCacheVisitor acv)
		{
			super(typeRef, typePath, desc, visible, acv);
		}
		
		@Override
		public void visit(MethodVisitor mv) 
		{
			this.acv.revisitOptional(mv.visitTypeAnnotation(typeRef, typePath, desc, visible));
		}
	}
	
	public static class MetadataVisitEnd extends Instruction
	{
		@Override
		public void visit(MethodVisitor mv) 
		{
			mv.visitEnd();
		}
	}
}