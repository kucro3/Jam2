package org.kucro3.jam2.jar;

import org.kucro3.jam2.util.ClassContext;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

class ClassFileVisitor extends ClassVisitor {
	ClassFileVisitor(ClassFile owner)
	{
		this(owner, null);
	}
	
	ClassFileVisitor(ClassFile owner, ClassVisitor cv)
	{
		super(ClassContext.API, cv);
		this.owner = owner;
	}
	
	public ClassFile getOwner()
	{
		return owner;
	}
	
	@Override
	public void visitSource(String source, String debug)
	{
		owner.source = source;
		owner.debug = debug;
		super.visitSource(source, debug);
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
	{
		MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
		ClassMethod cm = new ClassMethod(access, name, desc, signature, exceptions);
		return new ClassMethodVisitor(cm, mv);
	}
	
	final ClassFile owner;
}
