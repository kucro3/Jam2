package org.kucro3.jam2.asm;

import java.util.HashMap;
import java.util.Map;

import org.kucro3.jam2.simulator.MaxsComputer;
import org.kucro3.jam2.util.Version;
import org.kucro3.jam2.util.builder.AnnotationBuilder.LocalVariableAnnotationBuilder;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.TypePath;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ASMCodeBuilderRoot<T extends ASMCodeBuilderRoot> extends MethodVisitor implements Opcodes {
	protected ASMCodeBuilderRoot(MethodVisitor mv)
	{
		super(Version.getASMVersion(), new MaxsComputer(mv));
	}
	
	public T nop()
	{
		super.visitInsn(NOP);
		return (T) this;
	}
	
	public T aconst_null()
	{
		super.visitInsn(ACONST_NULL);
		return (T) this;
	}
	
	public T iconst_m1()
	{
		super.visitInsn(ICONST_M1);
		return (T) this;
	}
	
	public T iconst_0()
	{
		super.visitInsn(ICONST_0);
		return (T) this;
	}
	
	public T iconst_1()
	{
		super.visitInsn(ICONST_1);
		return (T) this;
	}
	
	public T iconst_2()
	{
		super.visitInsn(ICONST_2);
		return (T) this;
	}
	
	public T iconst_3()
	{
		super.visitInsn(ICONST_3);
		return (T) this;
	}
	
	public T iconst_4()
	{
		super.visitInsn(ICONST_4);
		return (T) this;
	}
	
	public T iconst_5()
	{
		super.visitInsn(ICONST_5);
		return (T) this;
	}
	
	public T lconst_0()
	{
		super.visitInsn(LCONST_0);
		return (T) this;
	}
	
	public T lconst_1()
	{
		super.visitInsn(LCONST_1);
		return (T) this;
	}
	
	public T fconst_0()
	{
		super.visitInsn(FCONST_0);
		return (T) this;
	}
	
	public T fconst_1()
	{
		super.visitInsn(FCONST_1);
		return (T) this;
	}
	
	public T fconst_2()
	{
		super.visitInsn(FCONST_2);
		return (T) this;
	}
	
	public T dconst_0()
	{
		super.visitInsn(DCONST_0);
		return (T) this;
	}
	
	public T dconst_1()
	{
		super.visitInsn(DCONST_1);
		return (T) this;
	}
	
	public T bipush(int val)
	{
		super.visitIntInsn(BIPUSH, val);
		return (T) this;
	}
	
	public T sipush(int val)
	{
		super.visitIntInsn(SIPUSH, val);
		return (T) this;
	}
	
	public T ldc(Object cst)
	{
		super.visitLdcInsn(cst);
		return (T) this;
	}
	
	public T iload(int var)
	{
		super.visitVarInsn(ILOAD, var);
		return (T) this;
	}
	
	public T lload(int var)
	{
		super.visitVarInsn(LLOAD, var);
		return (T) this;
	}
	
	public T fload(int var)
	{
		super.visitVarInsn(FLOAD, var);
		return (T) this;
	}
	
	public T dload(int var)
	{
		super.visitVarInsn(DLOAD, var);
		return (T) this;
	}
	
	public T aload(int var)
	{
		super.visitVarInsn(ALOAD, var);
		return (T) this;
	}
	
	public T iload_0()
	{
		return iload(0);
	}
	
	public T iload_1()
	{
		return iload(1);
	}
	
	public T iload_2()
	{
		return iload(2);
	}
	
	public T aload_0()
	{
		return aload(0);
	}
	
	public T aload_1()
	{
		return aload(1);
	}
	
	public T aload_2()
	{
		return aload(2);
	}
	
	public T aload_3()
	{
		return aload(3);
	}
	
	public T iload_3()
	{
		return iload(3);
	}
	
	public T lload_0()
	{
		return lload(0);
	}
	
	public T lload_1()
	{
		return lload(1);
	}
	
	public T lload_2()
	{
		return lload(2);
	}
	
	public T lload_3()
	{
		return lload(3);
	}
	
	public T fload_0()
	{
		return fload(0);
	}
	
	public T fload_1()
	{
		return fload(1);
	}
	
	public T fload_2()
	{
		return fload(2);
	}
	
	public T fload_3()
	{
		return fload(3);
	}
	
	public T dload_0()
	{
		return dload(0);
	}
	
	public T dload_1()
	{
		return dload(1);
	}
	
	public T dload_2()
	{
		return dload(2);
	}
	
	public T dload_3()
	{
		return dload(3);
	}
	
	public T iaload()
	{
		super.visitInsn(IALOAD);
		return (T) this;
	}
	
	public T laload()
	{
		super.visitInsn(LALOAD);
		return (T) this;
	}
	
	public T faload()
	{
		super.visitInsn(FALOAD);
		return (T) this;
	}
	
	public T daload()
	{
		super.visitInsn(DALOAD);
		return (T) this;
	}
	
	public T aaload()
	{
		super.visitInsn(AALOAD);
		return (T) this;
	}
	
	public T baload()
	{
		super.visitInsn(BALOAD);
		return (T) this;
	}
	
	public T caload()
	{
		super.visitInsn(CALOAD);
		return (T) this;
	}
	
	public T saload()
	{
		super.visitInsn(SALOAD);
		return (T) this;
	}
	
	public T istore(int var)
	{
		super.visitVarInsn(ISTORE, var);
		return (T) this;
	}
	
	public T lstore(int var)
	{
		super.visitVarInsn(LSTORE, var);
		return (T) this;
	}
	
	public T fstore(int var)
	{
		super.visitVarInsn(FSTORE, var);
		return (T) this;
	}
	
	public T dstore(int var)
	{
		super.visitVarInsn(DSTORE, var);
		return (T) this;
	}
	
	public T astore(int var)
	{
		super.visitVarInsn(ASTORE, var);
		return (T) this;
	}
	
	public T istore_0()
	{
		return istore(0);
	}
	
	public T istore_1()
	{
		return istore(1);
	}
	
	public T istore_2()
	{
		return istore(3);
	}
	
	public T istore_3()
	{
		return istore(3);
	}
	
	public T fstore_0()
	{
		return fstore(0);
	}
	
	public T fstore_1()
	{
		return fstore(1);
	}
	
	public T fstore_2()
	{
		return fstore(2);
	}
	
	public T fstore_3()
	{
		return fstore(3);
	}
	
	public T dstore_0()
	{
		return dstore(0);
	}
	
	public T dstore_1()
	{
		return dstore(1);
	}
	
	public T dstore_2()
	{
		return dstore(2);
	}
	
	public T dstore_3()
	{
		return dstore(3);
	}
	
	public T astore_0()
	{
		return astore(0);
	}
	
	public T astore_1()
	{
		return astore(1);
	}
	
	public T astore_2()
	{
		return astore(2);
	}
	
	public T astore_3()
	{
		return astore(3);
	}
	
	public T iastore()
	{
		super.visitInsn(IASTORE);
		return (T) this;
	}
	
	public T lastore()
	{
		super.visitInsn(LASTORE);
		return (T) this;
	}
	
	public T fastore()
	{
		super.visitInsn(FASTORE);
		return (T) this;
	}
	
	public T dastore()
	{
		super.visitInsn(DASTORE);
		return (T) this;
	}
	
	public T aastore()
	{
		super.visitInsn(AASTORE);
		return (T) this;
	}
	
	public T bastore()
	{
		super.visitInsn(BASTORE);
		return (T) this;
	}
	
	public T castore()
	{
		super.visitInsn(CASTORE);
		return (T) this;
	}
	
	public T sastore()
	{
		super.visitInsn(SASTORE);
		return (T) this;
	}
	
	public T pop()
	{
		super.visitInsn(POP);
		return (T) this;
	}
	
	public T pop2()
	{
		super.visitInsn(POP2);
		return (T) this;
	}
	
	public T dup()
	{
		super.visitInsn(DUP);
		return (T) this;
	}
	
	public T dup_x1()
	{
		super.visitInsn(DUP_X1);
		return (T) this;
	}
	
	public T dup_x2()
	{
		super.visitInsn(DUP_X2);
		return (T) this;
	}
	
	public T dup2()
	{
		super.visitInsn(DUP2);
		return (T) this;
	}
	
	public T dup2_x1()
	{
		super.visitInsn(DUP2_X1);
		return (T) this;
	}
	
	public T dup2_x2()
	{
		super.visitInsn(DUP2_X2);
		return (T) this;
	}
	
	public T swap()
	{
		super.visitInsn(SWAP);
		return (T) this;
	}
	
	public T iadd()
	{
		super.visitInsn(IADD);
		return (T) this;
	}
	
	public T ladd()
	{
		super.visitInsn(LADD);
		return (T) this;
	}
	
	public T fadd()
	{
		super.visitInsn(FADD);
		return (T) this;
	}
	
	public T dadd()
	{
		super.visitInsn(DADD);
		return (T) this;
	}
	
	public T isub()
	{
		super.visitInsn(ISUB);
		return (T) this;
	}
	
	public T lsub()
	{
		super.visitInsn(LSUB);
		return (T) this;
	}
	
	public T fsub()
	{
		super.visitInsn(FSUB);
		return (T) this;
	}
	
	public T dsub()
	{
		super.visitInsn(DSUB);
		return (T) this;
	}
	
	public T imul()
	{
		super.visitInsn(IMUL);
		return (T) this;
	}
	
	public T lmul()
	{
		super.visitInsn(LMUL);
		return (T) this;
	}
	
	public T fmul()
	{
		super.visitInsn(FMUL);
		return (T) this;
	}
	
	public T dmul()
	{
		super.visitInsn(DMUL);
		return (T) this;
	}
	
	public T idiv()
	{
		super.visitInsn(IDIV);
		return (T) this;
	}
	
	public T ldiv()
	{
		super.visitInsn(LDIV);
		return (T) this;
	}
	
	public T fdiv()
	{
		super.visitInsn(FDIV);
		return (T) this;
	}
	
	public T ddiv()
	{
		super.visitInsn(DDIV);
		return (T) this;
	}
	
	public T irem()
	{
		super.visitInsn(IREM);
		return (T) this;
	}
	
	public T lrem()
	{
		super.visitInsn(LREM);
		return (T) this;
	}
	
	public T frem()
	{
		super.visitInsn(FREM);
		return (T) this;
	}
	
	public T drem()
	{
		super.visitInsn(DREM);
		return (T) this;
	}
	
	public T ineg()
	{
		super.visitInsn(INEG);
		return (T) this;
	}
	
	public T lneg()
	{
		super.visitInsn(LNEG);
		return (T) this;
	}
	
	public T fneg()
	{
		super.visitInsn(FNEG);
		return (T) this;
	}
	
	public T dneg()
	{
		super.visitInsn(DNEG);
		return (T) this;
	}
	
	public T ishl()
	{
		super.visitInsn(ISHL);
		return (T) this;
	}
	
	public T lshl()
	{
		super.visitInsn(LSHL);
		return (T) this;
	}
	
	public T ishr()
	{
		super.visitInsn(ISHR);
		return (T) this;
	}
	
	public T lshr()
	{
		super.visitInsn(LSHR);
		return (T) this;
	}
	
	public T iushr()
	{
		super.visitInsn(IUSHR);
		return (T) this;
	}
	
	public T lushr()
	{
		super.visitInsn(LUSHR);
		return (T) this;
	}
	
	public T iand()
	{
		super.visitInsn(IAND);
		return (T) this;
	}
	
	public T land()
	{
		super.visitInsn(LAND);
		return (T) this;
	}
	
	public T ior()
	{
		super.visitInsn(IOR);
		return (T) this;
	}
	
	public T lor()
	{
		super.visitInsn(LOR);
		return (T) this;
	}
	
	public T ixor()
	{
		super.visitInsn(IXOR);
		return (T) this;
	}
	
	public T lxor()
	{
		super.visitInsn(LXOR);
		return (T) this;
	}
	
	public T iinc()
	{
		super.visitInsn(IINC);
		return (T) this;
	}
	
	public T i2l()
	{
		super.visitInsn(I2L);
		return (T) this;
	}
	
	public T i2f()
	{
		super.visitInsn(I2F);
		return (T) this;
	}
	
	public T i2d()
	{
		super.visitInsn(I2D);
		return (T) this;
	}
	
	public T l2i()
	{
		super.visitInsn(L2I);
		return (T) this;
	}
	
	public T l2f()
	{
		super.visitInsn(L2F);
		return (T) this;
	}
	
	public T l2d()
	{
		super.visitInsn(L2D);
		return (T) this;
	}
	
	public T f2i()
	{
		super.visitInsn(F2I);
		return (T) this;
	}
	
	public T f2l()
	{
		super.visitInsn(F2L);
		return (T) this;
	}
	
	public T f2d()
	{
		super.visitInsn(F2D);
		return (T) this;
	}
	
	public T d2i()
	{
		super.visitInsn(D2I);
		return (T) this;
	}
	
	public T d2l()
	{
		super.visitInsn(D2L);
		return (T) this;
	}
	
	public T d2f()
	{
		super.visitInsn(D2F);
		return (T) this;
	}
	
	public T i2b()
	{
		super.visitInsn(I2B);
		return (T) this;
	}
	
	public T i2c()
	{
		super.visitInsn(I2C);
		return (T) this;
	}
	
	public T i2s()
	{
		super.visitInsn(I2S);
		return (T) this;
	}
	
	public T lcmp()
	{
		super.visitInsn(LCMP);
		return (T) this;
	}
	
	public T fcmpl()
	{
		super.visitInsn(FCMPL);
		return (T) this;
	}
	
	public T fcmpg()
	{
		super.visitInsn(FCMPG);
		return (T) this;
	}
	
	public T dcmpl()
	{
		super.visitInsn(DCMPL);
		return (T) this;
	}
	
	public T dcmpg()
	{
		super.visitInsn(DCMPG);
		return (T) this;
	}
	
	public T ifeq()
	{
		super.visitInsn(IFEQ);
		return (T) this;
	}
	
	public T ifne()
	{
		super.visitInsn(IFNE);
		return (T) this;
	}
	
	public T iflt()
	{
		super.visitInsn(IFLT);
		return (T) this;
	}
	
	public T ifge()
	{
		super.visitInsn(IFGE);
		return (T) this;
	}
	
	public T ifgt()
	{
		super.visitInsn(IFGT);
		return (T) this;
	}
	
	public T ifle()
	{
		super.visitInsn(IFLE);
		return (T) this;
	}
	
	public T if_icmpeq(String l)
	{
		return if_icmpeq(label(l));
	}
	
	public T if_icmpeq(Label l)
	{
		super.visitJumpInsn(IF_ICMPEQ, l);
		return (T) this;
	}
	
	public T if_icmpne(String l)
	{
		return if_icmpne(label(l));
	}
	
	public T if_icmpne(Label l)
	{
		super.visitJumpInsn(IF_ICMPNE, l);
		return (T) this;
	}
	
	public T if_icmplt(String l)
	{
		return if_icmplt(label(l));
	}
	
	public T if_icmplt(Label l)
	{
		super.visitJumpInsn(IF_ICMPLT, l);
		return (T) this;
	}
	
	public T if_icmpge(String l)
	{
		return if_icmpge(label(l));
	}
	
	public T if_icmpge(Label l)
	{
		super.visitJumpInsn(IF_ICMPGE, l);
		return (T) this;
	}
	
	public T if_icmpgt(String l)
	{
		return if_icmpge(label(l));
	}
	
	public T if_icmpgt(Label l)
	{
		super.visitJumpInsn(IF_ICMPGT, l);
		return (T) this;
	}
	
	public T if_icmple(String l)
	{
		return if_icmple(label(l));
	}
	
	public T if_icmple(Label l)
	{
		super.visitJumpInsn(IF_ICMPLE, l);
		return (T) this;
	}
	
	public T if_acmpeq(String l)
	{
		return if_acmpeq(label(l));
	}
	
	public T if_acmpeq(Label l)
	{
		super.visitJumpInsn(IF_ACMPEQ, l);
		return (T) this;
	}
	
	public T if_acmpne(String l)
	{
		return if_acmpne(label(l));
	}
	
	public T if_acmpne(Label l)
	{
		super.visitJumpInsn(IF_ACMPNE, l);
		return (T) this;
	}
	
	public T goTo(String l)
	{
		return goTo(label(l));
	}
	
	public T goTo(Label l)
	{
		super.visitJumpInsn(GOTO, l);
		return (T) this;
	}
	
	public T jsr(String l)
	{
		return jsr(label(l));
	}
	
	public T jsr(Label l)
	{
		super.visitJumpInsn(GOTO, l);
		return (T) this;
	}
	
	public T ret()
	{
		super.visitInsn(RET);
		return (T) this;
	}
	
	public T tableswitch(int min, int max, String dflt, String[] labels)
	{
		return tableswitch(min, max, label(dflt), labels(labels));
	}
	
	public T tableswitch(int min, int max, Label dflt, Label[] labels)
	{
		super.visitTableSwitchInsn(min, max, dflt, labels);
		return (T) this;
	}
	
	public T lookupswitch(String dflt, int[] keys, String[] labels)
	{
		return lookupswitch(label(dflt), keys, labels(labels));
	}
	
	public T lookupswitch(Label dflt, int[] keys, Label[] labels)
	{
		super.visitLookupSwitchInsn(dflt, keys, labels);
		return (T) this;
	}
	
	public T ireturn()
	{
		super.visitInsn(IRETURN);
		return (T) this;
	}
	
	public T lreturn()
	{
		super.visitInsn(LRETURN);
		return (T) this;
	}
	
	public T freturn()
	{
		super.visitInsn(FRETURN);
		return (T) this;
	}
	
	public T dreturn()
	{
		super.visitInsn(DRETURN);
		return (T) this;
	}
	
	public T areturn()
	{
		super.visitInsn(ARETURN);
		return (T) this;
	}
	
	public T reTurn()
	{
		super.visitInsn(RETURN);
		return (T) this;
	}
	
	public T getstatic(String owner, String name, String desc)
	{
		super.visitFieldInsn(GETSTATIC, owner, name, desc);
		return (T) this;
	}
	
	public T putstatic(String owner, String name, String desc)
	{
		super.visitFieldInsn(PUTSTATIC, owner, name, desc);
		return (T) this;
	}
	
	public T getfield(String owner, String name, String desc)
	{
		super.visitFieldInsn(GETFIELD, owner, name, desc);
		return (T) this;
	}
	
	public T putfield(String owner, String name, String desc)
	{
		super.visitFieldInsn(PUTFIELD, owner, name, desc);
		return (T) this;
	}
	
	public T invokevirtual(String owner, String name, String desc)
	{
		super.visitMethodInsn(INVOKEVIRTUAL, owner, name, desc, false);
		return (T) this;
	}
	
	public T invokespecial(String owner, String name, String desc)
	{
		super.visitMethodInsn(INVOKESPECIAL, owner, name, desc, false);
		return (T) this;
	}
	
	public T invokestatic(String owner, String name, String desc)
	{
		super.visitMethodInsn(INVOKESTATIC, owner, name, desc, false);
		return (T) this;
	}
	
	public T invokeinterface(String owner, String name, String desc)
	{
		super.visitMethodInsn(INVOKEINTERFACE, owner, name, desc, true);
		return (T) this;
	}
	
	public T invokedynamic(String name, String desc, Handle bsm, Object... bsmArgs)
	{
		super.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
		return (T) this;
	}
	
	public T neW(String type)
	{
		super.visitTypeInsn(NEW, type);
		return (T) this;
	}
	
	public T newarray(String type)
	{
		super.visitTypeInsn(NEWARRAY, type);
		return (T) this;
	}
	
	public T anewarray(String type)
	{
		super.visitTypeInsn(ANEWARRAY, type);
		return (T) this;
	}
	
	public T arraylength()
	{
		super.visitInsn(ARRAYLENGTH);
		return (T) this;
	}
	
	public T athrow()
	{
		super.visitInsn(ATHROW);
		return (T) this;
	}
	
	public T checkcast(String type)
	{
		super.visitTypeInsn(CHECKCAST, type);
		return (T) this;
	}
	
	public T instanceOf(String type)
	{
		super.visitTypeInsn(INSTANCEOF, type);
		return (T) this;
	}
	
	public T monitorenter()
	{
		super.visitInsn(MONITORENTER);
		return (T) this;
	}
	
	public T monitorexit()
	{
		super.visitInsn(MONITOREXIT);
		return (T) this;
	}
	
	public T multianewarray(String type, int dims)
	{
		super.visitMultiANewArrayInsn(type, dims);
		return (T) this;
	}
	
	public T ifnull(String l)
	{
		return ifnull(label(l));
	}
	
	public T ifnull(Label l)
	{
		super.visitJumpInsn(IFNULL, l);
		return (T) this;
	}
	
	public T ifnonnull(String l)
	{
		return ifnonnull(label(l));
	}
	
	public T ifnonnull(Label l)
	{
		super.visitJumpInsn(IFNONNULL, l);
		return (T) this;
	}
	
	public final MethodVisitor getMethodVisitor()
	{
		return mv;
	}
	
	public T frame(int type, int nLocal, Object[] local, int nStack, Object[] stack)
	{
		mv.visitFrame(type, nLocal, local, nStack, stack);
		return (T) this;
	}
	
	public T localVariable(String name, String desc, String signature, String lstart, String lend, int index)
	{
		return localVariable(name, desc, signature, label(lstart), label(lend), index);
	}
	
	public T localVariable(String name, String desc, String signature, Label start, Label end, int index)
	{
		mv.visitLocalVariable(name, desc, signature, start, end, index);
		return (T) this;
	}
	
	public LocalVariableAnnotationBuilder<T>
		localVariableAnnotation(int typeRef, TypePath typePath, String[] start, String[] end, int[] index, String desc, boolean visible)
	{
		return localVariableAnnotation(typeRef, typePath, labels(start), labels(end), index, desc, visible);
	}
	
	public LocalVariableAnnotationBuilder<T>
		localVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String desc, boolean visible)
	{
		return new LocalVariableAnnotationBuilder<T>
				(mv.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, desc, visible), (T) this);
	}
	
	public T lineNumber(int line, String start)
	{
		return lineNumber(line, label(start));
	}
	
	public T lineNumber(int line, Label start)
	{
		mv.visitLineNumber(line, start);
		return (T) this;
	}
	
	public final T maxs(int maxStacks, int maxLocals)
	{
		mv.visitMaxs(maxStacks, maxLocals);
		return (T) this;
	}
	
	public final T end(boolean computeMaxs)
	{
		if(computeMaxs)
			mv.visitMaxs(0, 0);

		return end();
	}
	
	public final T end()
	{
		mv.visitEnd();
		return (T) this;
	}
	
	protected final Label label(String name)
	{
		Label r;
		if((r = labels.get(name)) == null)
			labels.put(name, r = new Label());
		return r;
	}
	
	protected final Label[] labels(String[] names)
	{
		Label[] labels = new Label[names.length];
		for(int i = 0; i < labels.length; i++)
			labels[i] = label(names[i]);
		return labels;
	}
	
	protected final Map<String, Label> labels = new HashMap<>();
}
