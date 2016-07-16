package org.kucro3.jam2.util;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public final class Jam2Util extends ClassLoader implements Opcodes {
	private Jam2Util()
	{
		super(getProvidedClassLoader());
	}
	
	public static Jam2Util getInstance()
	{
		return INSTANCE;
	}
	
	public static ClassLoader getProvidedClassLoader()
	{
		return Jam2Util.class.getClassLoader();
	}
	
	public static Class<?> newClass(String name, ClassWriter writer)
	{
		return newClass(name, writer.toByteArray());
	}
	
	public static Class<?> newClass(String name, byte[] byts)
	{
		return newClass(name, byts, 0, byts.length);
	}
	
	public static Class<?> newClass(String name, byte[] byts, int off, int len)
	{
		return INSTANCE.defineClass(name, byts, off, len);
	}
	
	public static void pushEmptyConstructor(ClassWriter cw, int modifiers, Class<?> superClass)
	{
		MethodVisitor mv = __init__(cw, modifiers, "()V", null);
		mv.visitCode();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, Type.getInternalName(superClass),
				"<init>", "()V", false);
		mv.visitInsn(RETURN);
		mv.visitMaxs(1, 1);
		mv.visitEnd();
	}
	
	public static void pushNewInstance(ClassWriter cw, int modifiers, String methodName,
			Class<?> type, Class<?>[] constructorArguments, boolean varargs)
	{
		pushNewInstance(cw, modifiers, methodName, Type.getInternalName(type), 
				varargs ? null : _toDescriptors(constructorArguments), varargs);
	}
	
	public static void pushNewInstance(ClassWriter cw, int modifiers, String methodName,
			String type, String[] constructorArguments, boolean varargs)
	{
		
	}
	
	public static void pushFieldGetter(ClassWriter cw, int modifiers, String name,
			FieldContext ctx, boolean objReturn)
	{
		pushFieldGetter(cw, ctx.getModifier(), name,
				ctx.getDeclaringClassInternalName(), ctx.getFieldName(), ctx.getDescriptor(),
				FieldType.fromModifier(ctx.getModifier()), objReturn);
	}
	
	public static void pushFieldGetter(ClassWriter cw, int modifiers, String name,
			Class<?> owner, String fieldName, Class<?> type, FieldType ft, boolean objReturn)
	{
		pushFieldGetter(cw, modifiers, name, Type.getInternalName(owner), fieldName, Type.getDescriptor(type), ft, objReturn);
	}
	
	public static void pushFieldGetter(ClassWriter cw, int modifiers, String name, 
			String ownerInternalName, String fieldName, String type, FieldType ft, boolean objReturn)
	{
		boolean selfStatic = (modifiers & ACC_STATIC) != 0;
		int delta = selfStatic ? -1 : 0;
		MethodVisitor mv = _newMethod(cw, modifiers, name, objReturn ? "Ljava/lang/Object;" : type, 
				Type.getObjectType(ownerInternalName).getDescriptor(), null);
		mv.visitCode();
		if(!ft.isStatic())
		{
			mv.visitVarInsn(ALOAD, 1 + delta);
			mv.visitTypeInsn(CHECKCAST, ownerInternalName);
		}
		mv.visitFieldInsn(ft.get(), ownerInternalName, fieldName, type);
		if(objReturn)
		{
			pushBoxing(mv, type);
			mv.visitInsn(ARETURN);
		}
		else
			mv.visitInsn(_forReturnInsn(type));
		mv.visitMaxs(2 + (ft.isStatic() ? -1 : 0), 2 + delta);
		mv.visitEnd();
	}
	
	public static void pushFieldSetter(ClassWriter cw, int modifiers, String name,
			FieldContext ctx, boolean objReturn)
	{
		pushFieldSetter(cw, modifiers, name,
				ctx.getDeclaringClassInternalName(), ctx.getFieldName(), ctx.getDescriptor(),
				FieldType.fromModifier(ctx.getModifier()), objReturn);
	}
	
	public static void pushFieldSetter(ClassWriter cw, int modifiers, String name,
			Class<?> owner, String fieldName, Class<?> type, FieldType ft, boolean objArgument)
	{
		pushFieldSetter(cw, modifiers, name, Type.getInternalName(owner), fieldName, Type.getDescriptor(type), ft, objArgument);
	}
	
	public static void pushFieldSetter(ClassWriter cw, int modifiers, String name,
			String ownerInternalName, String fieldName, String type, FieldType ft, boolean objArgument)
	{		
		boolean selfStatic = (modifiers & ACC_STATIC) != 0;
		int delta = selfStatic ? -1 : 0;
		MethodVisitor mv = _newMethod(cw, modifiers, name, DESCRIPTOR_VOID,
				new String[] {"Ljava/lang/Object;", objArgument ? "Ljava/lang/Object;" : type}, null);
		mv.visitCode();
		if(!ft.isStatic())
		{
			mv.visitVarInsn(ALOAD, 1 + delta);
			mv.visitTypeInsn(CHECKCAST, ownerInternalName);
		}
		mv.visitVarInsn(ALOAD, 2 + delta);
		if(objArgument)
		{
			String boxingMapped;
			if((boxingMapped = _boxingMapped(type)) != null)
			{
				mv.visitTypeInsn(CHECKCAST, boxingMapped);
				pushUnboxing(mv, type);
			}
		}
		mv.visitFieldInsn(ft.put(), ownerInternalName, fieldName, type);
		mv.visitInsn(RETURN);
		mv.visitMaxs(2 + (ft.isStatic() ? -1 : 0), 3 + delta);
		mv.visitEnd();
	}
	
	public static void pushGetter(ClassWriter cw, int modifiers, String name, FieldContext ctx)
	{
		pushGetter(cw, modifiers, ctx.getDeclaringClassInternalName(),
				name, ctx.getFieldName(), ctx.getDescriptor(), FieldType.fromModifier(ctx.getModifier()));
	}
	
	public static void pushGetter(ClassWriter cw, int modifiers, String ownerInternalName,
			String name, String fieldName, Class<?> type, FieldType ft)
	{
		pushGetter(cw, modifiers, ownerInternalName, name, fieldName, Type.getDescriptor(type), ft);
	}
	
	public static void pushGetter(ClassWriter cw, int modifiers, String ownerInternalName, 
			String name, String fieldname, String type, FieldType ft)
	{
		MethodVisitor mv = _newMethod(cw, modifiers, name, type, (String[])null, null);
		mv.visitCode();
		if(!ft.isStatic())
			mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(ft.get(), ownerInternalName, fieldname, type);
		mv.visitInsn(_forReturnInsn(type));
		mv.visitMaxs(1, ft.isStatic() ? 0 : 1);
		mv.visitEnd();
	}
	
	public static void pushSetter(ClassWriter cw, int modifiers, String name, FieldContext ctx)
	{
		pushSetter(cw, modifiers, ctx.getDeclaringClassInternalName(),
				name, ctx.getFieldName(), ctx.getDescriptor(), FieldType.fromModifier(ctx.getModifier()));
	}
	
	public static void pushSetter(ClassWriter cw, int modifiers, String ownerInternalName,
			String name, String fieldName, Class<?> type, FieldType ft)
	{
		pushSetter(cw, modifiers, ownerInternalName, name, fieldName, Type.getDescriptor(type), ft);
	}
	
	public static void pushSetter(ClassWriter cw, int modifiers, String ownerInternalName,
			String name, String fieldname, String type, FieldType ft)
	{
		int maxs = ft.isStatic() ? 1 : 2;
		
		MethodVisitor mv = _newMethod(cw, modifiers, name, DESCRIPTOR_VOID, type, null);
		mv.visitCode();
		if(!ft.isStatic())
			mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(_forLocalLoadInsn(type), 1);
		mv.visitFieldInsn(ft.put(), ownerInternalName, fieldname, type);
		mv.visitInsn(RETURN);
		mv.visitMaxs(maxs, maxs);
		mv.visitEnd();
	}
	
	public static void pushJavaBean(ClassWriter cw, FieldContext ctx)
	{
		pushJavaBean(cw, ctx.getDeclaringClassInternalName(), ctx.getFieldName(), ctx.getDescriptor());
	}
	
	public static void pushJavaBean(ClassWriter cw, String ownerInternalName, String fieldName, Class<?> type)
	{
		pushJavaBean(cw, ownerInternalName, fieldName, Type.getDescriptor(type));
	}
	
	public static void pushJavaBean(ClassWriter cw, String ownerInternalName, String fieldName, String type)
	{
		String namedJavaBean = _namingJavaBean(fieldName);
		newField(cw, ACC_PRIVATE, fieldName, type);
		pushGetter(cw, ACC_PUBLIC, ownerInternalName, _namingJavaBeanGetter(namedJavaBean), fieldName, type, FieldType.NONSTATIC);
		pushSetter(cw, ACC_PUBLIC, ownerInternalName, _namingJavaBeanSetter(namedJavaBean), fieldName, type, FieldType.NONSTATIC);
	}
	
	public static void pushCaller(ClassWriter cw, int modifiers, String name,
			Class<?> callingClass, String methodName, Class<?> returnType, Class<?>[] arguments, CallingType ct,
			boolean vargs, boolean objReturn)
	{
		_pushCaller$(cw, modifiers, name, callingClass, methodName, returnType, arguments, ct,
				vargs, objReturn, MaxsContext.newDefaultContext(),
				LAMBDA__TO_DESCRIPTOR_$0_0, LAMBDA_PUSHCALLER_$0_0, LAMBDA_PUSHCALLER_$1_0, LAMBDA_PUSHCALLER_$2_0);
	}
	
	public static void pushCaller(ClassWriter cw, int modifiers, String name,
			String callingClass, String methodName, String returnType, String[] arguments, CallingType ct,
			boolean vargs, boolean objReturn)
	{
		_pushCaller$(cw, modifiers, name, callingClass, methodName, returnType, arguments, ct,
				vargs, objReturn, MaxsContext.newDefaultContext(),
				LAMBDA__TO_DESCRIPTOR_$0_1, LAMBDA_PUSHCALLER_$0_1, LAMBDA_PUSHCALLER_$1_1, LAMBDA_PUSHCALLER_$2_1);
	}
	
	public static void pushCaller(ClassWriter cw, int modifiers, String name, MethodContext callingCtx, CallingType ct,
			boolean vargs, boolean objReturn)
	{
		pushCaller(cw, modifiers, name,
				callingCtx.getDeclaringClassInternalName(), callingCtx.getMethodName(), callingCtx.getReturnTypeDescriptor(),
				callingCtx.getArgumentDescriptors(), ct, vargs, objReturn);
	}
	
	private static void _pushCaller$(ClassWriter cw, int modifiers, String name,
			Object callingClass, String methodName, Object returnType, Object[] arguments, CallingType ct,
			boolean vargs, boolean objReturn, MaxsContext mctx,
			LAMBDA__TODESCRIPTOR_$0 lambda0,
			LAMBDA_PUSHCALLER_$0 lambda1, LAMBDA_PUSHCALLER_$1 lambda2, LAMBDA_PUSHCALLER_$2 lambda3)
	{
		boolean callerStatic = (modifiers & ACC_STATIC) != 0;
		if(arguments == null)
			arguments = new String[0];
		String callingDescriptor =  _toDescriptor$(returnType, arguments, "", lambda0);
		returnType = objReturn ? lambda2.function() : returnType;
		String descriptor = vargs ? 
				_toDescriptor$(returnType, null, "Ljava/lang/Object;[Ljava/lang/Object;", lambda0) :
				_toDescriptor$(returnType, arguments, "Ljava/lang/Object;", lambda0);
		boolean isStatic = ct == CallingType.STATIC;
		boolean isVoid = callingDescriptor.endsWith("V");
		int insn = ct.getMethodInsn();
		
		if(vargs)
			modifiers |= ACC_VARARGS;
		
		MethodVisitor mv = _newMethod(cw, modifiers, name, descriptor, null);
		mv.visitCode();
		
		String returnTypeDescriptor =
				_pushCallerProcessCallingPreprocess(mv, arguments.length, lambda3.function(callingClass), 
						callingDescriptor, vargs, isStatic, callerStatic, mctx);
		_pushCallerProcessCalling(mv, insn, lambda3.function(callingClass), methodName, callingDescriptor, ct.getFlag());
		_pushCallerProcessReturn(mv, returnTypeDescriptor, objReturn, isVoid);
		
		mctx.visitMaxs(mv);
		mv.visitEnd();
	}
	
	static String _pushCallerProcessCallingPreprocess(MethodVisitor mv, int argumentLength, String ownerInternalName, String callingDescriptor,
			boolean varargs, boolean isStatic, boolean selfStatic, MaxsContext mctx)
	{
		int localDelta = selfStatic ? -1 : 0;
		if(!isStatic)
		{
			mv.visitVarInsn(ALOAD, 1 + localDelta);
			mv.visitTypeInsn(CHECKCAST, ownerInternalName);
		}
		if(varargs)
		{
			int[] i = new int[] {1};
			for(int j = 0; j < argumentLength; j++)
			{
				mv.visitVarInsn(ALOAD, 2 + localDelta);
				mv.visitIntInsn(BIPUSH, j);
				mv.visitInsn(AALOAD);
				String nd = _nextDescriptor(callingDescriptor, i);
				mv.visitTypeInsn(CHECKCAST, Type.getType(_tryBoxingMapped(nd)).getInternalName());
				pushUnboxing(mv, nd);
			}
			mctx.updateMaxs(argumentLength + 3, 3 + localDelta);
			return _nextDescriptor(callingDescriptor, i);
		}
		else
		{
			for(int j = 0; j < argumentLength; j++)
				mv.visitVarInsn(ALOAD, j + 2 + localDelta);
			mctx.updateMaxs(argumentLength + 1, argumentLength + 2 + localDelta);
			return _getReturnDescriptor(callingDescriptor);
		}
	}
	
	static void _pushCallerProcessCalling(MethodVisitor mv, int insn, String callingClassInternalName, String methodName,
			String methodDescriptor, boolean flag)
	{
		mv.visitMethodInsn(insn, callingClassInternalName, methodName, methodDescriptor, flag);
	}
	
	static void _pushCallerProcessReturn(MethodVisitor mv, String returnTypeDescriptor, boolean objReturn, boolean callingVoid)
	{
		if(!objReturn)
			mv.visitInsn(_forReturnInsn(returnTypeDescriptor));
		else
		{
			if(callingVoid)
				_pushGetVoidObject(mv);
			else
				pushBoxing(mv, returnTypeDescriptor);
			mv.visitInsn(ARETURN);
		}
	}
	
	public static MethodVisitor pushFunctionalLambda(ClassWriter cw, String internalName, MethodVisitor mv, LambdaContext ctx)
	{
		return pushFunctionalLambda(cw, internalName, mv, ctx, false);
	}
	
	public static MethodVisitor pushFunctionalLambda(ClassWriter cw, String internalName, MethodVisitor mv, LambdaContext ctx,
			boolean reserveMethodVisitor)
	{
		MethodVisitor rt;
		
		if((rt = ctx.mv) == null)
		{
			String syntheticInternalName;
			if((syntheticInternalName = ctx.internalLambdaName) == null)
				syntheticInternalName = ctx.internalLambdaName = _generateLambdaFunctionName();
			if(!reserveMethodVisitor)
				rt = ctx.mv = _newMethod(cw, ACC_PRIVATE | ACC_STATIC | ACC_SYNTHETIC, 
						syntheticInternalName, ctx.getDescriptor(), null);
		}
		
		Type implType = Type.getMethodType(Type.getType(ctx.getReturnType()), _toTypes(ctx.getArgumentDescriptors()));
		mv.visitInvokeDynamicInsn(ctx.getFunctionName(),
				_toDescriptor(Type.getObjectType(ctx.getTemplate()).getDescriptor(), null, ""),
				METHOD_HANDLE_LAMBDA_METAFACTORY,
				implType,
				new Handle(H_INVOKESTATIC, internalName, ctx.getInternalLambdaName(), ctx.getDescriptor()),
				implType);
		
		return rt;
	}
	
	public static boolean pushBoxing(MethodVisitor mv, Class<?> type)
	{
		if(!type.isPrimitive())
			return false;
		return pushBoxing(mv, Type.getDescriptor(type));
	}
	
	public static boolean pushBoxing(MethodVisitor mv, String descriptor)
	{
		return _pushBoxingOperation$(mv, descriptor, -1, LAMBDA_PUSHBOXINGOPERATION_$_0);
	}
	
	public static boolean pushUnboxing(MethodVisitor mv, Class<?> type)
	{
		if(!type.isPrimitive())
			return false;
		return pushUnboxing(mv, Type.getDescriptor(type));
	}
	
	public static boolean pushUnboxing(MethodVisitor mv, String descriptor)
	{
		return _pushBoxingOperation$(mv, descriptor, -1, LAMBDA_PUSHBOXINGOPERATION_$_1);
	}
	
	private static boolean _pushBoxingOperation$(MethodVisitor mv, String descriptor, int index, LAMBDA_PUSHBOXINGOPERATION_$0[] lambda)
	{
		if(index < 0)
		{
			Integer objIndex = MAPPED_DESCRIPTOR_INDEX.get(descriptor);
			if(objIndex == null)
				return false;
			index = objIndex.intValue();
		}
		LAMBDA_PUSHBOXINGOPERATION_$0 L0 = lambda[index];
		if(L0 == null)
			return false;
		L0.function(mv);
		return true;
	}
	
	public static void pushBoxingBoolean(MethodVisitor mv)
	{
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false);
	}
	
	public static void pushBoxingByte(MethodVisitor mv)
	{
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;", false);
	}
	
	public static void pushBoxingCharacter(MethodVisitor mv)
	{
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;", false);
	}
	
	public static void pushBoxingShort(MethodVisitor mv)
	{
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;", false);
	}
	
	public static void pushBoxingInteger(MethodVisitor mv)
	{
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
	}
	
	public static void pushBoxingFloat(MethodVisitor mv)
	{
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false);
	}
	
	public static void pushBoxingLong(MethodVisitor mv)
	{
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
	}
	
	public static void pushBoxingDouble(MethodVisitor mv)
	{
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
	}
	
	public static void pushUnboxingBoolean(MethodVisitor mv)
	{
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z", false);
	}
	
	public static void pushUnboxingByte(MethodVisitor mv)
	{
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Byte", "byteValue", "()B" ,false);
	}
	
	public static void pushUnboxingCharacter(MethodVisitor mv)
	{
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Character", "charValue", "()C", false);
	}
	
	public static void pushUnboxingShort(MethodVisitor mv)
	{
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Short", "shortValue", "()S", false);
	}
	
	public static void pushUnboxingInteger(MethodVisitor mv)
	{
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I", false);
	}
	
	public static void pushUnboxingFloat(MethodVisitor mv)
	{
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Float", "floatValue", "()F", false);
	}
	
	public static void pushUnboxingLong(MethodVisitor mv)
	{
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Long", "longValue", "()J", false);
	}
	
	public static void pushUnboxingDouble(MethodVisitor mv)
	{
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D", false);
	}
	
	public static MethodVisitor newFinalize(ClassWriter cw)
	{
		return _newMethod(cw, ACC_PROTECTED, "finalize", void.class, null, null);
	}
	
	public static MethodVisitor newConstructor(ClassWriter cw, int modifiers, Class<?>[] arguments,
			String... throwings)
	{
		return __init__(cw, modifiers, _toDescriptor(void.class, arguments), throwings);
	}
	
	public static MethodVisitor newConstructor(ClassWriter cw, int modifiers, String[] arguments,
			String... throwings)
	{
		return __init__(cw, modifiers, _toDescriptor(DESCRIPTOR_VOID, arguments), throwings);
	}
	
	public static MethodVisitor newConstructor(ClassWriter cw, ConstructorContext ctx)
	{
		if(ctx.mv == null)
			return (ctx.mv = __init__(cw, ctx.getModifier(), _toDescriptor(DESCRIPTOR_VOID, ctx.getArgumentDescriptors(), ""),
					ctx.getExceptions()));
		else
			return ctx.mv;
	}
	
	public static MethodVisitor newStaticBlock(ClassWriter cw)
	{
		return __clinit__(cw);
	}
	
	public static MethodVisitor newMethod(ClassWriter cw, int modifiers, String name, Class<?> returnType,
			Class<?>[] arguments, String... throwings)
	{
		return _newMethod(cw, modifiers, name, returnType, arguments, throwings);
	}
	
	public static MethodVisitor newMethod(ClassWriter cw, int modifiers, String name, String returnType,
			String[] arguments, String... throwings)
	{
		return _newMethod(cw, modifiers, name, returnType, arguments, throwings);
	}
	
	public static MethodVisitor newMethod(ClassWriter cw, MethodContext ctx)
	{
		if(ctx.mv == null)
			return (ctx.mv = _newMethod(cw, ctx.getModifier(), ctx.getMethodName(), ctx.getReturnTypeDescriptor(),
					ctx.getArgumentDescriptors(), ctx.getExceptions()));
		else
			return ctx.mv;
	}
	
	public static FieldVisitor newField(ClassWriter cw, int modifiers, String name, Class<?> type)
	{
		return _newField(cw, modifiers, name, Type.getDescriptor(type));
	}
	
	public static FieldVisitor newField(ClassWriter cw, int modifiers, String name, String type)
	{
		return _newField(cw, modifiers, name, type);
	}
	
	public static FieldVisitor newField(ClassWriter cw, FieldContext ctx)
	{
		if(ctx.fv == null)
			return (ctx.fv = _newField(cw, ctx.getModifier(), ctx.getFieldName(), ctx.getDescriptor()));
		else
			return ctx.fv;
	}
	
	static MethodVisitor _newMethod(ClassWriter cw, int modifiers, String name, String descriptor,
			String[] throwing)
	{
		return cw.visitMethod(modifiers, name, descriptor, null, throwing);
	}
	
	static MethodVisitor _newMethod(ClassWriter cw, int modifiers, String name, String returnType,
			String[] arguments, String[] throwing)
	{
		return cw.visitMethod(modifiers, name, _toDescriptor(returnType, arguments, ""), null, throwing);
	}
	
	static MethodVisitor _newMethod(ClassWriter cw, int modifiers, String name, String returnType,
			String argument, String[] throwing)
	{
		return cw.visitMethod(modifiers, name, "(" + (argument == null ? "" : argument) + ")" + returnType, null, throwing);
	}
	
	static MethodVisitor _newMethod(ClassWriter cw, int modifiers, String name, Class<?> returnType,
			Class<?>[] arguments, String[] throwing)
	{
		return _newMethod(cw, modifiers, name, _toDescriptor(returnType, arguments, ""), throwing);
	}
	
	static FieldVisitor _newField(ClassWriter cw, int modifiers, String name, String type)
	{
		return cw.visitField(modifiers, name, type, null, null);
	}
	
	static String _toDescriptor(Class<?> returnType, Class<?>[] arguments, String prefix)
	{
		return _toDescriptor$(returnType, arguments, prefix, (obj) -> Type.getDescriptor((Class<?>)obj));
	}
	
	static String _toDescriptor(Class<?> returnType, Class<?>[] arguments)
	{
		return _toDescriptor(returnType, arguments, "");
	}
	
	static String _toDescriptor(String returnType, String[] arguments, String prefix)
	{
		return _toDescriptor$(returnType, arguments, prefix, (obj) -> (String)obj);
	}
	
	static String _toDescriptor(String returnType, String[] arguments)
	{
		return _toDescriptor(returnType, arguments, "");
	}
	
	static String _toConstructorDescriptor(Class<?>[] arguments)
	{
		return _toDescriptor(void.class, arguments);
	}
	
	static String _toConstructorDescriptor(String[] arguments)
	{
		return _toDescriptor(DESCRIPTOR_VOID, arguments);
	}
	
	private static String _toDescriptor$(Object returnType, Object[] arguments, String prefix, LAMBDA__TODESCRIPTOR_$0 lambda0)
	{
		Objects.requireNonNull(returnType);
		
		StringBuilder sb = new StringBuilder("(");
		sb.append(prefix);
		if(arguments != null)
			for(int i = 0; i < arguments.length; i++)
				sb.append(lambda0.function(arguments[i]));
		sb.append(")");
		
		return sb.append(lambda0.function(returnType)).toString();
	}
	
	static int _forReturnInsn(Class<?> type)
	{
		return _forTypeDependingInsn(type, TYPE_DEPENDING_INSN_INDEX_RETURN);
	}
	
	static int _forLocalLoadInsn(Class<?> type)
	{
		return _forTypeDependingInsn(type, TYPE_DEPENDING_INSN_INDEX_LOCAL_LOAD);
	}
	
	static int _forReturnInsn(String desc)
	{
		return _forTypeDependingInsn(desc, TYPE_DEPENDING_INSN_INDEX_RETURN);
	}
	
	static int _forLocalLoadInsn(String desc)
	{
		return _forTypeDependingInsn(desc, TYPE_DEPENDING_INSN_INDEX_LOCAL_LOAD);
	}
	
	static int _forTypeDependingInsn(Class<?> type, int index)
	{
		if(type.isPrimitive())
			return _forTypeDependingInsn(Type.getDescriptor(type), index);
		return TYPE_DEPENDING_INSN_CONST[0][index];
	}
	
	static int _forTypeDependingInsn(String desc, int index)
	{
		switch(_toSymbol(desc))
		{
		case DESCRIPTOR_VOID:
			return TYPE_DEPENDING_INSN_CONST[1][index];
		case DESCRIPTOR_BOOLEAN:
		case DESCRIPTOR_CHAR:
		case DESCRIPTOR_SHORT:
		case DESCRIPTOR_BYTE:
		case DESCRIPTOR_INT:
			return TYPE_DEPENDING_INSN_CONST[2][index];
		case DESCRIPTOR_FLOAT:
			return TYPE_DEPENDING_INSN_CONST[3][index];
		case DESCRIPTOR_LONG:
			return TYPE_DEPENDING_INSN_CONST[4][index];
		case DESCRIPTOR_DOUBLE:
			return TYPE_DEPENDING_INSN_CONST[5][index];
		case DESCRIPTOR_SYMBOL_ARRAY:
		case DESCRIPTOR_SYMBOL_OBJECT:
			return TYPE_DEPENDING_INSN_CONST[0][index];
		default:
			throw new IllegalStateException();
		}
	}
	
	static String _nextDescriptor(String descriptor, int[] currentIndex)
	{
		char c;
		while(true)
			switch(c = descriptor.charAt(currentIndex[0]))
			{
			case '(':
			case ')':
				currentIndex[0]++;
				break;
			case 'L':
			case '[':
				int start = currentIndex[0];
				for(; currentIndex[0] < descriptor.length(); currentIndex[0]++)
					if(descriptor.charAt(currentIndex[0]) == ';')
						return descriptor.substring(start, ++currentIndex[0]);
				throw new IllegalArgumentException("Uncompleted descriptor: " + descriptor);
			default:
				currentIndex[0]++;
				return String.valueOf(c); 
			}
	}
	
	static String _getReturnDescriptor(String descriptor)
	{
		int index = descriptor.lastIndexOf(')');
		if(index < 0)
			throw new IllegalArgumentException("Illegal descriptor: " + descriptor);
		return descriptor.substring(index + 1, descriptor.length());
	}
	
	static String _toSymbol(String descriptor)
	{
		return String.valueOf(descriptor.charAt(0));
	}
	
	static String _boxingMapped(String key)
	{
		return MAPPED_BOXING_OBJECT.get(key);
	}
	
	static String _tryBoxingMapped(String key)
	{
		String result = _boxingMapped(key);
		if(result == null)
			return key;
		return result;
	}
	
	static String _generateLambdaFunctionName()
	{
		return new StringBuilder("lambda$").append(UUID.randomUUID().toString().replace("-", "_")).toString();
	}
	
	static String _namingJavaBean(String fieldName)
	{
		if(fieldName.length() == 0)
			throw new IllegalArgumentException("Name cannot be empty");
		
		char f = Character.toUpperCase(fieldName.charAt(0));
		return new StringBuilder()
				.append(f)
				.append(fieldName.length() > 1 ? fieldName.substring(1) : "")
				.toString();
	}
	
	static String _namingJavaBeanGetter(String namedJavaBean)
	{
		return new StringBuilder("get").append(namedJavaBean).toString();
	}
	
	static String _namingJavaBeanSetter(String namedJavaBean)
	{
		return new StringBuilder("set").append(namedJavaBean).toString();
	}
	
	static void _pushGetVoidObject(MethodVisitor mv)
	{
		mv.visitFieldInsn(GETSTATIC, "org/kucro3/tool/asm/ASMUtil", "RETURN_VOID", "Ljava/lang/Object;");
	}
	
	static String[] _toDescriptors(Class<?>[] classes)
	{
		String[] descriptors = new String[classes.length];
		for(int i = 0; i < classes.length; i++)
			descriptors[i] = Type.getDescriptor(classes[i]);
		return descriptors;
	}
	
	static Type[] _toTypes(String[] descriptors)
	{
		Type[] types = new Type[descriptors.length];
		for(int i = 0; i < descriptors.length; i++)
			types[i] = Type.getType(descriptors[i]);
		return types;
	}
	
	static MethodVisitor __init__(ClassWriter cw, int modifiers, String descriptor, String[] throwings)
	{
		return _newMethod(cw, modifiers, "<init>", descriptor, throwings);
	}
	
	static MethodVisitor __clinit__(ClassWriter cw)
	{
		return _newMethod(cw, ACC_PUBLIC + ACC_STATIC, "<clinit>", _toDescriptor(void.class, null, ""), null);
	}
	
	@SuppressWarnings("deprecation")
	public Class<?> defClass(byte[] b, int off, int len)
	{
		return super.defineClass(b, off, len);
	}
	
	public Class<?> defClass(String name, byte[] b, int off, int len)
	{
		return super.defineClass(name, b, off, len);
	}
	
	private static final Jam2Util INSTANCE = new Jam2Util();
	
	private static final int[][] TYPE_DEPENDING_INSN_CONST = {
			{ARETURN, ALOAD},
			{RETURN},
			{IRETURN, ILOAD},
			{FRETURN, FLOAD},
			{LRETURN, LLOAD},
			{DRETURN, DLOAD}
	};
	
	private static final int TYPE_DEPENDING_INSN_INDEX_RETURN = 0;
	
	private static final int TYPE_DEPENDING_INSN_INDEX_LOCAL_LOAD = 1;
	
	private static interface LAMBDA__TODESCRIPTOR_$0
	{
		public String function(Object arg0);
	}
	
	private static final LAMBDA__TODESCRIPTOR_$0 
			LAMBDA__TO_DESCRIPTOR_$0_0 = (obj) -> Type.getDescriptor((Class<?>)obj),
			LAMBDA__TO_DESCRIPTOR_$0_1 = (obj) -> (String)obj;
	
	private static interface LAMBDA_PUSHCALLER_$0
	{
		public String function(Object arg0);
	}
	
	private static final LAMBDA_PUSHCALLER_$0
			LAMBDA_PUSHCALLER_$0_0 = (obj) -> Type.getDescriptor((Class<?>)obj),
			LAMBDA_PUSHCALLER_$0_1 = (obj) -> (String)obj;
			
	private static interface LAMBDA_PUSHCALLER_$1
	{
		public Object function();
	}
	
	private static final LAMBDA_PUSHCALLER_$1
			LAMBDA_PUSHCALLER_$1_0 = () -> Object.class,
			LAMBDA_PUSHCALLER_$1_1 = () -> "Ljava/lang/Object;";
			
	private static interface LAMBDA_PUSHCALLER_$2
	{
		public String function(Object arg0);
	}
	
	private static final LAMBDA_PUSHCALLER_$2
			LAMBDA_PUSHCALLER_$2_0 = (obj) -> Type.getInternalName((Class<?>)obj),
			LAMBDA_PUSHCALLER_$2_1 = (obj) -> (String)obj;
			
	private static interface LAMBDA_PUSHBOXINGOPERATION_$0
	{
		public void function(MethodVisitor mv);
	}
	
	private static final LAMBDA_PUSHBOXINGOPERATION_$0[]
			LAMBDA_PUSHBOXINGOPERATION_$_0 = {
				null,
				Jam2Util::pushBoxingBoolean,
				Jam2Util::pushBoxingCharacter,
				Jam2Util::pushBoxingByte,
				Jam2Util::pushBoxingShort,
				Jam2Util::pushBoxingInteger,
				Jam2Util::pushBoxingFloat,
				Jam2Util::pushBoxingLong,
				Jam2Util::pushBoxingDouble
		},
			LAMBDA_PUSHBOXINGOPERATION_$_1 = {
				null,
				Jam2Util::pushUnboxingBoolean,
				Jam2Util::pushUnboxingCharacter,
				Jam2Util::pushUnboxingByte,
				Jam2Util::pushUnboxingShort,
				Jam2Util::pushUnboxingInteger,
				Jam2Util::pushUnboxingFloat,
				Jam2Util::pushUnboxingLong,
				Jam2Util::pushUnboxingDouble
		};
	
	public static final Object RETURN_VOID = new Object();
	
	public static final String DESCRIPTOR_VOID = "V";
			
	public static final String DESCRIPTOR_BOOLEAN = "Z";
	
	public static final String DESCRIPTOR_CHAR = "C";
	
	public static final String DESCRIPTOR_SHORT = "S";
	
	public static final String DESCRIPTOR_BYTE = "B";
	
	public static final String DESCRIPTOR_INT = "I";
	
	public static final String DESCRIPTOR_FLOAT = "F";
	
	public static final String DESCRIPTOR_LONG = "J";
	
	public static final String DESCRIPTOR_DOUBLE = "D";
	
	public static final String DESCRIPTOR_SYMBOL_OBJECT = "L";

	public static final String DESCRIPTOR_SYMBOL_ARRAY = "[";
	
	private static final MethodType METHOD_TYPE_LAMBDA_METAFACTORY = 
			MethodType.methodType(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class,
					MethodType.class, MethodHandle.class, MethodType.class);
	
	private static final String METHOD_DESCRIPTOR_LAMBDA_METAFACTORY = METHOD_TYPE_LAMBDA_METAFACTORY.toMethodDescriptorString();
	
	private static final Handle METHOD_HANDLE_LAMBDA_METAFACTORY = 
			new Handle(H_INVOKESTATIC, "java/lang/invoke/LambdaMetafactory", "metafactory", METHOD_DESCRIPTOR_LAMBDA_METAFACTORY);
	
	private static final Map<String, Integer> MAPPED_DESCRIPTOR_INDEX
			= new HashMap<String, Integer>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = -8847617886452962469L;

		{
			put(DESCRIPTOR_VOID, Type.VOID);
			put(DESCRIPTOR_BOOLEAN, Type.BOOLEAN);
			put(DESCRIPTOR_CHAR, Type.CHAR);
			put(DESCRIPTOR_SHORT, Type.SHORT);
			put(DESCRIPTOR_BYTE, Type.BYTE);
			put(DESCRIPTOR_INT, Type.INT);
			put(DESCRIPTOR_FLOAT, Type.FLOAT);
			put(DESCRIPTOR_LONG, Type.LONG);
			put(DESCRIPTOR_DOUBLE, Type.DOUBLE);
		}
	};
	
	private static final Map<String, String> MAPPED_BOXING_OBJECT
			= new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 2982736764808443622L;

		{
			put(DESCRIPTOR_BOOLEAN, "java/lang/Boolean");
			put(DESCRIPTOR_CHAR, "java/lang/Character");
			put(DESCRIPTOR_SHORT, "java/lang/Short");
			put(DESCRIPTOR_BYTE, "java/lang/Byte");
			put(DESCRIPTOR_INT, "java/lang/Integer");
			put(DESCRIPTOR_FLOAT, "java/lang/Float");
			put(DESCRIPTOR_LONG, "java/lang/Long");
			put(DESCRIPTOR_DOUBLE, "java/lang/Double");
		}
	};
	
	public static enum CallingType
	{
		VIRTUAL(INVOKEVIRTUAL, false),
		INTERFACE(INVOKEINTERFACE, true),
		STATIC(INVOKESTATIC, false);
		
		private CallingType(int insn, boolean flag)
		{
			this.insn = insn;
			this.flag = flag;
		}
		
		final int getMethodInsn()
		{
			return insn;
		}
		
		final boolean getFlag()
		{
			return flag;
		}
		
		private final boolean flag;
		
		private final int insn;
	}
	
	public static enum FieldType
	{
		STATIC(GETSTATIC, PUTSTATIC, true),
		NONSTATIC(GETFIELD, PUTFIELD, false);
		
		private FieldType(int get, int put, boolean flag)
		{
			this.get = get;
			this.put = put;
			this.flag = flag;
		}
		
		final int get()
		{
			return get;
		}
		
		final int put()
		{
			return put;
		}
		
		final boolean isStatic()
		{
			return flag;
		}
		
		public static FieldType fromModifier(int modifiers)
		{
			if(Modifier.isStatic(modifiers))
				return STATIC;
			return NONSTATIC;
		}
		
		private final boolean flag;
		
		private final int get;
		
		private final int put;
	}
}