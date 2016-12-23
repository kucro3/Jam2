package org.kucro3.jam2.visitor;

import java.util.LinkedList;

import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

class AttributeCache {
	public void visitAttribute(Attribute attr)
	{
		attrs.add(attr);
	}
	
	public void clear()
	{
		attrs.clear();
	}
	
	public void revisit(FieldVisitor fv)
	{
		for(Attribute attr : attrs)
			fv.visitAttribute(attr);
	}
	
	public void revisit(MethodVisitor mv)
	{
		for(Attribute attr : attrs)
			mv.visitAttribute(attr);
	}
	
	public void revisit(ClassVisitor cv)
	{
		for(Attribute attr : attrs)
			cv.visitAttribute(attr);
	}
	
	private final LinkedList<Attribute> attrs = new LinkedList<>();
}