package org.kucro3.jam2.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.kucro3.jam2.util.ClassContext;
import org.kucro3.jam2.util.FieldContext;
import org.kucro3.jam2.util.Jam2Util;
import org.kucro3.jam2.util.MethodContext;
import org.kucro3.jam2.util.Version;
import org.kucro3.jam2.util.context.ClassCompound;
import org.kucro3.jam2.util.context.ClassConstantCompound;
import org.kucro3.jam2.util.context.ClassRestrictedModifiableCompound;
import org.kucro3.jam2.util.context.ConstantClassContext;
import org.kucro3.jam2.util.context.ConstantFieldContext;
import org.kucro3.jam2.util.context.ConstantMethodContext;
import org.kucro3.jam2.util.context.FieldCompound;
import org.kucro3.jam2.util.context.FieldConstantCompound;
import org.kucro3.jam2.util.context.FieldRestrictedModifiableCompound;
import org.kucro3.jam2.util.context.FullyModifiableClassContext;
import org.kucro3.jam2.util.context.FullyModifiableFieldContext;
import org.kucro3.jam2.util.context.FullyModifiableMethodContext;
import org.kucro3.jam2.util.context.MethodCompound;
import org.kucro3.jam2.util.context.MethodConstantCompound;
import org.kucro3.jam2.util.context.MethodRestrictedModifiableCompound;
import org.kucro3.jam2.util.context.RestrictedModifiableClassContext;
import org.kucro3.jam2.util.context.RestrictedModifiableFieldContext;
import org.kucro3.jam2.util.context.RestrictedModifiableMethodContext;
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
	
	public static MethodContext newMethodConstant(Method method)
	{
		return _newMethodContext(method, ConstantMethodContext::new);
	}
	
	public static MethodContext newMethodConstant(Constructor<?> constructor)
	{
		return _newMethodContext(constructor, ConstantMethodContext::new);
	}
	
	public static MethodContext newMethodRestrictedModifiable(Method method)
	{
		return _newMethodContext(method, RestrictedModifiableMethodContext::new);
	}
	
	public static MethodContext newMethodRestrictedModifiable(Constructor<?> constructor)
	{
		return _newMethodContext(constructor, RestrictedModifiableMethodContext::new);
	}
	
	public static MethodContext newMethodFullyModifiable(Method method)
	{
		return _newMethodContext(method, FullyModifiableMethodContext::new);
	}
	
	public static MethodContext newMethodFullyModifiable(Constructor<?> constructor)
	{
		return _newMethodContext(constructor, FullyModifiableMethodContext::new);
	}
	
	static MethodContext _newMethodContext(Method method, NewMethodContextFunction _NewMethodContext)
	{
		return _NewMethodContext.newMethodContext(
				Type.getInternalName(method.getDeclaringClass()),
				method.getModifiers(),
				method.getName(),
				Type.getMethodDescriptor(method),
				null,
				Jam2Util.toInternalNames(method.getExceptionTypes()));
	}
	
	static MethodContext _newMethodContext(Constructor<?> constructor, NewMethodContextFunction _NewMethodContext)
	{
		return _NewMethodContext.newMethodContext(
				Type.getInternalName(constructor.getDeclaringClass()),
				constructor.getModifiers(),
				"<init>",
				Type.getConstructorDescriptor(constructor),
				null,
				Jam2Util.toInternalNames(constructor.getExceptionTypes()));
	}
	
	public static FieldContext newFieldConstant(Field field)
	{
		return _newFieldContext(field, ConstantFieldContext::new);
	}
	
	public static FieldContext newFieldRestrictedModifiable(Field field)
	{
		return _newFieldContext(field, RestrictedModifiableFieldContext::new);
	}
	
	public static FieldContext newFieldFullyModifiable(Field field)
	{
		return _newFieldContext(field, FullyModifiableFieldContext::new);
	}
	
	static FieldContext _newFieldContext(Field field, NewFieldContextFunction _NewFieldContext)
	{
		return _NewFieldContext.newFieldContext(
				Type.getInternalName(field.getDeclaringClass()),
				field.getModifiers(),
				field.getName(),
				Type.getDescriptor(field.getType()),
				null,
				null);
	}
	
	public static ClassContext newClassConstant(Class<?> clz)
	{
		return _newClassContext(clz, ConstantClassContext::new);
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
				enclosingMethodDescriptor);
		
		for(Field field : clz.getDeclaredFields())
			cc.newField(newFieldConstant(field));
		
		for(Method method : clz.getDeclaredMethods())
			cc.newMethod(newMethodConstant(method));
		
		for(Constructor<?> constructor : clz.getDeclaredConstructors())
			cc.newMethod(newMethodConstant(constructor));
		
		return cc;
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
	}
}