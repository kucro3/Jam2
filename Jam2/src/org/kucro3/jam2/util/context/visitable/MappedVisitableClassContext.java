package org.kucro3.jam2.util.context.visitable;

import java.util.Collection;

import org.kucro3.jam2.util.ClassMemberMap;
import org.kucro3.jam2.util.FieldContext;
import org.kucro3.jam2.util.MethodContext;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

public abstract class MappedVisitableClassContext extends AbstractVisitableClassContext {
	public MappedVisitableClassContext()
	{
	}
	
	public MappedVisitableClassContext(ClassVisitor cv)
	{
		super(cv);
	}

	@Override
	public boolean containsField(String name) 
	{
		return map.mappedByField(name);
	}

	@Override
	public boolean containsMethod(String descriptor) 
	{
		return map.mappedByMethod(descriptor);
	}

	@Override
	public VisitedFieldCompound getField(String name)
	{
		return (VisitedFieldCompound) map.getByField(name);
	}

	@Override
	public VisitedMethodCompound getMethod(String descriptor)
	{
		return (VisitedMethodCompound) map.getByMethod(descriptor);
	}
	
	@Override
	public Collection<FieldContext> getFields() 
	{
		return map.byFields();
	}

	@Override
	public Collection<MethodContext> getMethods() 
	{
		return map.byMethods();
	}
	
	@Override
	public boolean hasField()
	{
		return map.hasField();
	}
	
	@Override
	public boolean hasMethod()
	{
		return map.hasMethod();
	}
	
	@Override
	public final VisitedFieldCompound visitField(int access, String name, String desc, String signature, Object value)
	{
		return newField(access, name, desc, signature, value);
	}
	
	@Override
	public final VisitedFieldCompound newField(int modifier, String name, String descriptor, String signature, 
			Object value)
	{
		FieldVisitor fv = super_visitField(modifier, name, descriptor, signature, value);
		VisitedFieldCompound vfc = newFieldCompound(modifier, name, descriptor, signature, value, fv);
		map.putByField(name, vfc);
		return vfc;
	}
	
	protected abstract VisitedFieldCompound newFieldCompound(int modifier, String name, String descriptor, String signature, 
			Object value, FieldVisitor fv);
	
	@Override
	public final VisitedMethodCompound visitMethod(int access, String name, String desc, String signature, String[] exceptions)
	{
		return newMethod(access, name, desc, signature, exceptions);
	}

	@Override
	public final VisitedMethodCompound newMethod(int modifier, String name, String descriptor, String signature,
			String[] exceptions)
	{
		MethodVisitor mv = super_visitMethod(modifier, name, descriptor, signature, exceptions);
		VisitedMethodCompound vmc = newMethodCompound(modifier, name, descriptor, signature, exceptions, mv);
		map.putByMethod(name, descriptor, vmc);
		return vmc;
	}
	
	protected abstract VisitedMethodCompound newMethodCompound(int modifier, String name, String descriptor, String signature,
			String[] exceptions, MethodVisitor mv);
	
	protected ClassMemberMap<MethodContext, FieldContext> map;
}
