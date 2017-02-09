package org.kucro3.jam2.util.context.visitable;

import org.kucro3.jam2.util.ClassContext;
import org.kucro3.jam2.util.context.FullyModifiableFieldContext;
import org.kucro3.jam2.util.context.FullyModifiableMethodContext;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

public class VisitableFullyModifiableClassContext extends MappedVisitableClassContextTemplate
		implements ClassContext.FullyModifiable {
	public VisitableFullyModifiableClassContext()
	{
		super();
	}
	
	public VisitableFullyModifiableClassContext(ClassVisitor cv)
	{
		super(cv);
	}
	
	@Override
	protected VisitedFieldCompound newFieldCompound(int modifier, String name, String descriptor, String signature,
			Object value, FieldVisitor fv)
	{
		return new VisitedFieldFullyModifiableCompound(
				new FullyModifiableFieldContext(getName(), modifier, name, descriptor, signature, value),
				fv);
	}

	@Override
	protected VisitedMethodCompound newMethodCompound(int modifier, String name, String descriptor, String signature,
			String[] exceptions, MethodVisitor mv)
	{
		return new VisitedMethodFullyModifiableCompound(
				new FullyModifiableMethodContext(getName(), modifier, name, descriptor, signature, exceptions),
				mv);
	}

	@Override
	public void setName(String name) 
	{
		super.name = name;
	}
}
