package org.kucro3.jam2.util.context.visitable;

import java.util.Collection;
import java.util.Objects;

import org.kucro3.jam2.util.ClassContext;
import org.kucro3.jam2.util.FieldContext;
import org.kucro3.jam2.util.MethodContext;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.TypePath;

public abstract class VisitableClassContextCompound extends VisitableClassContext implements ClassContext.Compound {
	public static VisitableClassContextCompound newCompound(ClassContext ref)
	{
		return newCompound(ref, null);
	}
	
	public static VisitableClassContextCompound newCompound(ClassContext ref, ClassVisitor cv)
	{
		if(ref instanceof ClassContext.RestrictedModifiable)
			return new VisitableClassContextRestrictedModifiableCompound((ClassContext.RestrictedModifiable) ref, cv);
		else if(ref instanceof ClassContext.FullyModifiable)
			return new VisitableClassContextFullyModifiableCompound((ClassContext.FullyModifiable) ref, cv);
		else
			return new VisitableClassContextConstantCompound(ref, cv);
	}
	
	static VisitableClassContext asVisitable(ClassContext ctx)
	{
		if(ctx instanceof VisitableClassContext)
			return (VisitableClassContext) ctx;
		return null;
	}
	
	public VisitableClassContextCompound(ClassContext ref)
	{
		this(ref, null);
	}
	
	public VisitableClassContextCompound(ClassContext ref, ClassVisitor cv)
	{
		super(cv);
		this.ref = Objects.requireNonNull(ref);
		this.visitableRef = asVisitable(ref);
		
		if(cv != null && visitableRef != null)
			throw new IllegalStateException("Duplicated class visitor");
	}
	
	@Override
	public boolean hasField()
	{
		return ref.hasField();
	}
	
	@Override
	public boolean hasMethod()
	{
		return ref.hasMethod();
	}
	
	@Override
	public String getDebug() 
	{
		return ref.getDebug();
	}

	@Override
	public String getEnclosingClass() 
	{
		return ref.getEnclosingClass();
	}

	@Override
	public String getEnclosingMethodName() 
	{
		return ref.getEnclosingMethodName();
	}

	@Override
	public String getEnclosingMethodDescriptor() 
	{
		return ref.getEnclosingMethodDescriptor();
	}

	@Override
	public String[] getInterfaces() 
	{
		return ref.getInterfaces();
	}

	@Override
	public String getSignature() 
	{
		return ref.getSignature();
	}

	@Override
	public String getName() 
	{
		return ref.getName();
	}

	@Override
	public int getModifier() 
	{
		return ref.getModifier();
	}

	@Override
	public String getSource() 
	{
		return ref.getSource();
	}

	@Override
	public String getSuperClass() 
	{
		return ref.getSuperClass();
	}

	@Override
	public int getVersion() 
	{
		return ref.getVersion();
	}

	@Override
	public boolean containsField(String name) 
	{
		return ref.containsField(name);
	}

	@Override
	public boolean containsMethod(String descriptor) 
	{
		return ref.containsMethod(descriptor);
	}

	@Override
	public Collection<FieldContext> getFields() 
	{
		return ref.getFields();
	}

	@Override
	public FieldContext getField(String name) 
	{
		return ref.getField(name);
	}

	@Override
	public Collection<MethodContext> getMethods() 
	{
		return ref.getMethods();
	}

	@Override
	public MethodContext getMethod(String descriptor) 
	{
		return ref.getMethod(descriptor);
	}

	@Override
	public VisitedFieldCompound newField(int modifier, String name, String descriptor, String signature, Object value) 
	{
		FieldVisitor fv = super_visitField(modifier, name, descriptor, signature, value);
		FieldContext fc = ref.newField(modifier, name, descriptor, signature, value);
		return newFieldCompound(fc, fv);
	}
	
	@Override
	public final VisitedFieldCompound visitField(int modifier, String name, String descriptor, String signature, Object value)
	{
		if(visitableRef != null)
			return visitableRef.visitField(modifier, name, descriptor, signature, value);
		return this.newField(modifier, name, descriptor, signature, value);
	}
	
	protected VisitedFieldCompound newFieldCompound(FieldContext ctx, FieldVisitor fv)
	{
		return VisitedFieldCompound.newCompound(ctx, fv);
	}
	
	@Override
	public VisitedMethodCompound newMethod(int modifier, String name, String descriptor, String signature,
			String[] exceptions)
	{
		MethodVisitor mv = super_visitMethod(modifier, name, descriptor, signature, exceptions);
		MethodContext mc = ref.newMethod(modifier, name, descriptor, signature, exceptions);
		return newMethodCompound(mc, mv);
	}
	
	@Override
	public final VisitedMethodCompound visitMethod(int modifier, String name, String descriptor, String signature,
			String[] exceptions)
	{
		if(visitableRef != null)
			return visitableRef.visitMethod(modifier, name, descriptor, signature, exceptions);
		return this.newMethod(modifier, name, descriptor, signature, exceptions);
	}
	
	protected VisitedMethodCompound newMethodCompound(MethodContext ctx, MethodVisitor mv)
	{
		return VisitedMethodCompound.newCompound(ctx, mv);
	}
	
	@Override
	public ClassContext getContext()
	{
		return ref;
	}
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
	{
		if(visitableRef != null)
			visitableRef.visit(version, access, name, signature, superName, interfaces);
		else
			super.visit(version, access, name, signature, superName, interfaces);
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible)
	{
		if(visitableRef != null)
			return visitableRef.visitAnnotation(desc, visible);
		return super.visitAnnotation(desc, visible);
	}
	
	@Override
	public void visitAttribute(Attribute attr)
	{
		if(visitableRef != null)
			visitableRef.visitAttribute(attr);
		else
			super.visitAttribute(attr);
	}
	
	@Override
	public void visitEnd()
	{
		if(visitableRef != null)
			visitableRef.visitEnd();
		else
			super.visitEnd();
	}
	
	@Override
	public void visitInnerClass(String name, String outerName, String innerName, int access)
	{
		if(visitableRef != null)
			visitableRef.visitInnerClass(name, outerName, innerName, access);
		else
			super.visitInnerClass(name, outerName, innerName, access);
	}
	
	@Override
	public void visitOuterClass(String owner, String name, String desc)
	{
		if(visitableRef != null)
			visitableRef.visitOuterClass(owner, name, desc);
		else
			super.visitOuterClass(owner, name, desc);
	}
	
	@Override
	public void visitSource(String source, String debug)
	{
		if(visitableRef != null)
			visitableRef.visitSource(source, debug);
		else
			super.visitSource(source, debug);
	}
	
	@Override
	public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		if(visitableRef != null)
			return visitableRef.visitTypeAnnotation(typeRef, typePath, desc, visible);
		else
			return super.visitTypeAnnotation(typeRef, typePath, desc, visible);
	}
	
	protected final ClassContext ref;
	
	protected final VisitableClassContext visitableRef;
}
