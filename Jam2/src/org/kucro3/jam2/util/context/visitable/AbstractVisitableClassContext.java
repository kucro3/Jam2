package org.kucro3.jam2.util.context.visitable;

import org.objectweb.asm.ClassVisitor;

public abstract class AbstractVisitableClassContext extends VisitableClassContext {
	public AbstractVisitableClassContext()
	{
		super();
	}
	
	public AbstractVisitableClassContext(ClassVisitor cv)
	{
		super(cv);
	}

	@Override
	public String getSource()
	{
		return source;
	}
	
	@Override
	public String getDebug()
	{
		return debug;
	}
	
	@Override
	public int getVersion()
	{
		return version;
	}
	
	@Override
	public String getSuperClass()
	{
		return superName;
	}
	
	@Override
	public String[] getInterfaces()
	{
		return interfaces;
	}
	
	@Override
	public String getEnclosingClass()
	{
		return enclosingClass;
	}
	
	@Override
	public String getEnclosingMethodName()
	{
		return enclosingMethodName;
	}
	
	@Override
	public String getEnclosingMethodDescriptor()
	{
		return enclosingMethodDescriptor;
	}
	
	@Override
	public int getModifier()
	{
		return modifier;
	}
	
	@Override
	public String getSignature()
	{
		return signature;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
	{
		super.visit(version, access, name, signature, superName, interfaces);
		this.version = version;
		this.modifier = access;
		this.name = name;
		this.signature = signature;
		this.superName = superName;
		this.interfaces = interfaces;
	}
	
	@Override
	public void visitSource(String source, String debug)
	{
		super.visitSource(source, debug);
		this.source = source;
		this.debug = debug;
	}
	
	@Override
	public void visitOuterClass(String owner, String name, String desc)
	{
		super.visitOuterClass(owner, name, desc);
		this.enclosingClass = owner;
		this.enclosingMethodName = name;
		this.enclosingMethodDescriptor = desc;
	}
	
	protected int modifier;
	
	protected String source;
	
	protected String debug;
	
	protected int version;
	
	protected String signature;
	
	protected String superName;
	
	protected String[] interfaces;
	
	protected String enclosingClass;
	
	protected String enclosingMethodName;
	
	protected String enclosingMethodDescriptor;
	
	protected String name;
}