package org.kucro3.jam2.util.context.visitable;

import org.kucro3.jam2.util.context.ConstantFieldContext;
import org.kucro3.jam2.util.context.ConstantMethodContext;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

public class VisitableConstantClassContext extends MappedVisitableClassContext {
	public VisitableConstantClassContext()
	{
		super();
	}
	
	public VisitableConstantClassContext(ClassVisitor cv)
	{
		super(cv);
	}
	
	@Override
	protected VisitedFieldCompound newFieldCompound(int modifier, String name, String descriptor, String signature,
			Object value, FieldVisitor fv) 
	{
		return new VisitedFieldCompound(
				new ConstantFieldContext(getName(), modifier, name, descriptor, signature, value),
				fv);
	}

	@Override
	protected VisitedMethodCompound newMethodCompound(int modifier, String name, String descriptor, String signature,
			String[] exceptions, MethodVisitor mv) 
	{
		return new VisitedMethodCompound(
				new ConstantMethodContext(getName(), modifier, name, descriptor, signature, exceptions),
				mv);
	}
}
