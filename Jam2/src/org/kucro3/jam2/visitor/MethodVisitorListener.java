package org.kucro3.jam2.visitor;

import org.objectweb.asm.Attribute;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.TypePath;

public interface MethodVisitorListener {
	public default boolean onVisitAnnotation(MethodVisitor mv, String desc, boolean visible)
	{return true;}
	
	public default boolean onVisitAnnotationDefault(MethodVisitor mv)
	{return true;}
	
	public default boolean onVisitAttribute(MethodVisitor mv, Attribute attr)
	{return true;}
	
	public default boolean onVisitCode(MethodVisitor mv)
	{return true;}
	
	public default boolean onVisitEnd(MethodVisitor mv)
	{return true;}
	
	public default boolean onVisitFieldInsn(MethodVisitor mv, int opcode, String owner, String name, String desc)
	{return true;}
	
	public default boolean onVisitFrame(MethodVisitor mv, int type, int nLocal, Object[] local, int nStack, Object[] stack)
	{return true;}
	
	public default boolean onVisitIincInsn(MethodVisitor mv, int var, int increment)
	{return true;}
	
	public default boolean onVisitInsn(MethodVisitor mv, int opcode)
	{return true;}
	
	public default boolean onVisitInsnAnnotation(MethodVisitor mv, int typeRef, TypePath typePath, String desc, boolean visible)
	{return true;}
	
	public default boolean onVisitIntInsn(MethodVisitor mv, int opcode, int operand)
	{return true;}
	
	public default boolean onVisitInvokeDynamicInsn(MethodVisitor mv, String name, String desc, Handle bsm, Object... bsmArgs)
	{return true;}
	
	public default boolean onVisitJumpInsn(MethodVisitor mv, int opcode, Label label)
	{return true;}
	
	public default boolean onVisitLabel(MethodVisitor mv, Label label)
	{return true;}
	
	public default boolean onVisitLdcInsn(MethodVisitor mv, Object cst)
	{return true;}
	
	public default boolean onVisitLineNumber(MethodVisitor mv, int line, Label start)
	{return true;}
	
	public default boolean onVisitLocalVariable(MethodVisitor mv, String name, String desc, String signature, Label start, Label end, int index)
	{return true;}
	
	public default boolean
		onVisitLocalVariableAnnotation(MethodVisitor mv, int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String desc, boolean visible)
	{return true;}
	
	public default boolean onVisitLookupSwitchInsn(MethodVisitor mv, Label dflt, int[] keys, Label[] labels)
	{return true;}
	
	public default boolean onVisitMaxs(MethodVisitor mv, int maxStack, int maxLocals)
	{return true;}
	
	public default boolean onVisitMethodInsn(MethodVisitor mv, int opcode, String owner, String name, String desc, boolean ifInterface)
	{return true;}
	
	public default boolean onVisitMultiANewArrayInsn(MethodVisitor mv, String desc, int dims)
	{return true;}
	
	public default boolean onVisitParameter(MethodVisitor mv, String name, int access)
	{return true;}
	
	public default boolean onVisitParameterAnnotation(MethodVisitor mv, int parameter, String desc, boolean visible)
	{return true;}

	public default boolean onVisitTableSwitchInsn(MethodVisitor mv, int min, int max, Label dflt, Label... labels)
	{return true;}
	
	public default boolean onVisitTryCatchAnnotation(MethodVisitor mv, int typeRef, TypePath typePath, String desc, boolean visible)
	{return true;}
	
	public default boolean onVisitTryCatchBlock(MethodVisitor mv, Label start, Label end, Label handler, String type)
	{return true;}
	
	public default boolean onVisitTypeAnnotation(MethodVisitor mv, int typeRef, TypePath typePath, String desc, boolean visible)
	{return true;}
	
	public default boolean onVisitTypeInsn(MethodVisitor mv, int opcode, String type)
	{return true;}
	
	public default boolean onVisitVarInsn(MethodVisitor mv, int opcode, int var)
	{return true;}
}
