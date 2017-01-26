package org.kucro3.jam2.visitor.cache;

import java.util.ArrayList;
import java.util.List;

import org.kucro3.jam2.opcode.Instruction.Frame;
import org.kucro3.jam2.opcode.Instruction.InsnAnnotation;
import org.kucro3.jam2.visitor.cache.AnnotationCacheVisitor;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
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
		super.container.setNextFrame(new Frame(type, nLocal, local, nStack, stack));
	}
	
	@Override
	public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		AnnotationCacheVisitor acv = newVisitInsnAnnotation(super.visitInsnAnnotation(typeRef, typePath, desc, visible));
		super.container.last().addInsnAnnotation(new CachedInsnAnnotation(typeRef, typePath, desc, visible, acv));
		return acv;
	}
	
	protected AnnotationCacheVisitor newVisitInsnAnnotation(AnnotationVisitor av)
	{
		return new AnnotationCacheVisitor(av);
	}
	
	@Override
	public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index)
	{
		super.visitLocalVariable(name, desc, signature, start, end, index);
		this.locals.add(new LocalVariable(name, desc, signature, start, end, index));
	}
	
	@Override
	public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, 
				Label[] end, int[] index, String desc, boolean visible)
	{
		AnnotationVisitor av = super.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, desc, visible);
		AnnotationCacheVisitor acv = new AnnotationCacheVisitor(av);
		LocalVariableAnnotation lva = new LocalVariableAnnotation(typeRef, typePath, start, end, index, desc, visible, acv);
		localAnnotations.add(lva);
		return acv;
	}
	
	@Override
	public void visitLineNumber(int line, Label start)
	{
		super.visitLineNumber(line, start);
		this.lines.add(new LineNumber(line, start));
	}
	
	@Override
	public void visitParameter(String name, int access)
	{
		super.visitParameter(name, access);
		this.params.add(new Parameter(name, access));
	}
	
	@Override
	public AnnotationVisitor visitAnnotationDefault()
	{
		AnnotationVisitor av = super.visitAnnotationDefault();
		AnnotationCacheVisitor acv = new AnnotationCacheVisitor(av);
		return annotationDefault = acv;
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible)
	{
		AnnotationVisitor av = super.visitAnnotation(desc, visible);
		AnnotationCacheVisitor acv = new AnnotationCacheVisitor(av);
		Annotation anno = new Annotation(desc, visible, acv);
		annotations.add(anno);
		return acv;
	}
	
	@Override
	public AnnotationVisitor visitParameterAnnotation(int index, String desc, boolean visible)
	{
		AnnotationVisitor av = super.visitParameterAnnotation(index, desc, visible);
		AnnotationCacheVisitor acv = new AnnotationCacheVisitor(av);
		ParameterAnnotation pa = new ParameterAnnotation(index, desc, visible, acv);
		annotations.add(pa);
		return acv;
	}
	
	@Override
	public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		AnnotationVisitor av = super.visitTypeAnnotation(typeRef, typePath, desc, visible);
		AnnotationCacheVisitor acv = new AnnotationCacheVisitor(av);
		TypeAnnotation ta = new TypeAnnotation(typeRef, typePath, desc, visible, acv);
		annotations.add(ta);
		return acv;
	}
	
	@Override
	public void visitAttribute(Attribute attr)
	{
		super.visitAttribute(attr);
		attributes.visitAttribute(attr);
	}
	
	@Override
	public void revisitParameters(MethodVisitor mv)
	{
		for(Parameter param : params)
			param.visit(mv);
	}
	
	@Override
	public void revisitLineNumbers(MethodVisitor mv)
	{
		for(LineNumber line : lines)
			line.visit(mv);
	}
	
	@Override
	public void revisitLocals(MethodVisitor mv)
	{
		for(LocalVariable local : locals)
			local.visit(mv);
	}
	
	@Override
	public void revisitLocalAnnotations(MethodVisitor mv)
	{
		for(LocalVariableAnnotation localAnnotation : localAnnotations)
			localAnnotation.visit(mv);
	}
	
	@Override
	public void revisitAnnotationDefault(MethodVisitor mv)
	{
		if(annotationDefault != null)
			annotationDefault.revisitOptional(mv.visitAnnotationDefault());
	}
	
	@Override
	public void revisitAnnotations(MethodVisitor mv)
	{
		for(Annotation annotation : annotations)
			annotation.visit(mv);
	}
	
	@Override
	public void revisitAttributes(MethodVisitor mv)
	{
		attributes.revisit(mv);
	}
	
	@Override
	public void clear()
	{
		super.clear();
		params.clear();
		lines.clear();
		locals.clear();
		localAnnotations.clear();
		annotations.clear();
		annotationDefault =  null;
	}
	
	protected final List<Parameter> params = new ArrayList<>();
	
	protected final List<LineNumber> lines = new ArrayList<>();
	
	protected final List<LocalVariable> locals = new ArrayList<>();
	
	protected final List<LocalVariableAnnotation> localAnnotations = new ArrayList<>();
	
	protected final List<Annotation> annotations = new ArrayList<>();
	
	protected final AttributeCache attributes = new AttributeCache();
	
	protected AnnotationCacheVisitor annotationDefault;
	
	public class CachedInsnAnnotation extends InsnAnnotation
	{
		public CachedInsnAnnotation(int typeRef, TypePath typePath, String desc, boolean visible,
				AnnotationCacheVisitor acv)
		{
			super(typeRef, typePath, desc, visible);
			this.acv = acv;
		}
		
		@Override
		public void visit(MethodVisitor mv) 
		{
			acv.revisitOptional(mv.visitInsnAnnotation(typeRef, typePath, desc, visible));
		}
		
		protected final AnnotationCacheVisitor acv;
	}
	
	public class LocalVariable
	{
		public LocalVariable(String name, String desc, String signature, Label start, Label end, int index)
		{
			this.name = name;
			this.desc = desc;
			this.signature = signature;
			this.start = start;
			this.end = end;
			this.index = index;
		}
		
		public void visit(MethodVisitor mv)
		{
			mv.visitLocalVariable(name, desc, signature, start, end, index);
		}
		
		protected final String name;
		
		protected final String desc;
		
		protected final String signature;
		
		protected final Label start;
		
		protected final Label end;
		
		protected final int index;
	}
	
	public class LocalVariableAnnotation
	{
		public LocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end,
				int[] index, String desc, boolean visible, AnnotationCacheVisitor acv)
		{
			this.typeRef = typeRef;
			this.typePath = typePath;
			this.start = start;
			this.end = end;
			this.index = index;
			this.desc = desc;
			this.visible = visible;
			this.acv = acv;
		}
		
		public void visit(MethodVisitor mv)
		{
			acv.revisitOptional(mv.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, desc, visible));
		}
		
		protected final int typeRef;
		
		protected final TypePath typePath;
		
		protected final Label[] start;
		
		protected final Label[] end;
		
		protected final int[] index;
		
		protected final String desc;
		
		protected final boolean visible;
		
		protected final AnnotationCacheVisitor acv;
	}
	
	public class LineNumber
	{
		public LineNumber(int line, Label start)
		{
			this.line = line;
			this.start = start;
		}
		
		public void visit(MethodVisitor mv)
		{
			mv.visitLineNumber(line, start);
		}
		
		protected final int line;
		
		protected final Label start;
	}
	
	public class Parameter
	{
		public Parameter(String name, int access)
		{
			this.name = name;
			this.access = access;
		}
		
		public void visit(MethodVisitor mv)
		{
			mv.visitParameter(name, access);
		}
		
		protected final String name;
		
		protected final int access;
	}
	
	public class Annotation
	{
		public Annotation(String desc, boolean visible, AnnotationCacheVisitor acv)
		{
			this.desc = desc;
			this.visible = visible;
			this.acv = acv;
		}
		
		public void visit(MethodVisitor mv)
		{
			acv.revisitOptional(mv.visitAnnotation(desc, visible));
		}
		
		protected final String desc;
		
		protected final boolean visible;
	
		protected final AnnotationCacheVisitor acv;
	}
	
	public class ParameterAnnotation extends Annotation
	{
		public ParameterAnnotation(int parameter, String desc, boolean visible, AnnotationCacheVisitor acv)
		{
			super(desc, visible, acv);
			this.parameter = parameter;
		}
		
		@Override
		public void visit(MethodVisitor mv)
		{
			acv.revisitOptional(mv.visitParameterAnnotation(parameter, desc, visible));
		}
		
		protected final int parameter;
	}
	
	public class TypeAnnotation extends Annotation
	{
		public TypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible, AnnotationCacheVisitor acv)
		{
			super(desc, visible, acv);
			this.typeRef = typeRef;
			this.typePath = typePath;
		}
		
		@Override
		public void visit(MethodVisitor mv)
		{
			acv.revisitOptional(mv.visitTypeAnnotation(typeRef, typePath, desc, visible));
		}
		
		protected final int typeRef;
		
		protected final TypePath typePath;
	}
}