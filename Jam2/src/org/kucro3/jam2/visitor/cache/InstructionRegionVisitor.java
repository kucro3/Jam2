package org.kucro3.jam2.visitor.cache;

import java.util.ArrayList;
import java.util.List;

import org.kucro3.jam2.opcode.Instruction.Frame;
import org.kucro3.jam2.util.annotation.Annotation;
import org.kucro3.jam2.util.annotation.Annotation.Value;
import org.kucro3.jam2.util.annotation.AnnotationContainer;
import org.kucro3.jam2.util.annotation.AnnotationValueAdapterVisitor;
import org.kucro3.jam2.util.annotation.InstructionAnnotation;
import org.kucro3.jam2.util.annotation.LocalVariableAnnotation;
import org.kucro3.jam2.util.annotation.ParameterAnnotation;
import org.kucro3.jam2.util.annotation.TypeAnnotation;
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
		AnnotationVisitor av = super.visitInsnAnnotation(typeRef, typePath, desc, visible);
		InstructionAnnotation ia = new InstructionAnnotation(typeRef, typePath, desc, visible);
		AnnotationCacheVisitor acv = new AnnotationCacheVisitor(av, ia);
		super.container.last().addInsnAnnotation(ia);
		return acv;
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
		LocalVariableAnnotation lva = new LocalVariableAnnotation(typeRef, typePath, start, end, index, desc, visible);
		AnnotationCacheVisitor acv = new AnnotationCacheVisitor(av, lva);
		localAnnotations.putAnnotation(lva);
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
		Value value = new Value(null);
		AnnotationValueAdapterVisitor avav = new AnnotationValueAdapterVisitor(av, value);
		annotations.setAnnotationDefault(value);
		return avav;
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible)
	{
		AnnotationVisitor av = super.visitAnnotation(desc, visible);
		Annotation anno = new Annotation(desc, visible);
		AnnotationCacheVisitor acv = new AnnotationCacheVisitor(av, anno);
		annotations.putAnnotation(anno);
		return acv;
	}
	
	@Override
	public AnnotationVisitor visitParameterAnnotation(int index, String desc, boolean visible)
	{
		AnnotationVisitor av = super.visitParameterAnnotation(index, desc, visible);
		ParameterAnnotation pa = new ParameterAnnotation(index, desc, visible);
		AnnotationCacheVisitor acv = new AnnotationCacheVisitor(av, pa);
		annotations.putAnnotation(pa);
		return acv;
	}
	
	@Override
	public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		AnnotationVisitor av = super.visitTypeAnnotation(typeRef, typePath, desc, visible);
		TypeAnnotation ta = new TypeAnnotation(typeRef, typePath, desc, visible);
		AnnotationCacheVisitor acv = new AnnotationCacheVisitor(av, ta);
		annotations.putAnnotation(ta);
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
		localAnnotations.visit(mv);
	}
	
	@Override
	public void revisitAnnotations(MethodVisitor mv)
	{
		annotations.visit(mv);
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
		localAnnotations.clearAnnotations();
		annotations.clearAnnotations();
	}
	
	protected final List<Parameter> params = new ArrayList<>();
	
	protected final List<LineNumber> lines = new ArrayList<>();
	
	protected final List<LocalVariable> locals = new ArrayList<>();
	
	protected final AnnotationContainer localAnnotations = new AnnotationContainer();
	
	protected final AnnotationContainer annotations = new AnnotationContainer();
	
	protected final AttributeCache attributes = new AttributeCache();
	
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
}