package org.kucro3.jam2.util.annotation;

import java.util.HashMap;
import java.util.Map;

import org.kucro3.jam2.util.annotation.Annotation.Value;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class AnnotationContainer {
	public Annotation getAnnotation(Class<?> clz)
	{
		return getAnnotation(Type.getDescriptor(clz));
	}
	
	public Annotation getAnnotation(String descriptor)
	{
		return annotations.get(descriptor);
	}
	
	public Annotation[] getAnnotations()
	{
		return annotations.values().toArray(new Annotation[0]);
	}
	
	public void clearAnnotations()
	{
		annotations.clear();
		defaultValue = null;
	}
	
	public Annotation removeAnnotation(Class<?> clz)
	{
		return removeAnnotation(Type.getDescriptor(clz));
	}
	
	public Annotation removeAnnotation(String descriptor)
	{
		return annotations.remove(descriptor);
	}
	
	public boolean containAnnotation(Class<?> clz)
	{
		return containAnnotation(Type.getDescriptor(clz));
	}
	
	public boolean containAnnotation(String desc)
	{
		return annotations.containsKey(desc);
	}
	
	public void setAnnotationDefault(Value val)
	{
		this.defaultValue = val;
	}
	
	public Value getAnnotationDefault()
	{
		return defaultValue;
	}
	
	public void putAnnotation(Class<?> clz, Annotation annotation)
	{
		putAnnotation(Type.getDescriptor(clz), annotation);
	}
	
	public void putAnnotation(String descriptor, Annotation annotation)
	{
		if(annotation.getDescriptor() == null)
			annotation.setDescriptor(descriptor);
		else if(!annotation.getDescriptor().equals(descriptor))
			throw new IllegalArgumentException("Illegal descriptor");
		
		putAnnotation0(annotation);
	}
	
	public void putAnnotation(Annotation annotation)
	{
		if(annotation.getDescriptor() == null)
			throw new IllegalArgumentException("Descriptor not initialized");
		
		putAnnotation0(annotation);
	}
	
	private void putAnnotation0(Annotation annotation)
	{
		annotations.put(annotation.descriptor, annotation);
	}
	
	public void visit(ClassVisitor cv)
	{
		for(Annotation annotation : annotations.values())
			annotation.visitOn(cv);
	}
	
	public void visit(FieldVisitor fv)
	{
		for(Annotation annotation : annotations.values())
			annotation.visitOn(fv);
	}
	
	public void visit(MethodVisitor mv)
	{
		if(defaultValue != null)
			defaultValue.visit(mv.visitAnnotationDefault(), null);
		for(Annotation annotation : annotations.values())
			annotation.visitOn(mv);
	}
	
	protected Value defaultValue;
	
	private final Map<String, Annotation> annotations = new HashMap<>();
}