package org.kucro3.jam2.opcode;

import static org.kucro3.jam2.opcode.OpcodeForm.*;

import java.util.HashMap;
import java.util.Map;

public class Opcode {
	private Opcode(String name, int code, OpcodeForm form, int stack)
	{
		this(name, code, form, stack, 0, 0);
	}
	
	private Opcode(String name, int code, OpcodeForm form, int stack, int root, int base)
	{
		this.code = code;
		this.form = form;
		this.stack = stack;
		this.name = name;
		this.root = root;
		this.base = base;
		OPCODE_MAP.put(name, code);
	}
	
	public int getCode()
	{
		return code;
	}
	
	public OpcodeForm getForm()
	{
		return form;
	}
	
	@Deprecated
	public int getStack()
	{
		return stack;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getIndex(String name)
	{
		Integer objRet = OPCODE_MAP.get(name);
		if(objRet == null)
			return -1;
		return objRet;
	}
	
	final int getRoot()
	{
		return root;
	}
	
	final int getBase()
	{
		return base;
	}
	
	public static Opcode get(int insn)
	{
		return OPCODES[insn];
	}
	
	private final int base;
	
	private final int root;
	
	private final String name;
	
	private final int code;
	
	private final OpcodeForm form;
	
	private final int stack;
	
	static final Opcode[] OPCODES;
	
	static final Map<String, Integer> OPCODE_MAP;
	
	public static final int STACK_DYNC = 0xFFFFFFFF;
	
	private static void init()
	{
		OPCODES[0x00] = new Opcode("nop", 0x00, VOID, 0);
		OPCODES[0x01] = new Opcode("aconst_null", 0x01, VOID, 1);
		OPCODES[0x02] = new Opcode("iconst_m1", 0x02, VOID, 1);
		OPCODES[0x03] = new Opcode("iconst_0", 0x03, VOID, 1);
		OPCODES[0x04] = new Opcode("iconst_1", 0x04, VOID, 1);
		OPCODES[0x05] = new Opcode("iconst_2", 0x05, VOID, 1);
		OPCODES[0x06] = new Opcode("iconst_3", 0x06, VOID, 1);
		OPCODES[0x07] = new Opcode("iconst_4", 0x07, VOID, 1);
		OPCODES[0x08] = new Opcode("iconst_5", 0x08, VOID, 1);
		OPCODES[0x09] = new Opcode("lconst_0", 0x09, VOID, 1);
		OPCODES[0x0A] = new Opcode("lconst_1", 0x0A, VOID, 1);
		OPCODES[0x0B] = new Opcode("fconst_0", 0x0B, VOID, 1);
		OPCODES[0x0C] = new Opcode("fconst_1", 0x0C, VOID, 1);
		OPCODES[0x0D] = new Opcode("fconst_2", 0x0D, VOID, 1);
		OPCODES[0x0E] = new Opcode("dconst_0", 0x0E, VOID, 1);
		OPCODES[0x0F] = new Opcode("dconst_1", 0x0F, VOID, 1);
		OPCODES[0x10] = new Opcode("bipush", 0x10, INT, 1);
		OPCODES[0x11] = new Opcode("sipush", 0x11, INT, 1);
		OPCODES[0x12] = new Opcode("ldc", 0x12, LDC, 1);
		OPCODES[0x15] = new Opcode("iload", 0x15, VAR, 1);
		OPCODES[0x16] = new Opcode("lload", 0x16, VAR, 1);
		OPCODES[0x17] = new Opcode("fload", 0x17, VAR, 1);
		OPCODES[0x18] = new Opcode("dload", 0x18, VAR, 1);
		OPCODES[0x19] = new Opcode("aload", 0x19, VAR, 1);
		OPCODES[0x1A] = new Opcode("iload_0", 0x1A, VAR_CST, 1, 0x15, 0x1A);
		OPCODES[0x1B] = new Opcode("iload_1", 0x1B, VAR_CST, 1, 0x15, 0x1A);
		OPCODES[0x1C] = new Opcode("iload_2", 0x1C, VAR_CST, 1, 0x15, 0x1A);
		OPCODES[0x1D] = new Opcode("iload_3", 0x1D, VAR_CST, 1, 0x15, 0x1A);
		OPCODES[0x1E] = new Opcode("lload_0", 0x1E, VAR_CST, 1, 0x16, 0x1E);
		OPCODES[0x1F] = new Opcode("lload_1", 0x1F, VAR_CST, 1, 0x16, 0x1E);
		OPCODES[0x20] = new Opcode("lload_2", 0x20, VAR_CST, 1, 0x16, 0x1E);
		OPCODES[0x21] = new Opcode("lload_3", 0x21, VAR_CST, 1, 0x16, 0x1E);
		OPCODES[0x22] = new Opcode("fload_0", 0x22, VAR_CST, 1, 0x17, 0x22);
		OPCODES[0x23] = new Opcode("fload_1", 0x23, VAR_CST, 1, 0x17, 0x22);
		OPCODES[0x24] = new Opcode("fload_2", 0x24, VAR_CST, 1, 0x17, 0x22);
		OPCODES[0x25] = new Opcode("fload_3", 0x25, VAR_CST, 1, 0x17, 0x22);
		OPCODES[0x26] = new Opcode("dload_0", 0x26, VAR_CST, 1, 0x18, 0x26);
		OPCODES[0x27] = new Opcode("dload_1", 0x27, VAR_CST, 1, 0x18, 0x26);
		OPCODES[0x28] = new Opcode("dload_2", 0x28, VAR_CST, 1, 0x18, 0x26);
		OPCODES[0x29] = new Opcode("dload_3", 0x29, VAR_CST, 1, 0x18, 0x26);
		OPCODES[0x2A] = new Opcode("aload_0", 0x2A, VAR_CST, 1, 0x19, 0x2A);
		OPCODES[0x2B] = new Opcode("aload_1", 0x2B, VAR_CST, 1, 0x19, 0x2A);
		OPCODES[0x2C] = new Opcode("aload_2", 0x2C, VAR_CST, 1, 0x19, 0x2A);
		OPCODES[0x2D] = new Opcode("aload_3", 0x2D, VAR_CST, 1, 0x19, 0x2A);
		OPCODES[0x2E] = new Opcode("iaload", 0x2E, VOID, -1);
		OPCODES[0x2F] = new Opcode("laload", 0x2F, VOID, -1);
		OPCODES[0x30] = new Opcode("faload", 0x30, VOID, -1);
		OPCODES[0x31] = new Opcode("daload", 0x31, VOID, -1);
		OPCODES[0x32] = new Opcode("aaload", 0x32, VOID, -1);
		OPCODES[0x33] = new Opcode("baload", 0x33, VOID, -1);
		OPCODES[0x34] = new Opcode("caload", 0x34, VOID, -1);
		OPCODES[0x35] = new Opcode("saload", 0x35, VOID, -1);
		OPCODES[0x36] = new Opcode("istore", 0x36, VAR, -1);
		OPCODES[0x37] = new Opcode("lstore", 0x37, VAR, -1);
		OPCODES[0x38] = new Opcode("fstore", 0x38, VAR, -1);
		OPCODES[0x39] = new Opcode("dstore", 0x39, VAR, -1);
		OPCODES[0x3A] = new Opcode("astore", 0x3A, VAR, -1);
		OPCODES[0x3B] = new Opcode("istore_0", 0x3B, VOID, -1, 0x36, 0x3B);
		OPCODES[0x3C] = new Opcode("istore_1", 0x3C, VOID, -1, 0x36, 0x3B);
		OPCODES[0x3D] = new Opcode("istore_2", 0x3D, VOID, -1, 0x36, 0x3B);
		OPCODES[0x3E] = new Opcode("istore_3", 0x3E, VOID, -1, 0x36, 0x3B);
		OPCODES[0x3F] = new Opcode("lstore_0", 0x3F, VOID, -1, 0x37, 0x3F);
		OPCODES[0x40] = new Opcode("lstore_1", 0x40, VOID, -1, 0x37, 0x3F);
		OPCODES[0x41] = new Opcode("lstore_2", 0x41, VOID, -1, 0x37, 0x3F);
		OPCODES[0x42] = new Opcode("lstore_3", 0x42, VOID, -1, 0x37, 0x3F);
		OPCODES[0x43] = new Opcode("fstore_0", 0x43, VOID, -1, 0x38, 0x43);
		OPCODES[0x44] = new Opcode("fstore_1", 0x44, VOID, -1, 0x38, 0x43);
		OPCODES[0x45] = new Opcode("fstore_2", 0x45, VOID, -1, 0x38, 0x43);
		OPCODES[0x46] = new Opcode("fstore_3", 0x46, VOID, -1, 0x38, 0x43);
		OPCODES[0x47] = new Opcode("dstore_0", 0x47, VOID, -1, 0x39, 0x47);
		OPCODES[0x48] = new Opcode("dstore_1", 0x48, VOID, -1, 0x39, 0x47);
		OPCODES[0x49] = new Opcode("dstore_2", 0x49, VOID, -1, 0x39, 0x47);
		OPCODES[0x4A] = new Opcode("dstore_3", 0x4A, VOID, -1, 0x39, 0x47);
		OPCODES[0x4B] = new Opcode("astore_0", 0x4B, VOID, -1, 0x3A, 0x4B);
		OPCODES[0x4C] = new Opcode("astore_1", 0x4C, VOID, -1, 0x3A, 0x4B);
		OPCODES[0x4D] = new Opcode("astore_2", 0x4D, VOID, -1, 0x3A, 0x4B);
		OPCODES[0x4E] = new Opcode("astore_3", 0x4E, VOID, -1, 0x3A, 0x4B);
		OPCODES[0x4F] = new Opcode("iastore", 0x4F, VOID, -3);
		OPCODES[0x50] = new Opcode("lastore", 0x50, VOID, -3);
		OPCODES[0x51] = new Opcode("fastore", 0x51, VOID, -3);
		OPCODES[0x52] = new Opcode("dastore", 0x52, VOID, -3);
		OPCODES[0x53] = new Opcode("aastore", 0x53, VOID, -3);
		OPCODES[0x54] = new Opcode("bastore", 0x54, VOID, -3);
		OPCODES[0x55] = new Opcode("castore", 0x55, VOID, -3);
		OPCODES[0x56] = new Opcode("sastore", 0x56, VOID, -3);
		OPCODES[0x57] = new Opcode("pop", 0x57, VOID, -1);
		OPCODES[0x58] = new Opcode("pop2", 0x58, VOID, -2);
		OPCODES[0x59] = new Opcode("dup", 0x59, VOID, 1);
		OPCODES[0x5A] = new Opcode("dup_x1", 0x5A, VOID, 2);
		OPCODES[0x5B] = new Opcode("dup_x2", 0x5B, VOID, 3);
		OPCODES[0x5C] = new Opcode("dup2", 0x5C, VOID, 1);
		OPCODES[0x5D] = new Opcode("dup2_x1", 0x5D, VOID, 2);
		OPCODES[0x5E] = new Opcode("dup2_x2", 0x5E, VOID, 3);
		OPCODES[0x5F] = new Opcode("swap", 0x5F, VOID, 0);
		OPCODES[0x60] = new Opcode("iadd", 0x60, VOID, -1);
		OPCODES[0x61] = new Opcode("ladd", 0x61, VOID, -1);
		OPCODES[0x62] = new Opcode("fadd", 0x62, VOID, -1);
		OPCODES[0x63] = new Opcode("dadd", 0x63, VOID, -1);
		OPCODES[0x64] = new Opcode("isub", 0x64, VOID, -1);
		OPCODES[0x65] = new Opcode("lsub", 0x65, VOID, -1);
		OPCODES[0x66] = new Opcode("fsub", 0x66, VOID, -1);
		OPCODES[0x67] = new Opcode("dsub", 0x67, VOID, -1);
		OPCODES[0x68] = new Opcode("imul", 0x68, VOID, -1);
		OPCODES[0x69] = new Opcode("lmul", 0x69, VOID, -1);
		OPCODES[0x6A] = new Opcode("fmul", 0x6A, VOID, -1);
		OPCODES[0x6B] = new Opcode("dmul", 0x6B, VOID, -1);
		OPCODES[0x6C] = new Opcode("idiv", 0x6C, VOID, -1);
		OPCODES[0x6D] = new Opcode("ldiv", 0x6D, VOID, -1);
		OPCODES[0x6E] = new Opcode("fdiv", 0x6E, VOID, -1);
		OPCODES[0x6F] = new Opcode("ddiv", 0x6F, VOID, -1);
		OPCODES[0x70] = new Opcode("irem", 0x70, VOID, -1);
		OPCODES[0x71] = new Opcode("lrem", 0x71, VOID, -1);
		OPCODES[0x72] = new Opcode("frem", 0x72, VOID, -1);
		OPCODES[0x73] = new Opcode("drem", 0x73, VOID, -1);
		OPCODES[0x74] = new Opcode("ineg", 0x74, VOID, 0);
		OPCODES[0x75] = new Opcode("lneg", 0x75, VOID, 0);
		OPCODES[0x76] = new Opcode("fneg", 0x76, VOID, 0);
		OPCODES[0x77] = new Opcode("dneg", 0x77, VOID, 0);
		OPCODES[0x78] = new Opcode("ishl", 0x78, VOID, -1);
		OPCODES[0x79] = new Opcode("lshl", 0x79, VOID, -1);
		OPCODES[0x7A] = new Opcode("ishr", 0x7A, VOID, -1);
		OPCODES[0x7B] = new Opcode("lshr", 0x7B, VOID, -1);
		OPCODES[0x7C] = new Opcode("iushr", 0x7C, VOID, -1);
		OPCODES[0x7D] = new Opcode("lushr", 0x7D, VOID, -1);
		OPCODES[0x7E] = new Opcode("iand", 0x7E, VOID, -1);
		OPCODES[0x7F] = new Opcode("land", 0x7F, VOID, -1);
		OPCODES[0x80] = new Opcode("ior", 0x80, VOID, -1);
		OPCODES[0x81] = new Opcode("lor", 0x81, VOID, -1);
		OPCODES[0x82] = new Opcode("ixor", 0x82, VOID, -1);
		OPCODES[0x83] = new Opcode("lxor", 0x83, VOID, -1);
		OPCODES[0x84] = new Opcode("iinc", 0x84, IINC, 0);
		OPCODES[0x85] = new Opcode("i2l", 0x85, VOID, 0);
		OPCODES[0x86] = new Opcode("i2f", 0x86, VOID, 0);
		OPCODES[0x87] = new Opcode("i2d", 0x87, VOID, 0);
		OPCODES[0x88] = new Opcode("l2i", 0x88, VOID, 0);
		OPCODES[0x89] = new Opcode("l2f", 0x89, VOID, 0);
		OPCODES[0x8A] = new Opcode("l2d", 0x8A, VOID, 0);
		OPCODES[0x8B] = new Opcode("f2i", 0x8B, VOID, 0);
		OPCODES[0x8C] = new Opcode("f2l", 0x8C, VOID, 0);
		OPCODES[0x8D] = new Opcode("f2d", 0x8D, VOID, 0);
		OPCODES[0x8E] = new Opcode("d2i", 0x8E, VOID, 0);
		OPCODES[0x8F] = new Opcode("d2l", 0x8F, VOID, 0);
		OPCODES[0x90] = new Opcode("d2f", 0x90, VOID, 0);
		OPCODES[0x91] = new Opcode("i2b", 0x91, VOID, 0);
		OPCODES[0x92] = new Opcode("i2c", 0x92, VOID, 0);
		OPCODES[0x93] = new Opcode("i2s", 0x93, VOID, 0);
		OPCODES[0x94] = new Opcode("lcmp", 0x94, VOID, -1);
		OPCODES[0x95] = new Opcode("fcmpl", 0x95, VOID, -1);
		OPCODES[0x96] = new Opcode("fcmpg", 0x96, VOID, -1);
		OPCODES[0x97] = new Opcode("dcmpl", 0x97, VOID, -1);
		OPCODES[0x98] = new Opcode("dcmpg", 0x98, VOID, -1);
		OPCODES[0x99] = new Opcode("ifeq", 0x99, VOID, -1);
		OPCODES[0x9A] = new Opcode("ifne", 0x9A, VOID, -1);
		OPCODES[0x9B] = new Opcode("iflt", 0x9B, VOID, -1);
		OPCODES[0x9C] = new Opcode("ifge", 0x9C, VOID, -1);
		OPCODES[0x9D] = new Opcode("ifgt", 0x9D, VOID, -1);
		OPCODES[0x9E] = new Opcode("ifle", 0x9E, VOID, -1);
		OPCODES[0x9F] = new Opcode("if_icmpeq", 0x9F, JMP, -2);
		OPCODES[0xA0] = new Opcode("if_icmpne", 0xA0, JMP, -2);
		OPCODES[0xA1] = new Opcode("if_icmplt", 0xA1, JMP, -2);
		OPCODES[0xA2] = new Opcode("if_icmpge", 0xA2, JMP, -2);
		OPCODES[0xA3] = new Opcode("if_icmpgt", 0xA3, JMP, -2);
		OPCODES[0xA4] = new Opcode("if_icmple", 0xA4, JMP, -2);
		OPCODES[0xA5] = new Opcode("if_acmpeq", 0xA5, JMP, -2);
		OPCODES[0xA6] = new Opcode("if_acmpne", 0xA6, JMP, -2);
		OPCODES[0xA7] = new Opcode("goto", 0xA7, JMP, 0);
		OPCODES[0xA8] = new Opcode("jsr", 0xA8, JMP, 0);
		OPCODES[0xA9] = new Opcode("ret", 0xA9, VOID, 0);
		OPCODES[0xAA] = new Opcode("tableswitch", 0xAA, SWITCH_TBL, 0);
		OPCODES[0xAB] = new Opcode("lookupswitch", 0xAB, SWITCH_LKP, 0);
		OPCODES[0xAC] = new Opcode("ireturn", 0xAC, VOID, -1);
		OPCODES[0xAD] = new Opcode("lreturn", 0xAD, VOID, -1);
		OPCODES[0xAE] = new Opcode("freturn", 0xAE, VOID, -1);
		OPCODES[0xAF] = new Opcode("dreturn", 0xAF, VOID, -1);
		OPCODES[0xB0] = new Opcode("areturn", 0xB0, VOID, -1);
		OPCODES[0xB1] = new Opcode("return", 0xB1, VOID, 0);
		OPCODES[0xB2] = new Opcode("getstatic", 0xB2, FIELD, 1);
		OPCODES[0xB3] = new Opcode("putstatic", 0xB3, FIELD, -1);
		OPCODES[0xB4] = new Opcode("getfield", 0xB4, FIELD, 1);
		OPCODES[0xB5] = new Opcode("putfield", 0xB5, FIELD, -1);
		OPCODES[0xB6] = new Opcode("invokevirtual", 0xB6, METHOD, STACK_DYNC);
		OPCODES[0xB7] = new Opcode("invokespecial", 0xB7, METHOD, STACK_DYNC);
		OPCODES[0xB8] = new Opcode("invokestatic", 0xB8, METHOD, STACK_DYNC);
		OPCODES[0xB9] = new Opcode("invokeinterface", 0xB9, METHOD_IF, STACK_DYNC);
		OPCODES[0xBA] = new Opcode("invokedynamic", 0xBA, INVKDYNC, STACK_DYNC);
		OPCODES[0xBB] = new Opcode("new", 0xBB, TYPE, 1);
		OPCODES[0xBC] = new Opcode("newarray", 0xBC, TYPE, 0);
		OPCODES[0xBD] = new Opcode("anewarray", 0xBD, TYPE, 0);
		OPCODES[0xBE] = new Opcode("arraylength", 0xBE, VOID, 0);
		OPCODES[0xBF] = new Opcode("athrow", 0xBF, VOID, -1);
		OPCODES[0xC0] = new Opcode("checkcast", 0xC0, TYPE, 0);
		OPCODES[0xC1] = new Opcode("instanceof", 0xC1, TYPE, 0);
		OPCODES[0xC2] = new Opcode("monitorenter", 0xC2, VOID, -1);
		OPCODES[0xC3] = new Opcode("monitorexit", 0xC3, VOID, -1);
		OPCODES[0xC5] = new Opcode("multianewarray", 0xC5, MARRAY, STACK_DYNC);
		OPCODES[0xC6] = new Opcode("ifnull", 0xC6, JMP, -1);
		OPCODES[0xC7] = new Opcode("ifnonnull", 0xC7, JMP, -1);
	}
	
	static {
		OPCODE_MAP = new HashMap<>();
		OPCODES = new Opcode[0x100];
		init();
	}
}
