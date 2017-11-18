package org.kucro3.jam2.simulator;

import java.lang.reflect.Modifier;

import org.kucro3.jam2.util.Version;
import org.kucro3.jam2.util.context.visitable.VisitedMethodCompound;
import org.kucro3.util.exception.RuntimeExceptions;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MaxsComputer extends MethodVisitor implements Opcodes {
	public MaxsComputer(MethodVisitor mv) 
	{
		super(Version.getASMVersion(), mv);
	}
	
	public MaxsComputer(MethodVisitor mv, String descriptor, boolean isStatic)
	{
		this(mv);
		requireLocal(fastGetArgumentRequirement(descriptor) + (isStatic ? 0 : 1));
	}
	
	public MaxsComputer(VisitedMethodCompound vmc)
	{
		this(vmc, vmc.getDescriptor(), Modifier.isStatic(vmc.getModifier()));
	}
	
	public MaxsComputer() 
	{
		super(Version.getASMVersion());
	}
	
	static int fastGetArgumentRequirement(String descriptor)
	{
		int index, length = 0;
		char temp;
		if((index = descriptor.indexOf('(')) != -1)
			LOOP: for(int i = index + 1; i < descriptor.length(); i++)
				if((temp = descriptor.charAt(i)) == ')')
					return length;
				else if(temp == 'L')
					if((i = descriptor.indexOf(';', i)) != -1)
						length += LENGTH_OBJECT;
					else
						break;
				else if(temp == '[')
					for(i++; i < descriptor.length(); i++)
						if((temp = descriptor.charAt(i)) == '[')
							continue;
						else if(temp == 'L')
							if((i = descriptor.indexOf(';', i)) != -1)
								length += LENGTH_OBJECT;
							else
								break LOOP;
						else
							length += LENGTH_OBJECT;
				else if(temp == 'D' || temp == 'J')
					length += LENGTH_DOUBLE_OR_LONG;
				else
					length += LENGTH_DEFAULT;
		throw new IllegalArgumentException("Illegal descriptor: " + descriptor);
	}
	
	static int fastGetDescriptorRequirement(char prefix)
	{
		if(prefix == 'D' || prefix == 'J')
			return 2;
		else if(prefix == 'V')
			return 0;
		else
			return 1;
	}
	
	static int fastGetDescriptorRequirement(String descriptor)
	{
		return fastGetDescriptorRequirement(descriptor.charAt(0));
	}
	
	static int fastGetReturnRequirement(String descriptor)
	{
		int i;
		if((i = descriptor.indexOf(')')) != -1)
			return fastGetDescriptorRequirement(descriptor.charAt(i + 1));
		throw new IllegalArgumentException("Illegal argument: " + descriptor);
	}
	
	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String desc)
	{
		super.visitFieldInsn(opcode, owner, name, desc);
		int slotLength = fastGetDescriptorRequirement(desc);
		switch(opcode)
		{
		case GETSTATIC:
			push(slotLength);
			break;
			
		case PUTSTATIC:
			pop(slotLength);
			break;
			
		case GETFIELD:
			push(slotLength);
			pop(LENGTH_OBJECT);
			break;
			
		case PUTFIELD:
			pop(slotLength);
			pop(LENGTH_OBJECT);
			break;
			
		default:
			throw new IllegalArgumentException("Illegal opcode: " + opcode);
		}
	}

	@Override
	public void visitIincInsn(int var, int increment)
	{
		super.visitIincInsn(var, increment);
		load(var);
//		store(var);
	}
	
	@Override
	public void visitInsn(int opcode)
	{
		super.visitInsn(opcode);
		if(opcode == NOP) // nop
			return;
		else if(opcode == ACONST_NULL) // aconst_null
			push(LENGTH_OBJECT);
		else if(opcode >= ICONST_M1 && opcode <= DCONST_1) // xconst_x
			if(opcode <= ICONST_5
			||(opcode >= FCONST_0 && opcode <= FCONST_2)) // i/fconst_x
				push(LENGTH_DEFAULT);
			else // l/dconst_x
				push(LENGTH_DOUBLE_OR_LONG);
//		else if(opcode >= 0x1A /*iload_0*/ && opcode <= 0x2D /*aload_3*/) // xload_x (unsupported/converted by ASM)
//			if(opcode <= 0x1D
//			||(opcode >= 0x22 && opcode <= 0x25)) // i/fload_x
//				push(LENGTH_DEFAULT);
//			else if(opcode >= 0x2A) // aload_x
//				push(LENGTH_OBJECT);
//			else // l/dload_x
//				push(LENGTH_DOUBLE_OR_LONG);
		else if(opcode >= IALOAD && opcode <= SALOAD)
		{
			pop(LENGTH_OBJECT);
			pop(LENGTH_DEFAULT);
			if(opcode == LALOAD || opcode == DALOAD) // l/daload
				push(LENGTH_DOUBLE_OR_LONG);
			else if(opcode == AASTORE)
				push(LENGTH_OBJECT);
			else
				push(LENGTH_DEFAULT);
		}
//		else if(opcode >= 0x3B /*istore_0*/ && opcode <= 0x4E /*astore_3*/) // xstore_x (unsupported/converted by ASM)
//			if(opcode <= 0x3E
//			||(opcode >= 0x43 && opcode <= 0x46)) // i/fstore_x
//				pop(LENGTH_DEFAULT);
//			else if(opcode >= 0x4B) // astore_x
//				pop(LENGTH_OBJECT);
//			else  // l/dstore_x
//				pop(LENGTH_DOUBLE_OR_LONG);
		else if(opcode >= IASTORE && opcode <= SASTORE)
		{
			pop(LENGTH_OBJECT);
			pop(LENGTH_DEFAULT);
			if(opcode == LASTORE || opcode == DASTORE) // l/dastore
				pop(LENGTH_DOUBLE_OR_LONG);
			else if(opcode == AASTORE)
				pop(LENGTH_OBJECT);
			else
				pop(LENGTH_DEFAULT);
		}
		else if(opcode >= POP && opcode <= SWAP)
		{
			if(opcode > POP2)
				switch(opcode)
				{
				case DUP: 		pop(1);		push(2);	break;
				case DUP_X1:	pop(2);		push(3);	break;
				case DUP_X2:	pop(3);		push(4);	break;
				case DUP2:		pop(2);		push(4);	break;
				case DUP2_X1:	pop(3);		push(5);	break;
				case DUP2_X2:	pop(4);		push(6);	break;
				case SWAP:		pop(1);		push(1);	break;
				default:
					RuntimeExceptions.shouldNotReachHere();
				}
			else if(opcode != POP2) // pop
				pop(1);
			else // pop2
				pop(2);
		}
		else if(opcode >= IADD && opcode <= LXOR)
			if((opcode & 0x01) == 0) // i/f
			{
				pop(LENGTH_DEFAULT);
				pop(LENGTH_DEFAULT);
				push(LENGTH_DEFAULT);
			}
			else // l/d
			{
				pop(LENGTH_DOUBLE_OR_LONG);
				if(opcode >= ISHL && opcode <= LUSHR)
					pop(LENGTH_DEFAULT);
				else
					pop(LENGTH_DOUBLE_OR_LONG);
				push(LENGTH_DOUBLE_OR_LONG);
			}
		else if(opcode >= I2L && opcode <= I2S)
			switch(opcode)
			{
			case I2L:	pop(LENGTH_DEFAULT);		push(LENGTH_DOUBLE_OR_LONG);	break;
			case I2F:	pop(LENGTH_DEFAULT);		push(LENGTH_DEFAULT);			break;
			case I2D:	pop(LENGTH_DEFAULT);		push(LENGTH_DOUBLE_OR_LONG);	break;
			case L2I:	pop(LENGTH_DOUBLE_OR_LONG);	push(LENGTH_DEFAULT);			break;
			case L2F:	pop(LENGTH_DOUBLE_OR_LONG);	push(LENGTH_DEFAULT);			break;
			case L2D:	pop(LENGTH_DOUBLE_OR_LONG);	push(LENGTH_DOUBLE_OR_LONG);	break;
			case F2I:	pop(LENGTH_DEFAULT);		push(LENGTH_DEFAULT);			break;
			case F2L:	pop(LENGTH_DEFAULT);		push(LENGTH_DOUBLE_OR_LONG);	break;
			case F2D:	pop(LENGTH_DEFAULT);		push(LENGTH_DOUBLE_OR_LONG);	break;
			case D2I:	pop(LENGTH_DOUBLE_OR_LONG);	push(LENGTH_DEFAULT);			break;
			case D2L:	pop(LENGTH_DOUBLE_OR_LONG);	push(LENGTH_DOUBLE_OR_LONG);	break;
			case D2F:	pop(LENGTH_DOUBLE_OR_LONG);	push(LENGTH_DEFAULT);			break;
			case I2B:	pop(LENGTH_DEFAULT);		push(LENGTH_DEFAULT);			break;
			case I2C:	pop(LENGTH_DEFAULT);		push(LENGTH_DEFAULT);			break;
			case I2S:	pop(LENGTH_DEFAULT);		push(LENGTH_DEFAULT);			break;
			default:
				RuntimeExceptions.shouldNotReachHere();
			}
		else if(opcode >= LCMP && opcode <= DCMPG)
		{
			if(opcode == DCMPL || opcode == DCMPG)
			{
				pop(LENGTH_DOUBLE_OR_LONG);
				pop(LENGTH_DOUBLE_OR_LONG);
			}
			else
			{
				pop(LENGTH_DEFAULT);
				pop(LENGTH_DEFAULT);
			}
			push(LENGTH_DEFAULT);
		}
		else if(opcode == RET)
			return;
		else if(opcode >= IRETURN && opcode <= RETURN)
			if(opcode == RETURN)
				return;
			else if(opcode == ARETURN)
				pop(LENGTH_OBJECT);
			else if(opcode == DRETURN || opcode == LRETURN)
				pop(LENGTH_DOUBLE_OR_LONG);
			else
				pop(LENGTH_DEFAULT);
		else switch(opcode)
		{
		case ARRAYLENGTH:	pop(LENGTH_OBJECT);	push(LENGTH_DEFAULT);	break;
		case ATHROW:		pop(LENGTH_OBJECT);							break;
		case MONITORENTER:	pop(LENGTH_OBJECT);							break;
		case MONITOREXIT:	pop(LENGTH_OBJECT);							break;
		default:
			throw new IllegalArgumentException("Illegal opcode: " + opcode);
		}
	}
	
	@Override
	public void visitIntInsn(int opcode, int operand)
	{
		super.visitIntInsn(opcode, operand);
		if(opcode == BIPUSH || opcode == SIPUSH)
			push(LENGTH_DEFAULT);
		else
			throw new IllegalArgumentException("Illegal opcode: " + opcode);
	}
	
	@Override
	public void visitJumpInsn(int opcode, Label label)
	{
		super.visitJumpInsn(opcode, label);
		if(opcode >= IFEQ && opcode <= IFLE)
			pop(LENGTH_DEFAULT);
		else if(opcode >= IF_ICMPEQ && opcode <= IF_ACMPNE)
			if(opcode >= IF_ACMPEQ)
			{
				pop(LENGTH_OBJECT);
				pop(LENGTH_OBJECT);
			}
			else
			{
				pop(LENGTH_DEFAULT);
				pop(LENGTH_DEFAULT);
			}
		else switch(opcode)
		{
		case GOTO:							break;
		case JSR:							break;
		case IFNULL:	pop(LENGTH_OBJECT);	break;
		case IFNONNULL:	pop(LENGTH_OBJECT);	break;
		default:
			throw new IllegalArgumentException("Illegal opcode: " + opcode);
		}
	}
	
	@Override
	public void visitLdcInsn(Object obj)
	{
		super.visitLdcInsn(obj);
		if(obj instanceof Number || obj instanceof Character || obj instanceof Boolean)
			if(obj instanceof Long || obj instanceof Double)
				push(LENGTH_DOUBLE_OR_LONG);
			else
				push(LENGTH_DEFAULT);
		else
			push(LENGTH_OBJECT);
	}
	
	@Deprecated
	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc)
	{
		super.visitMethodInsn(opcode, owner, name, desc);
		_visitMethodInsn(opcode, owner, name, desc);
	}
	
	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean isInterface)
	{
		super.visitMethodInsn(opcode, owner, name, desc, isInterface);
		_visitMethodInsn(opcode, owner, name, desc);
	}
	
	void _visitMethodInsn(int opcode, String owner, String name, String desc)
	{
		switch(opcode)
		{
		case INVOKEVIRTUAL:
		case INVOKESPECIAL:
		case INVOKEINTERFACE:
			pop(LENGTH_OBJECT);
			pop(fastGetArgumentRequirement(desc));
			push(fastGetReturnRequirement(desc));
			break;
			
		case INVOKESTATIC:
			pop(fastGetArgumentRequirement(desc));
			push(fastGetReturnRequirement(desc));
			break;
			
		default:
			throw new IllegalArgumentException("Illegal opcode: " + opcode);
		}
	}
	
	@Override
	public void visitMultiANewArrayInsn(String desc, int dims)
	{
		super.visitMultiANewArrayInsn(desc, dims);
		for(int i = 0; i < dims; i++)
			pop(LENGTH_DEFAULT);
	}
	
	@Override
	public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels)
	{
		super.visitLookupSwitchInsn(dflt, keys, labels);
		pop(LENGTH_DEFAULT);
	}
	
	@Override
	public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels)
	{
		super.visitTableSwitchInsn(min, max, dflt, labels);
		pop(LENGTH_DEFAULT);
	}
	
	@Override
	public void visitTypeInsn(int opcode, String type)
	{
		super.visitTypeInsn(opcode, type);
		switch(opcode)
		{
		case NEW:			;						push(LENGTH_OBJECT);	break;
		case NEWARRAY:		pop(LENGTH_DEFAULT);	push(LENGTH_OBJECT);	break;
		case ANEWARRAY:		pop(LENGTH_DEFAULT);	push(LENGTH_OBJECT);	break;
		case CHECKCAST:		pop(LENGTH_OBJECT);		push(LENGTH_OBJECT);	break;
		case INSTANCEOF:	pop(LENGTH_OBJECT);		push(LENGTH_DEFAULT);	break;
		default:
			throw new IllegalArgumentException("Illegal opcode: " + opcode);
		}
	}
	
	@Override
	public void visitVarInsn(int opcode, int var)
	{
		super.visitVarInsn(opcode, var);
		if(opcode >= ILOAD && opcode <= ALOAD)
			if(opcode == DLOAD || opcode == LLOAD)
			{
				load(var + 1);
				push(LENGTH_DOUBLE_OR_LONG);
			}
			else
			{
				load(var);
				if (opcode == ALOAD)
					push(LENGTH_OBJECT);
				else
					push(LENGTH_DEFAULT);
			}
		else if(opcode >= ISTORE && opcode <= ASTORE)
			if(opcode == DSTORE || opcode == DSTORE)
			{
				store(var + 1);
				pop(LENGTH_DOUBLE_OR_LONG);
			}
			else
			{
				if (opcode == ASTORE)
					pop(LENGTH_OBJECT);
				else
					pop(LENGTH_DEFAULT);
				store(var);
			}
		else
			throw new IllegalArgumentException("Illegal opcode: " + opcode);
	}
	
	@Override
	public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs)
	{
		super.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
		pop(fastGetArgumentRequirement(desc));
		push(fastGetReturnRequirement(desc));
	}
	
	public int requireLocal(int index)
	{
		return local = Math.max(index + 1, local);
	}
	
	public int requireStack(int length)
	{
		int i;
		if((i = stacks - length) >= 0)
			return i;
		throw new IllegalStateException("Stack underflow");
	}
	
	public void load(int index)
	{
		requireLocal(index);
	}
	
	public void store(int index)
	{
		requireLocal(index);
	}
	
	public void push(int length)
	{
		if(length == 0)
			return;
		maxStack = Math.max(stacks += length, maxStack);
	}
	
	public void pop(int length)
	{
		if(length == 0)
			return;
		maxStack = Math.max(stacks = requireStack(length), maxStack);
	}
	
	public int getCurrentLocalMax()
	{
		return local;
	}
	
	public int getCurrentStackMax()
	{
		return maxStack;
	}
	
	public int getCurrentStack()
	{
		return stacks;
	}
	
	@Override
	public void visitMaxs(int maxStack, int maxLocals)
	{
		if(maxStack == 0 && maxLocals == 0)
			visitMaxs();
		else
			super.visitMaxs(maxStack, maxLocals);
	}
	
	public void visitMaxs()
	{
		super.visitMaxs(getCurrentStackMax(), getCurrentLocalMax());
	}
	
	public void visitMaxs(String descriptor, boolean isStatic)
	{
		requireLocal(fastGetArgumentRequirement(descriptor) + (isStatic ? 0 : 1));
		visitMaxs();
	}
	
	public static final int LENGTH_DEFAULT = 1;
	
	public static final int LENGTH_OBJECT = 1;
	
	public static final int LENGTH_DOUBLE_OR_LONG = 2;
	
	private int local;
	
	private int maxStack;
	
	private int stacks;
}