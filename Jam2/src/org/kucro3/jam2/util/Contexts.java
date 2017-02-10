package org.kucro3.jam2.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.kucro3.jam2.util.ClassContext;
import org.kucro3.jam2.util.FieldContext;
import org.kucro3.jam2.util.Jam2Util;
import org.kucro3.jam2.util.MethodContext;
import org.kucro3.jam2.util.Version;
import org.kucro3.jam2.util.context.ConstantClassContext;
import org.kucro3.jam2.util.context.ConstantFieldContext;
import org.kucro3.jam2.util.context.ConstantMethodContext;
import org.kucro3.jam2.util.context.visitable.VisitableClassContext;
import org.kucro3.jam2.util.context.visitable.VisitableClassContextCompound;
import org.objectweb.asm.Type;

public class Contexts {
	private Contexts()
	{
	}
	
	public static VisitableClassContext toVisitable(ClassContext ctx)
	{
		return VisitableClassContextCompound.newCompound(ctx);
	}
	
	public static MethodContext newMethodConstant(Method method)
	{
		return new ConstantMethodContext(
				Type.getInternalName(method.getDeclaringClass()),
				method.getModifiers(),
				method.getName(),
				Type.getMethodDescriptor(method),
				null,
				Jam2Util.toInternalNames(method.getExceptionTypes()));
	}
	
	public static MethodContext newMethodConstant(Constructor<?> constructor)
	{
		return new ConstantMethodContext(
				Type.getInternalName(constructor.getDeclaringClass()),
				constructor.getModifiers(),
				"<init>",
				Type.getConstructorDescriptor(constructor),
				null,
				Jam2Util.toInternalNames(constructor.getExceptionTypes()));
	}
	
	public static FieldContext newFieldConstant(Field field)
	{
		return new ConstantFieldContext(
				Type.getInternalName(field.getDeclaringClass()),
				field.getModifiers(),
				field.getName(),
				Type.getDescriptor(field.getType()),
				null,
				null);
	}
	
	public static ClassContext newClassConstant(Class<?> clz)
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
		
		ConstantClassContext ccc = new ConstantClassContext(
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
			ccc.newField(newFieldConstant(field));
		
		for(Method method : clz.getDeclaredMethods())
			ccc.newMethod(newMethodConstant(method));
		
		for(Constructor<?> constructor : clz.getDeclaredConstructors())
			ccc.newMethod(newMethodConstant(constructor));
		
		return ccc;
	}
}