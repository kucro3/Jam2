package org.kucro3.jam2.visitor;

import org.kucro3.jam2.util.Version;
import org.kucro3.util.Reference;
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
		super(Version.getASMVersion(), mv);
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
		Reference<AnnotationVisitor> ref;
		if(!listener.preVisitAnnotation(mv, descriptor, visible))
			throw new HookedInterruption();
		ref = new Reference<AnnotationVisitor>(super.visitAnnotation(descriptor, visible));
		listener.onVisitAnnotation(mv, descriptor, visible, ref);
		return ref.get();
	}
	
	@Override
	public AnnotationVisitor visitAnnotationDefault()
	{
		Reference<AnnotationVisitor> ref;
		if(!listener.preVisitAnnotationDefault(mv))
			throw new HookedInterruption();
		ref = new Reference<AnnotationVisitor>(super.visitAnnotationDefault());
		listener.onVisitAnnotationDefault(mv, ref);
		return ref.get();
	}
	
	@Override
	public void visitAttribute(Attribute attribute)
	{
		if(listener.preVisitAttribute(mv, attribute))
			return;
		super.visitAttribute(attribute);
		listener.onVisitAttribute(mv, attribute);
	}
	
	@Override
	public void visitCode()
	{
		if(!listener.preVisitCode(mv))
			return;
		super.visitCode();
		listener.onVisitCode(mv);
	}
	
	@Override
	public void visitEnd()
	{
		if(!listener.preVisitEnd(mv))
			return;
		super.visitEnd();
		listener.onVisitEnd(mv);
	}
	
	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String descriptor)
	{
		if(!listener.preVisitFieldInsn(mv, opcode, owner, name, descriptor))
			return;
		super.visitFieldInsn(opcode, owner, name, descriptor);
		listener.onVisitFieldInsn(mv, opcode, owner, name, descriptor);
	}
	
	@Override
	public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack)
	{
		if(!listener.preVisitFrame(mv, type, nLocal, local, nStack, stack))
			return;
		super.visitFrame(type, nLocal, local, nStack, stack);
		listener.onVisitFrame(mv, type, nLocal, local, nStack, stack);
	}
	
	@Override
	public void visitIincInsn(int var, int increment)
	{
		if(!listener.preVisitIincInsn(mv, var, increment))
			return;
		super.visitIincInsn(var, increment);
		listener.onVisitIincInsn(mv, var, increment);
	}
	
	@Override
	public void visitInsn(int opcode)
	{
		if(!listener.preVisitInsn(mv, opcode))
			return;
		super.visitInsn(opcode);
		listener.onVisitInsn(mv, opcode);
	}
	
	@Override
	public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		Reference<AnnotationVisitor> ref;
		if(!listener.preVisitInsnAnnotation(mv, typeRef, typePath, desc, visible))
			throw new HookedInterruption();
		ref = new Reference<AnnotationVisitor>(super.visitInsnAnnotation(typeRef, typePath, desc, visible));
		listener.onVisitInsnAnnotation(mv, typeRef, typePath, desc, visible);
		return ref.get();
	}
	
	@Override
	public void visitIntInsn(int opcode, int operand)
	{
		if(!listener.preVisitIntInsn(mv, opcode, operand))
			return;
		super.visitIntInsn(opcode, operand);
		listener.onVisitIntInsn(mv, opcode, operand);
	}
	
	@Override
	public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs)
	{
		if(!listener.preVisitInvokeDynamicInsn(mv, name, desc, bsm, bsmArgs))
			return;
		super.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
		listener.onVisitInvokeDynamicInsn(mv, name, desc, bsm, bsmArgs);
	}
	
	@Override
	public void visitJumpInsn(int opcode, Label label)
	{
		if(!listener.preVisitJumpInsn(mv, opcode, label))
			return;
		super.visitJumpInsn(opcode, label);
		listener.onVisitJumpInsn(mv, opcode, label);
	}
	
	@Override
	public void visitLabel(Label label)
	{
		if(!listener.preVisitLabel(mv, label))
			return;
		super.visitLabel(label);
		listener.onVisitLabel(mv, label);
	}
	
	@Override
	public void visitLdcInsn(Object cst)
	{
		if(!listener.preVisitLdcInsn(mv, cst))
			return;
		super.visitLdcInsn(cst);
		listener.onVisitLdcInsn(mv, cst);
	}
	
	@Override
	public void visitLineNumber(int line, Label start)
	{
		if(!listener.preVisitLineNumber(mv, line, start))
			return;
		super.visitLineNumber(line, start);
		listener.onVisitLineNumber(mv, line, start);
	}
	
	@Override
	public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index)
	{
		if(!listener.preVisitLocalVariable(mv, name, desc, signature, start, end, index))
			return;
		super.visitLocalVariable(name, desc, signature, start, end, index);
		listener.onVisitLocalVariable(mv, name, desc, signature, start, end, index);
	}
	
	@Override
	public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end,
			int[] index, String desc, boolean visible)
	{
		Reference<AnnotationVisitor> ref;
		if(!listener.preVisitLocalVariableAnnotation(mv, typeRef, typePath, start, end, index, desc, visible))
			throw new HookedInterruption();
		ref = new Reference<AnnotationVisitor>(super.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, desc, visible));
		
		return ref.get();
	}
	
	@Override
	public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels)
	{
		if(!listener.preVisitLookupSwitchInsn(mv, dflt, keys, labels))
			return;
		super.visitLookupSwitchInsn(dflt, keys, labels);
		listener.onVisitLookupSwitchInsn(mv, dflt, keys, labels);
	}
	
	@Override
	public void visitMaxs(int maxStack, int maxLocals)
	{
		if(!listener.preVisitMaxs(mv, maxStack, maxLocals))
			return;
		super.visitMaxs(maxStack, maxLocals);
		listener.onVisitMaxs(mv, maxStack, maxLocals);
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
		if(!listener.preVisitMethodInsn(mv, opcode, owner, name, descriptor, ifInterface))
			return;
		super.visitMethodInsn(opcode, owner, name, descriptor, ifInterface);
		listener.onVisitMethodInsn(mv, opcode, owner, name, descriptor, ifInterface);
	}
	
	@Override
	public void visitMultiANewArrayInsn(String desc, int dims)
	{
		if(!listener.preVisitMultiANewArrayInsn(mv, desc, dims))
			return;
		super.visitMultiANewArrayInsn(desc, dims);
		listener.onVisitMultiANewArrayInsn(mv, desc, dims);
	}
	
	@Override
	public void visitParameter(String name, int access)
	{
		if(!listener.preVisitParameter(mv, name, access))
			return;
		super.visitParameter(name, access);
		listener.onVisitParameter(mv, name, access);
	}
	
	@Override
	public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible)
	{
		Reference<AnnotationVisitor> ref;
		if(!listener.preVisitParameterAnnotation(mv, parameter, desc, visible))
			throw new HookedInterruption();
		ref = new Reference<AnnotationVisitor>(super.visitParameterAnnotation(parameter, desc, visible));
		listener.onVisitParameterAnnotation(mv, parameter, desc, visible, ref);
		return ref.get();
	}
	
	@Override
	public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels)
	{
		if(!listener.preVisitTableSwitchInsn(mv, min, max, dflt, labels))
			return;
		super.visitTableSwitchInsn(min, max, dflt, labels);
		listener.onVisitTableSwitchInsn(mv, min, max, dflt, labels);
	}
	
	@Override
	public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		Reference<AnnotationVisitor> ref;
		if(!listener.preVisitTryCatchAnnotation(mv, typeRef, typePath, desc, visible))
			throw new HookedInterruption();
		ref = new Reference<AnnotationVisitor>(super.visitTryCatchAnnotation(typeRef, typePath, desc, visible));
		listener.onVisitTryCatchAnnotation(mv, typeRef, typePath, desc, visible, ref);
		return ref.get();
	}
	
	@Override
	public void visitTryCatchBlock(Label start, Label end, Label handler, String type)
	{
		if(!listener.preVisitTryCatchBlock(mv, start, end, handler, type))
			return;
		super.visitTryCatchBlock(start, end, handler, type);
		listener.onVisitTryCatchBlock(mv, start, end, handler, type);
	}
	
	@Override
	public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		Reference<AnnotationVisitor> ref;
		if(!listener.preVisitTypeAnnotation(mv, typeRef, typePath, desc, visible))
			throw new HookedInterruption();
		ref = new Reference<AnnotationVisitor>(super.visitTypeAnnotation(typeRef, typePath, desc, visible));
		listener.onVisitTypeAnnotation(mv, typeRef, typePath, desc, visible, ref);
		return ref.get();
	}
	
	@Override
	public void visitTypeInsn(int opcode, String type)
	{
		if(!listener.preVisitTypeInsn(mv, opcode, type))
			return;
		super.visitTypeInsn(opcode, type);
		listener.onVisitTypeInsn(mv, opcode, type);
	}
	
	@Override
	public void visitVarInsn(int opcode, int var)
	{
		if(!listener.preVisitVarInsn(mv, opcode, var))
			return;
		super.visitVarInsn(opcode, var);
		listener.onVisitVarInsn(mv, opcode, var);
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
