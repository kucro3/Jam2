package org.kucro3.jam2.util;

import java.lang.reflect.Method;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import static org.kucro3.jam2.util.Jam2Util.*;

public class LambdaContext
{
	public static LambdaContext newContext(Method method)
	{
		return new LambdaContext(method.getDeclaringClass(), method.getName(),
				method.getReturnType(), method.getParameterTypes());
	}
	
	public static LambdaContext newContext(Class<?> templateClass, String functionName,
			Class<?> returnType, Class<?>... arguments)
	{
		return new LambdaContext(templateClass, functionName, returnType, arguments);
	}
	
	LambdaContext(Class<?> lambdaTemplate, String functionName, Class<?> returnType, Class<?>[] arguments)
	{
		this(Type.getInternalName(lambdaTemplate), functionName, lambdaTemplate,
				Type.getDescriptor(returnType), returnType,
				_toDescriptors(arguments), arguments);
	}
	
	LambdaContext(String lambdaTemplate, String functionName, Class<?> returnType, Class<?>[] arguments)
	{
		this(lambdaTemplate, functionName, null,
				Type.getDescriptor(returnType), returnType,
				_toDescriptors(arguments), arguments);
	}
	
	LambdaContext(String lambdaTemplate, String functionName, String returnType, String[] arguments)
	{
		this(lambdaTemplate, functionName, null,
				returnType, null,
				arguments, null);
	}
	
	protected LambdaContext(String template, String functionName, Class<?> templateClass, 
			String returnType, Class<?> returnTypeClass,
			String[] argumentDescriptors, Class<?>[] arguments)
	{
		this.functionName = functionName;
		this.template = template;
		this.templateClass = templateClass;
		this.returnType = returnType;
		this.returnTypeClass = returnTypeClass;
		this.argumentDescriptors = argumentDescriptors;
		this.arguments = arguments;
		this.descriptor = _toDescriptor(returnType, argumentDescriptors, "");
	}
	
	public String getFunctionName()
	{
		return functionName;
	}
	
	public String getTemplate()
	{
		return template;
	}
	
	public Class<?> getTemplateClass()
	{
		return templateClass;
	}
	
	public String getReturnType()
	{
		return returnType;
	}
	
	public Class<?> getReturnTypeClass()
	{
		return returnTypeClass;
	}
	
	public String[] getArgumentDescriptors()
	{
		return argumentDescriptors;
	}
	
	public Class<?>[] getArgumentTypes()
	{
		return arguments;
	}
	
	public String getDescriptor()
	{
		return descriptor;
	}
	
	public String getInternalLambdaName()
	{
		return internalLambdaName;
	}
	
	public MethodVisitor getMethodVisitor()
	{
		return mv;
	}
	
	private final String functionName;
	
	private final String template;
	
	private final Class<?> templateClass;
	
	private final String returnType;
	
	private final Class<?> returnTypeClass;
	
	private final String[] argumentDescriptors;
	
	private final Class<?>[] arguments;
	
	private final String descriptor;
	
	String internalLambdaName;
	
	MethodVisitor mv;
}
