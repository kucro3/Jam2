package org.kucro3.jam2.jar;

import org.kucro3.jam2.util.ClassContext;
import org.objectweb.asm.FieldVisitor;

public class ClassFieldVisitor extends FieldVisitor {
	ClassFieldVisitor(ClassField owner, FieldVisitor fv)
	{
		super(ClassContext.API, fv);
		this.owner = owner;
	}
	
	public ClassField getOwner()
	{
		return owner;
	}
	
	private final ClassField owner;
}
