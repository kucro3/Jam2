package org.kucro3.jam2.jar;

import org.kucro3.jam2.opcode.InstructionContainer;
import org.kucro3.jam2.util.ClassContext;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
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
		owner.methods.put(name + desc, cm);
		return new ClassMethodVisitor(cm, mv, owner.isInstructionCached() ? new InstructionContainer(mv) : null);
	}
	
	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value)
	{
		FieldVisitor fv = super.visitField(access, name, desc, signature, value);
		ClassField cf = new ClassField(access, name, desc, signature, value);
		owner.fields.put(name, cf);
		return new ClassFieldVisitor(cf, fv);
	}
	
	final ClassFile owner;
}
