package org.kucro3.jam2.util;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;

import org.kucro3.jam2.simulator.MaxsComputer;
import org.objectweb.asm.ClassVisitor;
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
	
	public static String generateUUIDForClassName()
	{
		return UUID.randomUUID().toString().replace('-', '_');
	}
	
	public static void pushEmptyConstructor(ClassVisitor cw, int modifiers, Class<?> superClass)
	{
		pushEmptyConstructor(cw, modifiers, Type.getInternalName(superClass));
	}
	
	public static void pushEmptyConstructor(ClassVisitor cw, int modifiers, String superClass)
	{
		MethodVisitor mv = __init__(cw, modifiers, "()V", null);
		mv.visitCode();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, superClass,
				"<init>", "()V", false);
		mv.visitInsn(RETURN);
		mv.visitMaxs(1, 1);
		mv.visitEnd();
	}
	
	public static void pushNewInstance(ClassVisitor cw, int modifiers, String methodName,
			Class<?> type, Class<?>[] constructorArguments, Class<?>[] exceptions, boolean varargs, boolean objReturn)
	{
		pushNewInstance(cw, modifiers, methodName, Type.getInternalName(type),
				varargs ? null : _toDescriptors(constructorArguments), _toDescriptors(exceptions), varargs, objReturn);
	}
	
	public static void pushNewInstance(ClassVisitor cw, int modifiers, String methodName,
			MethodContext cCtx, boolean varags, boolean objReturn)
	{
		pushNewInstance(cw, modifiers, methodName,
				cCtx.getDeclaringType(), cCtx.getArguments(), cCtx.getExceptions(),
				varags, objReturn);
	}
	
	public static void pushNewInstance(ClassVisitor cw, int modifiers, String methodName,
			String type, String[] constructorArguments, String[] exceptions, boolean varargs, boolean objReturn)
	{
		if(varargs)
			modifiers |= ACC_VARARGS;
		String descriptor = _toConstructorDescriptor(constructorArguments);
		boolean selfStatic = Modifier.isStatic(modifiers);
		String[] arguments = varargs ? new String[] {"[Ljava/lang/Object;"}
				: constructorArguments;
		String selfDescriptor = 
				Jam2Util.toDescriptor("", objReturn ? "Ljava/lang/Object;" : Type.getObjectType(type).getDescriptor(), arguments);
		MethodVisitor _mv = cw.visitMethod(modifiers, methodName, selfDescriptor, null, exceptions);
		MaxsComputer mv = new MaxsComputer(_mv);
		
		mv.visitTypeInsn(NEW, type);
		mv.visitInsn(DUP);
		_pushCallerProcessCallingPreprocess(mv, constructorArguments.length, type, descriptor,
				varargs, true, selfStatic, -1);
		mv.visitMethodInsn(INVOKESPECIAL, type, "<init>", descriptor, false);
		mv.visitInsn(ARETURN);
		mv.visitMaxs(selfDescriptor, selfStatic);
		mv.visitEnd();
	}
	
	public static void pushFieldGetter(ClassVisitor cw, int modifiers, String name,
			FieldContext ctx, boolean objArg, boolean objReturn)
	{
		pushFieldGetter(cw, modifiers, name,
				Objects.requireNonNull(ctx.getDeclaringType(), "Declaring class needed"), ctx.getName(), ctx.getDescriptor(),
				FieldType.fromModifier(ctx.getModifier()), objArg, objReturn);
	}
	
	public static void pushFieldGetter(ClassVisitor cw, int modifiers, String name,
			Class<?> owner, String fieldName, Class<?> type, FieldType ft, boolean objArg, boolean objReturn)
	{
		pushFieldGetter(cw, modifiers, name, Type.getInternalName(owner), fieldName, Type.getDescriptor(type), ft, objArg, objReturn);
	}
	
	public static void pushFieldGetter(ClassVisitor cw, int modifiers, String name, 
			String ownerInternalName, String fieldName, String type, FieldType ft, boolean objArg, boolean objReturn)
	{
		boolean selfStatic = (modifiers & ACC_STATIC) != 0;
		int delta = selfStatic ? -1 : 0;
		String selfDescriptor =
				Jam2Util.toDescriptor("",
						objReturn ? "Ljava/lang/Object;" : type,
						new String[] {objArg ? "Ljava/lang/Object;" : Type.getObjectType(ownerInternalName).getDescriptor()});
		MethodVisitor _mv = cw.visitMethod(modifiers, name, selfDescriptor, null, null);
		MaxsComputer mv = new MaxsComputer(_mv);
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
		mv.visitMaxs(selfDescriptor, selfStatic);
		mv.visitEnd();
	}
	
	public static void pushFieldSetter(ClassVisitor cw, int modifiers, String name,
			FieldContext ctx, boolean objArgument)
	{
		pushFieldSetter(cw, modifiers, name,
				ctx.getDeclaringType(), ctx.getName(), ctx.getDescriptor(),
				FieldType.fromModifier(ctx.getModifier()), objArgument);
	}
	
	public static void pushFieldSetter(ClassVisitor cw, int modifiers, String name,
			Class<?> owner, String fieldName, Class<?> type, FieldType ft, boolean objArgument)
	{
		pushFieldSetter(cw, modifiers, name, Type.getInternalName(owner), fieldName, Type.getDescriptor(type), ft, objArgument);
	}
	
	public static void pushFieldSetter(ClassVisitor cw, int modifiers, String name,
			String ownerInternalName, String fieldName, String type, FieldType ft, boolean objArgument)
	{		
		boolean selfStatic = (modifiers & ACC_STATIC) != 0;
		int delta = selfStatic ? -1 : 0;
		String selfDescriptor = 
				Jam2Util.toDescriptor("",
						DESCRIPTOR_VOID, 
						new String[] {"Ljava/lang/Object;", objArgument ? "Ljava/lang/Object;" : type});
		MethodVisitor _mv = cw.visitMethod(modifiers, name, selfDescriptor, null, null);
		MaxsComputer mv = new MaxsComputer(_mv);
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
			else
				mv.visitTypeInsn(CHECKCAST, Type.getType(type).getInternalName());
		}
		mv.visitFieldInsn(ft.put(), ownerInternalName, fieldName, type);
		mv.visitInsn(RETURN);
		mv.visitMaxs(selfDescriptor, selfStatic);
		mv.visitEnd();
	}
	
	public static void pushGetter(ClassVisitor cw, int modifiers, String name, FieldContext ctx)
	{
		pushGetter(cw, modifiers, ctx.getDeclaringType(),
				name, ctx.getName(), ctx.getDescriptor(), FieldType.fromModifier(ctx.getModifier()));
	}
	
	public static void pushGetter(ClassVisitor cw, int modifiers, String ownerInternalName,
			String name, String fieldName, Class<?> type, FieldType ft)
	{
		pushGetter(cw, modifiers, ownerInternalName, name, fieldName, Type.getDescriptor(type), ft);
	}
	
	public static void pushGetter(ClassVisitor cw, int modifiers, String ownerInternalName, 
			String name, String fieldname, String type, FieldType ft)
	{
		MethodVisitor _mv = _newMethod(cw, modifiers, name, type, (String[])null, null);
		MaxsComputer mv = new MaxsComputer(_mv);
		mv.visitCode();
		if(!ft.isStatic())
			mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(ft.get(), ownerInternalName, fieldname, type);
		mv.visitInsn(_forReturnInsn(type));
		mv.visitMaxs("()" + type, ft.isStatic());
		mv.visitEnd();
	}
	
	public static void pushSetter(ClassVisitor cw, int modifiers, String name, FieldContext ctx)
	{
		pushSetter(cw, modifiers, ctx.getDeclaringType(),
				name, ctx.getName(), ctx.getDescriptor(), FieldType.fromModifier(ctx.getModifier()));
	}
	
	public static void pushSetter(ClassVisitor cw, int modifiers, String ownerInternalName,
			String name, String fieldName, Class<?> type, FieldType ft)
	{
		pushSetter(cw, modifiers, ownerInternalName, name, fieldName, Type.getDescriptor(type), ft);
	}
	
	public static void pushSetter(ClassVisitor cw, int modifiers, String ownerInternalName,
			String name, String fieldname, String type, FieldType ft)
	{
		MethodVisitor _mv = _newMethod(cw, modifiers, name, DESCRIPTOR_VOID, type, null);
		MaxsComputer mv = new MaxsComputer(_mv);
		mv.visitCode();
		if(!ft.isStatic())
			mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(_forLocalLoadInsn(type), 1);
		mv.visitFieldInsn(ft.put(), ownerInternalName, fieldname, type);
		mv.visitInsn(RETURN);
		mv.visitMaxs("(" + type + ")" + DESCRIPTOR_VOID, ft.isStatic());
		mv.visitEnd();
	}
	
	public static void pushJavaBean(ClassVisitor cw, FieldContext ctx)
	{
		pushJavaBean(cw, ctx.getDeclaringType(), ctx.getName(), ctx.getDescriptor());
	}
	
	public static void pushJavaBean(ClassVisitor cw, String ownerInternalName, String fieldName, Class<?> type)
	{
		pushJavaBean(cw, ownerInternalName, fieldName, Type.getDescriptor(type));
	}
	
	public static void pushJavaBean(ClassVisitor cw, String ownerInternalName, String fieldName, String type)
	{
		String namedJavaBean = _namingJavaBean(fieldName);
		newField(cw, ACC_PRIVATE, fieldName, type);
		pushGetter(cw, ACC_PUBLIC, ownerInternalName, _namingJavaBeanGetter(namedJavaBean), fieldName, type, FieldType.NONSTATIC);
		pushSetter(cw, ACC_PUBLIC, ownerInternalName, _namingJavaBeanSetter(namedJavaBean), fieldName, type, FieldType.NONSTATIC);
	}
	
	public static void pushCaller(ClassVisitor cw, int modifiers, String name,
			Class<?> callingClass, String methodName, Class<?> returnType, Class<?>[] arguments, CallingType ct,
			boolean vargs, boolean objReturn)
	{
		_pushCaller$(cw, modifiers, name, callingClass, methodName, returnType, arguments, ct,
				vargs, objReturn, 
				LAMBDA__TO_DESCRIPTOR_$0_0, LAMBDA_PUSHCALLER_$0_0, LAMBDA_PUSHCALLER_$1_0, LAMBDA_PUSHCALLER_$2_0);
	}
	
	public static void pushCaller(ClassVisitor cw, int modifiers, String name,
			String callingClass, String methodName, String returnType, String[] arguments, CallingType ct,
			boolean vargs, boolean objReturn)
	{
		_pushCaller$(cw, modifiers, name, callingClass, methodName, returnType, arguments, ct,
				vargs, objReturn,
				LAMBDA__TO_DESCRIPTOR_$0_1, LAMBDA_PUSHCALLER_$0_1, LAMBDA_PUSHCALLER_$1_1, LAMBDA_PUSHCALLER_$2_1);
	}
	
	public static void pushCaller(ClassVisitor cw, int modifiers, String name, MethodContext callingCtx, CallingType ct,
			boolean vargs, boolean objReturn)
	{
		pushCaller(cw, modifiers, name,
				callingCtx.getDeclaringType(), callingCtx.getName(), callingCtx.getReturnType(),
				callingCtx.getArguments(), ct, vargs, objReturn);
	}
	
	private static void _pushCaller$(ClassVisitor cw, int modifiers, String name,
			Object callingClass, String methodName, Object returnType, Object[] arguments, CallingType ct,
			boolean vargs, boolean objReturn, 
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
		
		MethodVisitor _mv = _newMethod(cw, modifiers, name, descriptor, null);
		MaxsComputer mv = new MaxsComputer(_mv);
		mv.visitCode();
		
		String returnTypeDescriptor =
				_pushCallerProcessCallingPreprocess(mv, arguments.length, lambda3.function(callingClass), 
						callingDescriptor, vargs, isStatic, callerStatic, 0);
		_pushCallerProcessCalling(mv, insn, lambda3.function(callingClass), methodName, callingDescriptor, ct.getFlag());
		_pushCallerProcessReturn(mv, returnTypeDescriptor, objReturn, isVoid);
		
		mv.visitMaxs(descriptor, Modifier.isStatic(modifiers));
		mv.visitEnd();
	}
	
	static String _pushCallerProcessCallingPreprocess(MethodVisitor mv, int argumentLength, String ownerInternalName, String callingDescriptor,
			boolean varargs, boolean isStatic, boolean selfStatic, int localOffset)
	{
		int localDelta = selfStatic ? -1 : 0;
		if(!isStatic)
		{
			mv.visitVarInsn(ALOAD, 1 + localDelta + localOffset);
			mv.visitTypeInsn(CHECKCAST, ownerInternalName);
		}
		if(varargs)
		{
			int[] i = new int[] {1};
			for(int j = 0; j < argumentLength; j++)
			{
				mv.visitVarInsn(ALOAD, 2 + localDelta + localOffset);
				mv.visitIntInsn(BIPUSH, j);
				mv.visitInsn(AALOAD);
				String nd = _nextDescriptor(callingDescriptor, i);
				mv.visitTypeInsn(CHECKCAST, Type.getType(_tryBoxingMapped(nd)).getInternalName());
				pushUnboxing(mv, nd);
			}
			return _nextDescriptor(callingDescriptor, i);
		}
		else
		{
			for(int j = 0; j < argumentLength; j++)
				mv.visitVarInsn(ALOAD, j + 2 + localDelta + localOffset);
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
	
	public static MethodVisitor pushFunctionalLambda(ClassVisitor cw, String internalName, MethodVisitor mv, LambdaContext ctx)
	{
		return pushFunctionalLambda(cw, internalName, mv, ctx, false, null);
	}
	
	public static MethodVisitor pushFunctionalLambda(ClassVisitor cw, String internalName, MethodVisitor mv, LambdaContext ctx, 
			boolean reserveMethodVisitor)
	{
		return pushFunctionalLambda(cw, internalName, mv, ctx, reserveMethodVisitor, null);
	}
	
	public static MethodVisitor pushFunctionalLambda(ClassVisitor cw, String internalName, MethodVisitor mv, LambdaContext ctx,
			String lambdaFunctionName)
	{
		return pushFunctionalLambda(cw, internalName, mv, ctx, false, lambdaFunctionName);
	}
	
	public static MethodVisitor pushFunctionalLambda(ClassVisitor cw, String internalName, MethodVisitor mv, LambdaContext ctx,
			boolean reserveMethodVisitor, String lambdaFunctionName)
	{
		assert Version.getClassVersion() > V1_6 : "Lamdba is not supported in VM versioned under 7";
		
		MethodVisitor rt;
		
		if((rt = ctx.mv) == null)
		{
			String syntheticInternalName;
			if((syntheticInternalName = ctx.internalLambdaName) == null)
				syntheticInternalName = ctx.internalLambdaName = _generateLambdaFunctionName(lambdaFunctionName);
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
	
	public static void pushFunctionPrivateReference(MethodVisitor mv, String methodName, LambdaContext ctx)
	{
		_pushFunctionReference$(mv, null, methodName, ctx, H_INVOKESPECIAL, true);
	}
	
	public static void pushFunctionStaticReference(MethodVisitor mv, String ownerInternalName, String methodName, LambdaContext ctx)
	{
		_pushFunctionReference$(mv, ownerInternalName, methodName, ctx, H_INVOKESTATIC, false);
	}
	
	public static void pushFunctionVirtualReference(MethodVisitor mv, String ownerInternalName, String methodName, LambdaContext ctx)
	{
		_pushFunctionReference$(mv, ownerInternalName, methodName, ctx, H_INVOKEVIRTUAL, false);
	}
	
	private static void _pushFunctionReference$(MethodVisitor mv, String ownerInternalName, String methodName, LambdaContext ctx, 
			int handleType, boolean ignoreHandle)
	{
		assert Version.getClassVersion() > V1_7 : "Lambda depending metafactory is not supported in VM versioned under 8";
		
		Type implType = Type.getMethodType(Type.getType(ctx.getReturnType()), _toTypes(ctx.getArgumentDescriptors()));
		mv.visitInvokeDynamicInsn(ctx.getFunctionName(),
				_toDescriptor(Type.getObjectType(ctx.getTemplate()).getDescriptor(), null, ""),
				METHOD_HANDLE_LAMBDA_METAFACTORY,
				implType,
				ignoreHandle ? null : new Handle(handleType, ownerInternalName, methodName, ctx.getDescriptor()),
				implType);
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
	
	public static MethodVisitor newFinalize(ClassVisitor cw)
	{
		return _newMethod(cw, ACC_PROTECTED, "finalize", void.class, null, null);
	}
	
	public static MethodVisitor newConstructor(ClassVisitor cw, int modifiers, Class<?>[] arguments,
			String... throwings)
	{
		return __init__(cw, modifiers, _toDescriptor(void.class, arguments), throwings);
	}
	
	public static MethodVisitor newConstructor(ClassVisitor cw, int modifiers, String[] arguments,
			String... throwings)
	{
		return __init__(cw, modifiers, _toDescriptor(DESCRIPTOR_VOID, arguments), throwings);
	}
	
	public static MethodVisitor newStaticBlock(ClassVisitor cw)
	{
		return __clinit__(cw);
	}
	
	public static MethodVisitor newMethod(ClassVisitor cw, int modifiers, String name, Class<?> returnType,
			Class<?>[] arguments, String... throwings)
	{
		return _newMethod(cw, modifiers, name, returnType, arguments, throwings);
	}
	
	public static MethodVisitor newMethod(ClassVisitor cw, int modifiers, String name, String returnType,
			String[] arguments, String... throwings)
	{
		return _newMethod(cw, modifiers, name, returnType, arguments, throwings);
	}
	
	public static MethodVisitor newMethod(ClassVisitor cw, MethodContext ctx)
	{
		return cw.visitMethod(ctx.getModifier(), ctx.getName(), ctx.getDescriptor(), ctx.getSignature(), ctx.getExceptions());
	}
	
	public static FieldVisitor newField(ClassVisitor cw, int modifiers, String name, Class<?> type)
	{
		return _newField(cw, modifiers, name, Type.getDescriptor(type));
	}
	
	public static FieldVisitor newField(ClassVisitor cw, int modifiers, String name, String type)
	{
		return _newField(cw, modifiers, name, type);
	}
	
	public static FieldVisitor newField(ClassVisitor cw, FieldContext ctx)
	{
		return cw.visitField(ctx.getModifier(), ctx.getName(), ctx.getDescriptor(), ctx.getName(), ctx.getValue());
	}
	
	static MethodVisitor _newMethod(ClassVisitor cw, int modifiers, String name, String descriptor,
			String[] throwing)
	{
		return cw.visitMethod(modifiers, name, descriptor, null, throwing);
	}
	
	static MethodVisitor _newMethod(ClassVisitor cw, int modifiers, String name, String returnType,
			String[] arguments, String[] throwing)
	{
		return cw.visitMethod(modifiers, name, _toDescriptor(returnType, arguments, ""), null, throwing);
	}
	
	static MethodVisitor _newMethod(ClassVisitor cw, int modifiers, String name, String returnType,
			String argument, String[] throwing)
	{
		return cw.visitMethod(modifiers, name, "(" + (argument == null ? "" : argument) + ")" + returnType, null, throwing);
	}
	
	static MethodVisitor _newMethod(ClassVisitor cw, int modifiers, String name, Class<?> returnType,
			Class<?>[] arguments, String[] throwing)
	{
		return _newMethod(cw, modifiers, name, _toDescriptor(returnType, arguments, ""), throwing);
	}
	
	static FieldVisitor _newField(ClassVisitor cw, int modifiers, String name, String type)
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
		while(currentIndex[0] < descriptor.length())
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
					switch(c = descriptor.charAt(currentIndex[0]))
					{
					case '[':
						continue;
					case 'L':
						for(; currentIndex[0] < descriptor.length(); currentIndex[0]++)
							if(descriptor.charAt(currentIndex[0]) == ';')
								return descriptor.substring(start, ++currentIndex[0]);
					default:
						return descriptor.substring(start, ++currentIndex[0]);
					}
				throw new IllegalArgumentException("Uncompleted descriptor: " + descriptor);
			default:
				currentIndex[0]++;
				return String.valueOf(c); 
			}
		return null;
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
	
	static String _generateLambdaFunctionName(String arg)
	{
		if(arg != null)
			return new StringBuilder("lambda$").append(arg).toString();
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
		mv.visitFieldInsn(GETSTATIC, "org/kucro3/jam2/util/Jam2Util", "RETURN_VOID", "Ljava/lang/Object;");
	}
	
	static String[] _toDescriptors(Class<?>[] classes)
	{
		if(classes == null)
			return new String[0];
		String[] descriptors = new String[classes.length];
		for(int i = 0; i < classes.length; i++)
			descriptors[i] = Type.getDescriptor(classes[i]);
		return descriptors;
	}
	
	public static String[] toDescriptors(Class<?>[] classes)
	{
		return _toDescriptors(classes);
	}

	public static String toDescriptor(String returnType, String[] arguments)
	{
		return toDescriptor(null, returnType, arguments);
	}

	public static String toDescriptor(String name, String returnType, String[] arguments)
	{
		return name == null ?
				_toDescriptor(returnType, arguments) :
				(name + _toDescriptor(returnType, arguments));
	}

	public static String toDescriptor(Class<?> returnType, Class<?>[] arguments)
	{
		return toDescriptor(null, returnType, arguments);
	}
	
	public static String toDescriptor(String name, Class<?> returnType, Class<?>[] arguments)
	{
		return name == null ?
				_toDescriptor(returnType, arguments) :
				(name + _toDescriptor(returnType, arguments));
	}

	public static String toDescriptor(MethodContext context)
	{
		return context.getName() + context.getDescriptor();
	}
	
	public static String toDescriptor(Method mthd)
	{
		return toDescriptor(mthd.getName(), mthd.getReturnType(), mthd.getParameterTypes());
	}
	
	public static String toConstructorDescriptor(Constructor<?> constructor)
	{
		return toConstructorDescriptor(constructor.getParameterTypes());
	}
	
	public static String toConstructorDescriptor(Class<?>[] arguments)
	{
		return toDescriptor("", void.class, arguments);
	}
	
	public static String toConstructorFullDescriptor(Class<?>[] arguments)
	{
		return toDescriptor("<init>", void.class, arguments);
	}
	
	static String[] _toInternalNames(Class<?>[] classes)
	{
		if(classes == null)
			return new String[0];
		String[] names = new String[classes.length];
		for(int i = 0; i < classes.length; i++)
			names[i] = Type.getInternalName(classes[i]);
		return names;
	}
	
	public static String[] toInternalNames(Class<?>[] classes)
	{
		return _toInternalNames(classes);
	}

	static String[] _toCanonicalNames(Class<?>[] classes)
	{
		if(classes == null)
			return new String[0];
		String[] names = new String[classes.length];
		for(int i = 0; i < classes.length; i++)
			names[i] = classes[i].getCanonicalName();
		return names;
	}

	public static String[] toCanonicalNames(Class<?>[] classes)
	{
		return _toCanonicalNames(classes);
	}
	
	public static String toInternalName(Class<?> clazz)
	{
		return Type.getInternalName(clazz);
	}
	
	public static boolean isClassResource(String resource)
	{
		return resource.endsWith(".class");
	}
	
	public static String fromResourceToInternalName(String resource)
	{
		return resource.substring(0, resource.length() - 6);
	}

	public static String[] fromResourcesToInternalNames(String... resources)
	{
		return __stringProcess(resources, Jam2Util::fromResourceToInternalName);
	}
	
	public static String fromInternalNameToResource(String internalName)
	{
		return internalName + ".class";
	}

	public static String[] fromInternalNamesToResources(String... internalNames)
	{
		return __stringProcess(internalNames, Jam2Util::fromInternalNameToResource);
	}

	public static String fromInternalNameToDescriptor(String internalName)
	{
		return Type.getObjectType(internalName).getDescriptor();
	}

	public static String[] fromInternalNamesToDescriptors(String... internalNames)
	{
		return __stringProcess(internalNames, Jam2Util::fromInternalNameToDescriptor);
	}
	
	public static String fromDescriptorToInternalName(String desc)
	{
		return Type.getType(desc).getInternalName();
	}

	public static String[] fromDescriptorsToInternalNames(String... descriptors)
	{
		return __stringProcess(descriptors, Jam2Util::fromDescriptorToInternalName);
	}

	public static String fromDescriptorToCanonical(String desc)
	{
		return fromInternalNameToCanonical(fromDescriptorToInternalName(desc));
	}

	public static String[] fromDescriptorsToCanonicals(String... descriptors)
	{
		return __stringProcess(descriptors, Jam2Util::fromDescriptorToCanonical);
	}
	
	public static String fromCanonicalToInternalName(String name)
	{
		return name.replace('.', '/');
	}

	public static String[] fromCanonicalsToInternalNames(String... names)
	{
		return __stringProcess(names, Jam2Util::fromCanonicalToInternalName);
	}

	static String[] __stringProcess(String[] arr, Function<String, String> processor)
	{
		String[] ret = new String[arr.length];
		for(int i = 0; i < ret.length; i++)
			ret[i] = processor.apply(arr[i]);
		return ret;
	}
	
	public static String fromInternalNameToCanonical(String name)
	{
		return name.replace('/', '.');
	}

	public static Optional<Class<?>[]> tryFromCanoncialsToClasses(String... names)
	{
		return __tryToClasses(names, null);
	}

	public static Optional<Class<?>> tryFromCanoncialToClass(String name)
	{
		Optional<Class<?>[]> optional = tryFromCanoncialsToClasses(name);
		return optional.isPresent() ? Optional.of(optional.get()[0]) : Optional.empty();
	}

	public static Optional<Class<?>[]> tryFromInternalNamesToClasses(String... names)
	{
		return __tryToClasses(names, (internalName) -> Jam2Util.fromInternalNameToCanonical(internalName));
	}

	public static Optional<Class<?>> tryFromInternalNameToClass(String name)
	{
		Optional<Class<?>[]> optional = tryFromInternalNamesToClasses(name);
		return optional.isPresent() ? Optional.of(optional.get()[0]) : Optional.empty();
	}

	public static Optional<Class<?>[]> tryFromDescriptorsToClasses(String... names)
	{
		return __tryToClasses(names, (descriptor) -> Jam2Util.fromDescriptorToCanonical(descriptor));
	}

	public static Optional<Class<?>> tryFromDescriptorToClass(String name)
	{
		Optional<Class<?>[]> optional = tryFromDescriptorsToClasses(name);
		return optional.isPresent() ? Optional.of(optional.get()[0]) : Optional.empty();
	}

	private static Optional<Class<?>[]> __tryToClasses(String[] names, Function<String, String> func)
	{
		Class<?>[] classes = new Class<?>[names.length];

		try {
			for(int i = 0; i < classes.length; i++)
				classes[i] = Class.forName(func == null ? names[i] : func.apply(names[i]));
		} catch (ClassNotFoundException e) {
			return Optional.empty();
		}

		return Optional.of(classes);
	}

	static Type[] _toTypes(String[] descriptors)
	{
		Type[] types = new Type[descriptors.length];
		for(int i = 0; i < descriptors.length; i++)
			types[i] = Type.getType(descriptors[i]);
		return types;
	}
	
	static MethodVisitor __init__(ClassVisitor cw, int modifiers, String descriptor, String[] throwings)
	{
		return _newMethod(cw, modifiers, "<init>", descriptor, throwings);
	}
	
	static MethodVisitor __clinit__(ClassVisitor cw)
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

	public Class<?> defClass(ClassLoader loader, byte[] b, int off, int len)
	{
		if(METHOD_CLASSLOADER_DEFINECLASS == null)
			throw new UnsupportedOperationException();

		try {
			return (Class<?>) METHOD_CLASSLOADER_DEFINECLASS.invoke(loader, b, off, len);
		} catch (Exception e) {
			throw new UnsupportedOperationException();
		}
	}

	public Class<?> defClass(ClassLoader loader, String name, byte[] b, int off, int len)
	{
		if(METHOD_CLASSLOADER_DEFINECLASS_WITH_NAME == null)
			throw new UnsupportedOperationException();

		try {
			return (Class) METHOD_CLASSLOADER_DEFINECLASS_WITH_NAME.invoke(loader, name, b, off, len);
		} catch (Exception e) {
			throw new UnsupportedOperationException();
		}
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

	private static final Method METHOD_CLASSLOADER_DEFINECLASS;

	private static final Method METHOD_CLASSLOADER_DEFINECLASS_WITH_NAME;

	static {
		// must not use org.kucro3.jam2.invoker here
		// it will cause a dead loop

		Method mthd = null;

		try {
			// defineClass(byte[], int, int)
			mthd = ClassLoader.class.getMethod("defineClass", byte[].class, int.class, int.class);
			mthd.setAccessible(true);
		} catch (Exception e) {
		} finally {
			METHOD_CLASSLOADER_DEFINECLASS = mthd;
		}

		try {
			// defineClass(String, byte[], int, int)
			mthd = ClassLoader.class.getMethod("defineClass", String.class, byte[].class, int.class, int.class);
			mthd.setAccessible(true);
		} catch (Exception e) {
		} finally {
			METHOD_CLASSLOADER_DEFINECLASS_WITH_NAME = mthd;
		}
	}

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
		STATIC(INVOKESTATIC, false),
		SPECIAL(INVOKESPECIAL, false);
		
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
		
		public static CallingType fromModifier(int modifiers)
		{
			if(Modifier.isStatic(modifiers))
				return STATIC;
			if(Modifier.isPrivate(modifiers))
				return SPECIAL;
			return VIRTUAL;
		}
		
		public static CallingType fromMethod(ClassContext ctx, MethodContext mctx)
		{
			if(Modifier.isInterface(ctx.getModifier()))
				return INTERFACE;
			return fromModifier(mctx.getModifier());
		}
		
		public static CallingType fromMethod(Method method)
		{
			if(method.getDeclaringClass().isInterface())
				return INTERFACE;
			return fromModifier(method.getModifiers());
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