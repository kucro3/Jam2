package org.kucro3.jam2.util.context.visitable;

import org.kucro3.jam2.util.ClassContext;
import org.kucro3.jam2.util.Version;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

public abstract class VisitableClassContext extends ClassVisitor implements ClassContext.Visitable {
	public VisitableClassContext() 
	{
		super(Version.getASMVersion());
	}
	
	public VisitableClassContext(ClassVisitor cv)
	{
		super(Version.getASMVersion(), cv);
	}
	
	@Override
	public abstract VisitedMethodCompound newMethod(int modifier, String name, String descriptor, String signature,
			String[] exceptions);
	
	@Override
	public abstract VisitedFieldCompound newField(int modifier, String name, String descriptor, String signature, 
			Object value);
	
	@Override
	public abstract VisitedFieldCompound visitField(int modifier, String name, String descriptor, String signature, 
			Object value);
	
	@Override
	public abstract VisitedMethodCompound visitMethod(int modifier, String name, String descriptor, String signature,
			String[] exceptions);
	
	protected final FieldVisitor super_visitField(int modifier, String name, String descriptor, String signature, 
			Object value)
	{
		return super.visitField(modifier, name, descriptor, signature, value);
	}
	
	protected final MethodVisitor super_visitMethod(int modifier, String name, String descriptor, String signature,
			String[] exceptions)
	{
		return super.visitMethod(modifier, name, descriptor, signature, exceptions);
	}
}
