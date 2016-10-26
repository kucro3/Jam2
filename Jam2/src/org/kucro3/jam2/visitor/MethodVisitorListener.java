package org.kucro3.jam2.visitor;

import org.kucro3.util.Reference;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.TypePath;

public interface MethodVisitorListener {
	public default boolean preVisitAnnotation(MethodVisitor mv, String desc, boolean visible)
	{return true;}
	
	public default boolean preVisitAnnotationDefault(MethodVisitor mv)
	{return true;}
	
	public default boolean preVisitAttribute(MethodVisitor mv, Attribute attr)
	{return true;}
	
	public default boolean preVisitCode(MethodVisitor mv)
	{return true;}
	
	public default boolean preVisitEnd(MethodVisitor mv)
	{return true;}
	
	public default boolean preVisitFieldInsn(MethodVisitor mv, int opcode, String owner, String name, String desc)
	{return true;}
	
	public default boolean preVisitFrame(MethodVisitor mv, int type, int nLocal, Object[] local, int nStack, Object[] stack)
	{return true;}
	
	public default boolean preVisitIincInsn(MethodVisitor mv, int var, int increment)
	{return true;}
	
	public default boolean preVisitInsn(MethodVisitor mv, int opcode)
	{return true;}
	
	public default boolean preVisitInsnAnnotation(MethodVisitor mv, int typeRef, TypePath typePath, String desc, boolean visible)
	{return true;}
	
	public default boolean preVisitIntInsn(MethodVisitor mv, int opcode, int operand)
	{return true;}
	
	public default boolean preVisitInvokeDynamicInsn(MethodVisitor mv, String name, String desc, Handle bsm, Object... bsmArgs)
	{return true;}
	
	public default boolean preVisitJumpInsn(MethodVisitor mv, int opcode, Label label)
	{return true;}
	
	public default boolean preVisitLabel(MethodVisitor mv, Label label)
	{return true;}
	
	public default boolean preVisitLdcInsn(MethodVisitor mv, Object cst)
	{return true;}
	
	public default boolean preVisitLineNumber(MethodVisitor mv, int line, Label start)
	{return true;}
	
	public default boolean preVisitLocalVariable(MethodVisitor mv, String name, String desc, String signature, Label start, Label end, int index)
	{return true;}
	
	public default boolean
		preVisitLocalVariableAnnotation(MethodVisitor mv, int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String desc, boolean visible)
	{return true;}
	
	public default boolean preVisitLookupSwitchInsn(MethodVisitor mv, Label dflt, int[] keys, Label[] labels)
	{return true;}
	
	public default boolean preVisitMaxs(MethodVisitor mv, int maxStack, int maxLocals)
	{return true;}
	
	public default boolean preVisitMethodInsn(MethodVisitor mv, int opcode, String owner, String name, String desc, boolean ifInterface)
	{return true;}
	
	public default boolean preVisitMultiANewArrayInsn(MethodVisitor mv, String desc, int dims)
	{return true;}
	
	public default boolean preVisitParameter(MethodVisitor mv, String name, int access)
	{return true;}
	
	public default boolean preVisitParameterAnnotation(MethodVisitor mv, int parameter, String desc, boolean visible)
	{return true;}

	public default boolean preVisitTableSwitchInsn(MethodVisitor mv, int min, int max, Label dflt, Label... labels)
	{return true;}
	
	public default boolean preVisitTryCatchAnnotation(MethodVisitor mv, int typeRef, TypePath typePath, String desc, boolean visible)
	{return true;}
	
	public default boolean preVisitTryCatchBlock(MethodVisitor mv, Label start, Label end, Label handler, String type)
	{return true;}
	
	public default boolean preVisitTypeAnnotation(MethodVisitor mv, int typeRef, TypePath typePath, String desc, boolean visible)
	{return true;}
	
	public default boolean preVisitTypeInsn(MethodVisitor mv, int opcode, String type)
	{return true;}
	
	public default boolean preVisitVarInsn(MethodVisitor mv, int opcode, int var)
	{return true;}
	
	public default void onVisitAnnotation(MethodVisitor mv, String desc, boolean visible, Reference<AnnotationVisitor> ref)
	{}
	
	public default void onVisitAnnotationDefault(MethodVisitor mv, Reference<AnnotationVisitor> ref)
	{}
	
	public default void onVisitAttribute(MethodVisitor mv, Attribute attr)
	{}
	
	public default void onVisitCode(MethodVisitor mv)
	{}
	
	public default void onVisitEnd(MethodVisitor mv)
	{}
	
	public default void onVisitFieldInsn(MethodVisitor mv, int opcode, String owner, String name, String desc)
	{}
	
	public default void onVisitFrame(MethodVisitor mv, int type, int nLocal, Object[] local, int nStack, Object[] stack)
	{}
	
	public default void onVisitIincInsn(MethodVisitor mv, int var, int increment)
	{}
	
	public default void onVisitInsn(MethodVisitor mv, int opcode)
	{}
	
	public default void onVisitInsnAnnotation(MethodVisitor mv, int typeRef, TypePath typePath, String desc, boolean visible)
	{}
	
	public default void onVisitIntInsn(MethodVisitor mv, int opcode, int operand)
	{}
	
	public default void onVisitInvokeDynamicInsn(MethodVisitor mv, String name, String desc, Handle bsm, Object... bsmArgs)
	{}
	
	public default void onVisitJumpInsn(MethodVisitor mv, int opcode, Label label)
	{}
	
	public default void onVisitLabel(MethodVisitor mv, Label label)
	{}
	
	public default void onVisitLdcInsn(MethodVisitor mv, Object cst)
	{}
	
	public default void onVisitLineNumber(MethodVisitor mv, int line, Label start)
	{}
	
	public default void onVisitLocalVariable(MethodVisitor mv, String name, String desc, String signature, Label start, Label end, int index)
	{}
	
	public default void
		onVisitLocalVariableAnnotation(MethodVisitor mv, int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String desc, boolean visible)
	{}
	
	public default void onVisitLookupSwitchInsn(MethodVisitor mv, Label dflt, int[] keys, Label[] labels)
	{}
	
	public default void onVisitMaxs(MethodVisitor mv, int maxStack, int maxLocals)
	{}
	
	public default void onVisitMethodInsn(MethodVisitor mv, int opcode, String owner, String name, String desc, boolean ifInterface)
	{}
	
	public default void onVisitMultiANewArrayInsn(MethodVisitor mv, String desc, int dims)
	{}
	
	public default void onVisitParameter(MethodVisitor mv, String name, int access)
	{}
	
	public default void onVisitParameterAnnotation(MethodVisitor mv, int parameter, String desc, boolean visible,
			Reference<AnnotationVisitor> ref)
	{}

	public default void onVisitTableSwitchInsn(MethodVisitor mv, int min, int max, Label dflt, Label... labels)
	{}
	
	public default void onVisitTryCatchAnnotation(MethodVisitor mv, int typeRef, TypePath typePath, String desc, boolean visible,
			Reference<AnnotationVisitor> ref)
	{}
	
	public default void onVisitTryCatchBlock(MethodVisitor mv, Label start, Label end, Label handler, String type)
	{}
	
	public default void onVisitTypeAnnotation(MethodVisitor mv, int typeRef, TypePath typePath, String desc, boolean visible
			, Reference<AnnotationVisitor> ref)
	{}
	
	public default void onVisitTypeInsn(MethodVisitor mv, int opcode, String type)
	{}
	
	public default void onVisitVarInsn(MethodVisitor mv, int opcode, int var)
	{}
}
