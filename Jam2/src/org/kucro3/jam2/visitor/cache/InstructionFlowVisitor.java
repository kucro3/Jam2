package org.kucro3.jam2.visitor.cache;

import java.util.ArrayList;
import java.util.List;

import org.kucro3.jam2.visitor.InstructionVisitor;
import org.kucro3.jam2.visitor.cache.AnnotationCacheVisitor;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.TypePath;

public class InstructionFlowVisitor extends InstructionVisitor implements MethodRevisitable {
	public InstructionFlowVisitor()
	{
	}
	
	public InstructionFlowVisitor(MethodVisitor mv)
	{
		super(mv);
	}
	
	@Override
	public void visitEnd()
	{
		this.endVisited = true;
	}
	
	@Override
	public void clear()
	{
		super.clear();
		this.maxStacks = 0;
		this.maxLocals = 0;
		this.endVisited = false;
		this.tryCatchBlocks.clear();
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
		super.container.visitLabel(label);
	}
	
	@Override
	public void visitTryCatchBlock(Label start, Label end, Label handler, String type)
	{
		super.visitTryCatchBlock(start, end, handler, type);
		this.tryCatchBlocks.add(new TryCatchBlock(start, end, handler, type));
	}
	
	@Override
	public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		if(tryCatchBlocks.size() == 0)
			return null;
		else
		{
			AnnotationCacheVisitor acv = newTryCatchAnnotation(
					super.visitTryCatchAnnotation(typeRef, typePath, desc, visible));
			tryCatchBlocks.get(tryCatchBlocks.size() - 1)
					.annos.add(new TryCatchBlockAnnotation(typeRef, typePath, desc, visible, acv));
			return acv;
		}
	}
	
	protected AnnotationCacheVisitor newTryCatchAnnotation(AnnotationVisitor av)
	{
		return new AnnotationCacheVisitor(av);
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
	
	@Override
	public void revisitTryCatchBlocks(MethodVisitor mv)
	{
		for(TryCatchBlock tcb : tryCatchBlocks)
			tcb.visit(mv);
	}
	
	@Override
	public void revisitMaxs(MethodVisitor mv)
	{
		mv.visitMaxs(maxStacks, maxLocals);
	}
	
	@Override
	public void revisitEnd(MethodVisitor mv)
	{
		if(endVisited)
			mv.visitEnd();
	}
	
	@Override
	public void revisitInstructions(MethodVisitor mv)
	{
		super.container.revisit(mv);
	}
	
	protected int maxStacks;
	
	protected int maxLocals;
	
	protected boolean endVisited;
	
	protected final List<TryCatchBlock> tryCatchBlocks = new ArrayList<>();
	
	public class TryCatchBlock
	{
		public TryCatchBlock(Label start, Label end, Label handler, String type)
		{
			this.start = start;
			this.end = end;
			this.handler = handler;
			this.type = type;
		}
		
		public void visit(MethodVisitor mv)
		{
			mv.visitTryCatchBlock(start, end, handler, type);
			for(TryCatchBlockAnnotation anno : annos)
				anno.visit(mv);
		}
		
		protected final Label start;
		
		protected final Label end;
		
		protected final Label handler;
		
		protected final String type;
		
		protected final List<TryCatchBlockAnnotation> annos = new ArrayList<>();
	}
	
	public class TryCatchBlockAnnotation
	{
		TryCatchBlockAnnotation(int typeRef, TypePath typePath, String desc, boolean visible, AnnotationCacheVisitor acv)
		{
			this.typeRef = typeRef;
			this.typePath = typePath;
			this.desc = desc;
			this.visible = visible;
			this.acv = acv;
		}
		
		public void visit(MethodVisitor mv)
		{
			acv.revisitOptional(mv.visitTryCatchAnnotation(typeRef, typePath, desc, visible));
		}
		
		protected final int typeRef;
		
		protected final TypePath typePath;
		
		protected final String desc;
		
		protected final boolean visible;
		
		protected final AnnotationCacheVisitor acv;
	}
}