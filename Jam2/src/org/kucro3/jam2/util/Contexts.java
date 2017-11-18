package org.kucro3.jam2.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.kucro3.jam2.util.context.*;
import org.kucro3.jam2.util.context.hook.HookFunction;
import org.kucro3.jam2.util.context.hook.HookedClassCompound;
import org.kucro3.jam2.util.context.hook.HookedFieldCompound;
import org.kucro3.jam2.util.context.hook.HookedMethodCompound;
import org.kucro3.jam2.util.context.hook.HookedVisitableClassCompound;
import org.kucro3.jam2.util.context.visitable.VisitableClassContext;
import org.kucro3.jam2.util.context.visitable.VisitableClassContextCompound;
import org.objectweb.asm.Type;

public class Contexts {
	private Contexts()
	{
	}
	
	public static ClassContext.Compound newCompound(ClassContext ctx)
	{
		return ClassCompound.newCompound(ctx);
	}
	
	public static FieldContext.Compound newCompound(FieldContext ctx)
	{
		return FieldCompound.newCompound(ctx);
	}
	
	public static MethodContext.Compound newCompound(MethodContext ctx)
	{
		return MethodCompound.newCompound(ctx);
	}
	
	public static ClassContext toConstant(ClassContext ctx)
	{
		return new ClassConstantCompound(ctx);
	}
	
	public static ClassContext.RestrictedModifiable toRestrictedModifiable(ClassContext.FullyModifiable ctx)
	{
		return new ClassRestrictedModifiableCompound(ctx);
	}
	
	public static FieldContext toConstant(FieldContext ctx)
	{
		return new FieldConstantCompound(ctx);
	}
	
	public static FieldContext.RestrictedModifiable toRestrictedModifiable(FieldContext.FullyModifiable ctx)
	{
		return new FieldRestrictedModifiableCompound(ctx);
	}
	
	public static MethodContext toConstant(MethodContext ctx)
	{
		return new MethodConstantCompound(ctx);
	}
	
	public static MethodContext.RestrictedModifiable toRestrictedModifiable(MethodContext.FullyModifiable ctx)
	{
		return new MethodRestrictedModifiableCompound(ctx);
	}
	
	public static VisitableClassContext toVisitable(ClassContext ctx)
	{
		return VisitableClassContextCompound.newCompound(ctx);
	}
	
	public static MethodContext.Reflectable newMethodConstant(Method method)
	{
		return (MethodContext.Reflectable) _newMethodContext(method, (NewReflectableMethodContextFunction) ReflectableConstantMethodContext::new);
	}
	
	public static MethodContext.ReflectableConstructor newMethodConstant(Constructor<?> constructor)
	{
		return (MethodContext.ReflectableConstructor) _newMethodContext(constructor, (NewReflectableConstructorContextFunction) ReflectableConstantConstructorContext::new);
	}

	public static MethodContext newMethodConstant(String declaringClass,
												  int modifier,
												  String name,
												  String descriptor)
	{
		return newMethodConstant(declaringClass, modifier, name, descriptor, null);
	}

	public static MethodContext newMethodConstant(String declaringClass,
												  int modifier,
												  String name,
												  String descriptor,
												  String signature)
	{
		return newMethodConstant(declaringClass, modifier, name, descriptor, signature, null);
	}

	public static MethodContext newMethodConstant(String declaringClass,
												  int modifier,
												  String name,
												  String descriptor,
												  String signature,
												  String[] exceptions)
	{
		return new ConstantMethodContext(declaringClass, modifier, name, descriptor, signature, exceptions);
	}
	
	public static MethodContext.RestrictedModifiable newMethodRestrictedModifiable(Method method)
	{
		return (MethodContext.RestrictedModifiable) _newMethodContext(method, RestrictedModifiableMethodContext::new);
	}
	
	public static MethodContext.RestrictedModifiable newMethodRestrictedModifiable(Constructor<?> constructor)
	{
		return (MethodContext.RestrictedModifiable) _newMethodContext(constructor, RestrictedModifiableMethodContext::new);
	}

	public static MethodContext.RestrictedModifiable newMethodRestrictedModifiable(String declaringClass,
												  int modifier,
												  String name,
												  String descriptor)
	{
		return newMethodRestrictedModifiable(declaringClass, modifier, name, descriptor, null);
	}

	public static MethodContext.RestrictedModifiable newMethodRestrictedModifiable(String declaringClass,
												  int modifier,
												  String name,
												  String descriptor,
												  String signature)
	{
		return newMethodRestrictedModifiable(declaringClass, modifier, name, descriptor, signature, null);
	}

	public static MethodContext.RestrictedModifiable newMethodRestrictedModifiable(String declaringClass,
												  int modifier,
												  String name,
												  String descriptor,
												  String signature,
												  String[] exceptions)
	{
		return new RestrictedModifiableMethodContext(declaringClass, modifier, name, descriptor, signature, exceptions);
	}
	
	public static MethodContext.FullyModifiable newMethodFullyModifiable(Method method)
	{
		return (MethodContext.FullyModifiable) _newMethodContext(method, FullyModifiableMethodContext::new);
	}
	
	public static MethodContext.FullyModifiable newMethodFullyModifiable(Constructor<?> constructor)
	{
		return (MethodContext.FullyModifiable) _newMethodContext(constructor, FullyModifiableMethodContext::new);
	}

	public static MethodContext.FullyModifiable newMethodFullyModifiable(String declaringClass,
												  int modifier,
												  String name,
												  String descriptor)
	{
		return newMethodFullyModifiable(declaringClass, modifier, name, descriptor, null);
	}

	public static MethodContext.FullyModifiable newMethodFullyModifiable(String declaringClass,
												  int modifier,
												  String name,
												  String descriptor,
												  String signature)
	{
		return newMethodFullyModifiable(declaringClass, modifier, name, descriptor, signature, null);
	}

	public static MethodContext.FullyModifiable newMethodFullyModifiable(String declaringClass,
												  int modifier,
												  String name,
												  String descriptor,
												  String signature,
												  String[] exceptions)
	{
		return new FullyModifiableMethodContext(declaringClass, modifier, name, descriptor, signature, exceptions);
	}
	
	static MethodContext _newMethodContext(Method method, NewMethodContextFunction _NewMethodContext)
	{
		return _NewMethodContext.newMethodContext(
				Type.getInternalName(method.getDeclaringClass()),
				method.getModifiers(),
				method.getName(),
				Type.getMethodDescriptor(method),
				null,
				Jam2Util.toInternalNames(method.getExceptionTypes()),
				method);
	}
	
	static MethodContext _newMethodContext(Constructor<?> constructor, NewConstructorContextFunction _NewMethodContext)
	{
		return _NewMethodContext.newMethodContext(
				Type.getInternalName(constructor.getDeclaringClass()),
				constructor.getModifiers(),
				"<init>",
				Type.getConstructorDescriptor(constructor),
				null,
				Jam2Util.toInternalNames(constructor.getExceptionTypes()),
				constructor);
	}
	
	public static FieldContext.Reflectable newFieldConstant(Field field)
	{
		return (FieldContext.Reflectable) _newFieldContext(field, (NewReflectableFieldContextFunction) ReflectableConstantFieldContext::new);
	}

	public static FieldContext newFieldConstant(String declaringClass,
												int modifier,
												String name,
												String descriptor)
	{
		return newFieldConstant(declaringClass, modifier, name, descriptor, null);
	}

	public static FieldContext newFieldConstant(String declaringClass,
												int modifier,
												String name,
												String descriptor,
												String signature)
	{
		return newFieldConstant(declaringClass, modifier, name, descriptor, signature, null);
	}

	public static FieldContext newFieldConstant(String declaringClass,
												int modifier,
												String name,
												String descriptor,
												String signature,
												Object value)
	{
		return new ConstantFieldContext(declaringClass, modifier, name, descriptor, signature, value);
	}
	
	public static FieldContext.RestrictedModifiable newFieldRestrictedModifiable(Field field)
	{
		return (FieldContext.RestrictedModifiable) _newFieldContext(field, RestrictedModifiableFieldContext::new);
	}

	public static FieldContext.RestrictedModifiable newFieldRestrictedModifiable(String declaringClass,
												int modifier,
												String name,
												String descriptor)
	{
		return newFieldRestrictedModifiable(declaringClass, modifier, name, descriptor, null);
	}

	public static FieldContext.RestrictedModifiable newFieldRestrictedModifiable(String declaringClass,
												int modifier,
												String name,
												String descriptor,
												String signature)
	{
		return newFieldRestrictedModifiable(declaringClass, modifier, name, descriptor, signature, null);
	}

	public static FieldContext.RestrictedModifiable newFieldRestrictedModifiable(String declaringClass,
												int modifier,
												String name,
												String descriptor,
												String signature,
												Object value)
	{
		return new RestrictedModifiableFieldContext(declaringClass, modifier, name, descriptor, signature, value);
	}
	
	public static FieldContext.FullyModifiable newFieldFullyModifiable(Field field)
	{
		return (FieldContext.FullyModifiable) _newFieldContext(field, FullyModifiableFieldContext::new);
	}

	public static FieldContext.FullyModifiable newFieldFullyModifiable(String declaringClass,
												int modifier,
												String name,
												String descriptor)
	{
		return newFieldFullyModifiable(declaringClass, modifier, name, descriptor, null);
	}

	public static FieldContext.FullyModifiable newFieldFullyModifiable(String declaringClass,
												int modifier,
												String name,
												String descriptor,
												String signature)
	{
		return newFieldFullyModifiable(declaringClass, modifier, name, descriptor, signature, null);
	}

	public static FieldContext.FullyModifiable newFieldFullyModifiable(String declaringClass,
												int modifier,
												String name,
												String descriptor,
												String signature,
												Object value)
	{
		return new FullyModifiableFieldContext(declaringClass, modifier, name, descriptor, signature, value);
	}

	static FieldContext _newFieldContext(Field field, NewFieldContextFunction _NewFieldContext)
	{
		return _NewFieldContext.newFieldContext(
				Type.getInternalName(field.getDeclaringClass()),
				field.getModifiers(),
				field.getName(),
				Type.getDescriptor(field.getType()),
				null,
				null,
				field);
	}
	
	public static ClassContext.Reflectable newClassConstant(Class<?> clz)
	{
		return (ClassContext.Reflectable) _newClassContext(clz, (NewReflectableClassContextFunction) ReflectableConstantClassContext::new);
	}
	
	public static ClassContext newClassRestrictedModifiable(Class<?> clz)
	{
		return _newClassContext(clz, (ver, acc, name, sig, spr, ifs, ecs, ecmn, ecmd) ->
			_uncheckedRestrictedSet(new RestrictedModifiableClassContext(name),
				ver, acc, sig, spr, ifs, ecs, ecmn, ecmd)
		);
	}
	
	public static ClassContext newClassFullyModifiable(Class<?> clz)
	{
		return _newClassContext(clz, (ver, acc, name, sig, spr, ifs, ecs, ecmn, ecmd) ->
			_uncheckedRestrictedSet(new FullyModifiableClassContext(name),
				ver, acc, sig, spr, ifs, ecs, ecmn, ecmd)
		);
	}
	
	static ClassContext _uncheckedRestrictedSet(ClassContext ctx,
			int ver, int acc, String sig, String spr, String[] ifs, String ecs, String ecmn, String ecmd)
	{
		ctx.setVersion(ver);
		ctx.setModifier(acc);
		ctx.setSignature(sig);
		ctx.setSuperClass(spr);
		ctx.setInterfaces(ifs);
		ctx.setEnclosingClass(ecs);
		ctx.setEnclosingMethodName(ecmn);
		ctx.setEnclosingMethodDescriptor(ecmd);
		return ctx;
	}
	
	static ClassContext _newClassContext(Class<?> clz, NewClassContextFunction _NewClassContext)
	{
		Class<?> enclosingClass = clz.getEnclosingClass();
		Method enclosingMethod = clz.getEnclosingMethod();
		Constructor<?> enclosingConstructor = clz.getEnclosingConstructor();
		
		String /// -> 
			enclosingClassName = null,
			enclosingMethodName = null,
			enclosingMethodDescriptor = null
		;
		
		if(enclosingClass != null)
		{
			enclosingClassName = Type.getInternalName(enclosingClass);
			if(enclosingMethod != null)
			{
				enclosingMethodName = enclosingMethod.getName();
				enclosingMethodDescriptor = Type.getMethodDescriptor(enclosingMethod);
			}
			else if(enclosingConstructor != null)
			{
				enclosingMethodName = "<init>";
				enclosingMethodDescriptor = Type.getConstructorDescriptor(enclosingConstructor);
			}
		}
		
		ClassContext cc = _NewClassContext.newClassContext(
				Version.getClassVersion(),
				clz.getModifiers(),
				Type.getInternalName(clz),
				null,
				clz.getSuperclass() == null ? null : Type.getInternalName(clz.getSuperclass()),
				Jam2Util.toInternalNames(clz.getInterfaces()),
				enclosingClassName,
				enclosingMethodName,
				enclosingMethodDescriptor,
				clz);
		
		for(Field field : clz.getDeclaredFields())
			cc.newField(newFieldConstant(field));
		
		for(Method method : clz.getDeclaredMethods())
			cc.newMethod(newMethodConstant(method));
		
		for(Constructor<?> constructor : clz.getDeclaredConstructors())
			cc.newMethod(newMethodConstant(constructor));
		
		return cc;
	}
	
	public static HookedClassCompound hookDirectly(ClassContext ctx, HookFunction... funcs)
	{
		return HookedClassCompound.newCompound(ctx, funcs);
	}
	
	public static HookedFieldCompound hookDirectly(FieldContext ctx, HookFunction... funcs)
	{
		return HookedFieldCompound.newCompound(ctx, funcs);
	}
	
	public static HookedMethodCompound hookDirectly(MethodContext ctx, HookFunction... funcs)
	{
		return HookedMethodCompound.newCompound(ctx, funcs);
	}
	
	public static HookedClassCompound hook(ClassContext ctx, HookFunction... funcs)
	{
		HookedClassCompound ret;
		if(ctx instanceof HookedClassCompound)
			(ret = (HookedClassCompound) ctx).hook(funcs);
		else
			ret = hookDirectly(ctx, funcs);
		return ret;
	}
	
	public static HookedFieldCompound hook(FieldContext ctx, HookFunction... funcs)
	{
		HookedFieldCompound ret;
		if(ctx instanceof HookedFieldCompound)
			(ret = (HookedFieldCompound) ctx).hook(funcs);
		else
			ret = hookDirectly(ctx, funcs);
		return ret;
	}
	
	public static HookedMethodCompound hook(MethodContext ctx, HookFunction... funcs)
	{
		HookedMethodCompound ret;
		if(ctx instanceof HookedMethodCompound)
			(ret = (HookedMethodCompound) ctx).hook(funcs);
		else
			ret = hookDirectly(ctx, funcs);
		return ret;
	}
	
	public static HookedVisitableClassCompound hookDirectly(VisitableClassContext ctx, HookFunction... funcs)
	{
		return HookedVisitableClassCompound.newCompound(ctx, funcs);
	}
	
	public static HookedVisitableClassCompound hook(VisitableClassContext ctx, HookFunction... funcs)
	{
		HookedVisitableClassCompound ret;
		if(ctx instanceof HookedVisitableClassCompound)
			(ret = (HookedVisitableClassCompound) ctx).hook(funcs);
		else
			ret = hookDirectly(ctx, funcs);
		return ret;
	}
	
	private interface NewClassContextFunction
	{
		ClassContext newClassContext(
				int version,
				int modifier,
				String name, // internal
				String signature,
				String superClass,
				String[] interfaces,
				String enclosingClassName,
				String enclosingMethodName,
				String enclosingMethodDescriptor
		);

		default ClassContext newClassContext(
				int version,
				int modifier,
				String name, // internal
				String signature,
				String superClass,
				String[] interfaces,
				String enclosingClassName,
				String enclosingMethodName,
				String enclosingMethodDescriptor,
				Class<?> clz)
		{
			return newClassContext(version, modifier, name, signature, superClass, interfaces, enclosingClassName, enclosingMethodName, enclosingMethodDescriptor);
		}
	}

	private interface NewReflectableClassContextFunction extends NewClassContextFunction
	{
		default ClassContext newClassContext(
				int version,
				int modifier,
				String name, // internal
				String signature,
				String superClass,
				String[] interfaces,
				String enclosingClassName,
				String enclosingMethodName,
				String enclosingMethodDescriptor)
		{
			throw new UnsupportedOperationException();
		}

		ClassContext newClassContext(
				int version,
				int modifier,
				String name, // internal
				String signature,
				String superClass,
				String[] interfaces,
				String enclosingClassName,
				String enclosingMethodName,
				String enclosingMethodDescriptor,
				Class<?> clz
			);
	}
	
	private interface NewFieldContextFunction
	{
		FieldContext newFieldContext(
				String declaringClass,
				int modifier,
				String name,
				String descriptor,
				String signature,
				Object value
		);

		default FieldContext newFieldContext(
				String declaringClass,
				int modifier,
				String name,
				String descriptor,
				String signature,
				Object value,
				Field field)
		{
			return newFieldContext(declaringClass, modifier, name, descriptor, signature, value);
		}
	}

	private interface NewReflectableFieldContextFunction extends NewFieldContextFunction
	{
		default FieldContext newFieldContext(
				String declaringClass,
				int modifier,
				String name,
				String descriptor,
				String signature,
				Object value)
		{
			throw new UnsupportedOperationException();
		}

		FieldContext newFieldContext(
				String declaringClass,
				int modifier,
				String name,
				String descriptor,
				String signature,
				Object value,
				Field field
			);
	}

	private interface NewMethodContextFunction
	{
		MethodContext newMethodContext(
				String declaingClass,
				int modifier,
				String name,
				String descriptor,
				String signature,
				String[] exceptions
		);

		default MethodContext newMethodContext(
				String declaingClass,
				int modifier,
				String name,
				String descriptor,
				String signature,
				String[] exceptions,
				Method method)
		{
			return newMethodContext(declaingClass, modifier, name, descriptor, signature, exceptions);
		}
	}

	private interface NewReflectableMethodContextFunction extends NewMethodContextFunction
	{
		default MethodContext newMethodContext(
				String declaingClass,
				int modifier,
				String name,
				String descriptor,
				String signature,
				String[] exceptions)
		{
			throw new UnsupportedOperationException();
		}

		MethodContext newMethodContext(
				String declaingClass,
				int modifier,
				String name,
				String descriptor,
				String signature,
				String[] exceptions,
				Method method
			);
	}

	private interface NewConstructorContextFunction
	{
		MethodContext newMethodContext(
				String declaingClass,
				int modifier,
				String name,
				String descriptor,
				String signature,
				String[] exceptions
		);

		default MethodContext newMethodContext(
				String declaingClass,
				int modifier,
				String name,
				String descriptor,
				String signature,
				String[] exceptions,
				Constructor<?> constructor)
		{
			return newMethodContext(declaingClass, modifier, name, descriptor, signature, exceptions);
		}
	}

	private interface NewReflectableConstructorContextFunction extends NewConstructorContextFunction
	{
		default MethodContext newMethodContext(
				String declaingClass,
				int modifier,
				String name,
				String descriptor,
				String signature,
				String[] exceptions)
		{
			throw new UnsupportedOperationException();
		}

		MethodContext newMethodContext(
				String declaingClass,
				int modifier,
				String name,
				String descriptor,
				String signature,
				String[] exceptions,
				Constructor<?> constructor
			);
	}
}