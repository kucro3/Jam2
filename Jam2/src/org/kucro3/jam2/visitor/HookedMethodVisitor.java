package org.kucro3.jam2.visitor;

import org.kucro3.jam2.util.ClassContext;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.TypePath;

public class HookedMethodVisitor extends MethodVisitor implements IHookedMethodVisitor {
	public HookedMethodVisitor(MethodVisitor mv)
	{
		super(ClassContext.API, mv);
		this.mv = mv;
	}
	
	public HookedMethodVisitor(MethodVisitor mv, MethodVisitorListener listener)
	{
		this(mv);
		setListener(listener);
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String descriptor, boolean visible)
	{
		if(listener.onVisitAnnotation(mv, descriptor, visible))
			return super.visitAnnotation(descriptor, visible);
		throw new HookedInterruption();
	}
	
	@Override
	public AnnotationVisitor visitAnnotationDefault()
	{
		if(listener.onVisitAnnotationDefault(mv))
			return super.visitAnnotationDefault();
		throw new HookedInterruption();
	}
	
	@Override
	public void visitAttribute(Attribute attribute)
	{
		if(listener.onVisitAttribute(mv, attribute))
			super.visitAttribute(attribute);
	}
	
	@Override
	public void visitCode()
	{
		if(listener.onVisitCode(mv))
			super.visitCode();
	}
	
	@Override
	public void visitEnd()
	{
		if(listener.onVisitEnd(mv))
			super.visitEnd();
	}
	
	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String descriptor)
	{
		if(listener.onVisitFieldInsn(mv, opcode, owner, name, descriptor))
			super.visitFieldInsn(opcode, owner, name, descriptor);
	}
	
	@Override
	public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack)
	{
		if(listener.onVisitFrame(mv, type, nLocal, local, nStack, stack))
			super.visitFrame(type, nLocal, local, nStack, stack);
	}
	
	@Override
	public void visitIincInsn(int var, int increment)
	{
		if(listener.onVisitIincInsn(mv, var, increment))
			super.visitIincInsn(var, increment);
	}
	
	@Override
	public void visitInsn(int opcode)
	{
		if(listener.onVisitInsn(mv, opcode))
			super.visitInsn(opcode);
	}
	
	@Override
	public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		if(listener.onVisitInsnAnnotation(mv, typeRef, typePath, desc, visible))
			return super.visitInsnAnnotation(typeRef, typePath, desc, visible);
		throw new HookedInterruption();
	}
	
	@Override
	public void visitIntInsn(int opcode, int operand)
	{
		if(listener.onVisitIntInsn(mv, opcode, operand))
			super.visitIntInsn(opcode, operand);
	}
	
	@Override
	public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs)
	{
		if(listener.onVisitInvokeDynamicInsn(mv, name, desc, bsm, bsmArgs))
			super.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
	}
	
	@Override
	public void visitJumpInsn(int opcode, Label label)
	{
		if(listener.onVisitJumpInsn(mv, opcode, label))
			super.visitJumpInsn(opcode, label);
	}
	
	@Override
	public void visitLabel(Label label)
	{
		if(listener.onVisitLabel(mv, label))
			super.visitLabel(label);
	}
	
	@Override
	public void visitLdcInsn(Object cst)
	{
		if(listener.onVisitLdcInsn(mv, cst))
			super.visitLdcInsn(cst);
	}
	
	@Override
	public void visitLineNumber(int line, Label start)
	{
		if(listener.onVisitLineNumber(mv, line, start))
			super.visitLineNumber(line, start);
	}
	
	@Override
	public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index)
	{
		if(listener.onVisitLocalVariable(mv, name, desc, signature, start, end, index))
			super.visitLocalVariable(name, desc, signature, start, end, index);
	}
	
	@Override
	public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end,
			int[] index, String desc, boolean visible)
	{
		if(listener.onVisitLocalVariableAnnotation(mv, typeRef, typePath, start, end, index, desc, visible))
			return super.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, desc, visible);
		throw new HookedInterruption();
	}
	
	@Override
	public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels)
	{
		if(listener.onVisitLookupSwitchInsn(mv, dflt, keys, labels))
			super.visitLookupSwitchInsn(dflt, keys, labels);
	}
	
	@Override
	public void visitMaxs(int maxStack, int maxLocals)
	{
		if(listener.onVisitMaxs(mv, maxStack, maxLocals))
			super.visitMaxs(maxStack, maxLocals);
	}
	
	@Deprecated
	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String descriptor)
	{
		boolean ifInterface = opcode == Opcodes.INVOKEINTERFACE;
		visitMethodInsn(opcode, owner, name, descriptor, ifInterface);
	}
	
	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean ifInterface)
	{
		if(listener.onVisitMethodInsn(mv, opcode, owner, name, descriptor, ifInterface))
			super.visitMethodInsn(opcode, owner, name, descriptor, ifInterface);
	}
	
	@Override
	public void visitMultiANewArrayInsn(String desc, int dims)
	{
		if(listener.onVisitMultiANewArrayInsn(mv, desc, dims))
			super.visitMultiANewArrayInsn(desc, dims);
	}
	
	@Override
	public void visitParameter(String name, int access)
	{
		if(listener.onVisitParameter(mv, name, access))
			super.visitParameter(name, access);
	}
	
	@Override
	public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible)
	{
		if(listener.onVisitParameterAnnotation(mv, parameter, desc, visible))
			return super.visitParameterAnnotation(parameter, desc, visible);
		throw new HookedInterruption();
	}
	
	@Override
	public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels)
	{
		if(listener.onVisitTableSwitchInsn(mv, min, max, dflt, labels))
			super.visitTableSwitchInsn(min, max, dflt, labels);
	}
	
	@Override
	public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		if(listener.onVisitTryCatchAnnotation(mv, typeRef, typePath, desc, visible))
			return super.visitTryCatchAnnotation(typeRef, typePath, desc, visible);
		throw new HookedInterruption();
	}
	
	@Override
	public void visitTryCatchBlock(Label start, Label end, Label handler, String type)
	{
		if(listener.onVisitTryCatchBlock(mv, start, end, handler, type))
			super.visitTryCatchBlock(start, end, handler, type);
	}
	
	@Override
	public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		if(listener.onVisitTypeAnnotation(mv, typeRef, typePath, desc, visible))
			return super.visitTypeAnnotation(typeRef, typePath, desc, visible);
		throw new HookedInterruption();
	}
	
	@Override
	public void visitTypeInsn(int opcode, String type)
	{
		if(listener.onVisitTypeInsn(mv, opcode, type))
			super.visitTypeInsn(opcode, type);
	}
	
	@Override
	public void visitVarInsn(int opcode, int var)
	{
		if(listener.onVisitVarInsn(mv, opcode, var))
			super.visitVarInsn(opcode, var);
	}
	
	@Override
	public void setListener(MethodVisitorListener listener)
	{
		this.listener = listener;
	}
	
	@Override
	public MethodVisitorListener getListener() 
	{
		return listener;
	}
	
	final MethodVisitor mv;
	
	MethodVisitorListener listener;
}
