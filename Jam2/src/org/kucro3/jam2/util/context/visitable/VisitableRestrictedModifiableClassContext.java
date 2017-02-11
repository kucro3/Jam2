package org.kucro3.jam2.util.context.visitable;

import org.kucro3.jam2.util.ClassContext;
import org.kucro3.jam2.util.context.RestrictedModifiableFieldContext;
import org.kucro3.jam2.util.context.RestrictedModifiableMethodContext;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

public class VisitableRestrictedModifiableClassContext extends MappedVisitableClassContextTemplate
		implements ClassContext.RestrictedModifiable {
	public VisitableRestrictedModifiableClassContext()
	{
		super();
	}
	
	public VisitableRestrictedModifiableClassContext(ClassVisitor cv)
	{
		super(cv);
	}
	
	@Override
	protected VisitedFieldCompound newFieldCompound(int modifier, String name, String descriptor, String signature,
			Object value, FieldVisitor fv) 
	{
		return new VisitedFieldRestrictedModifiableCompound(
				new RestrictedModifiableFieldContext(getName(), modifier, name, descriptor, signature, value),
				fv);
	}

	@Override
	protected VisitedMethodCompound newMethodCompound(int modifier, String name, String descriptor, String signature,
			String[] exceptions, MethodVisitor mv) 
	{
		return new VisitedMethodRestrictedModifiableCompound(
				new RestrictedModifiableMethodContext(getName(), modifier, name, descriptor, signature, exceptions),
				mv);
	}
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
	{
		setVersion(version);
		setModifier(access);
		this.name = name;
		setSignature(signature);
		setSuperClass(superName);
		setInterfaces(interfaces);
	}
	
	@Override
	public void visitSource(String source, String debug)
	{
		setSource(source);
		setDebug(debug);
	}
	
	@Override
	public void visitOuterClass(String owner, String name, String desc)
	{
		setEnclosingClass(owner);
		setEnclosingMethodName(name);
		setEnclosingMethodDescriptor(desc);
	}
}