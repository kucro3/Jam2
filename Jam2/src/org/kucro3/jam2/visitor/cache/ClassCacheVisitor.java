package org.kucro3.jam2.visitor.cache;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.SoundbankResource;

import org.kucro3.jam2.util.Version;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.TypePath;

public class ClassCacheVisitor extends ClassVisitor implements CacheVisitor<ClassVisitor> {
	public ClassCacheVisitor() 
	{
		super(Version.getASMVersion());
	}
	
	public ClassCacheVisitor(ClassVisitor cv)
	{
		super(Version.getASMVersion(), cv);
	}

	@Override
	public void clear() 
	{
		
	}

	@Override
	public void revisit(ClassVisitor t) 
	{
		
	}
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
	{
		ess.version = version;
		ess.access = access;
		ess.name = name;
		ess.signature = signature;
		ess.superName = superName;
		ess.interfaces = interfaces;
	}
	
	@Override
	public void visitSource(String source, String debug)
	{
		if(this.source == null)
			this.source = new InfoSourceContainer();
		this.source.source = source;
		this.source.debug = debug;
	}
	
	@Override
	public void visitOuterClass(String owner, String name, String desc)
	{
		if(outerClass == null)
			outerClass = new InfoOuterClassContainer();
		outerClass.owner = owner;
		outerClass.name = name;
		outerClass.desc = desc;
	}
	
	@Override
	public void visitAttribute(Attribute attr)
	{
		attrs.visitAttribute(attr);
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible)
	{
		return _visitAnnotation(super.visitAnnotation(desc, visible));
	}
	
	@Override
	public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		return _visitAnnotation(super.visitTypeAnnotation(typeRef, typePath, desc, visible));
	}
	
	private AnnotationVisitor _visitAnnotation(AnnotationVisitor av)
	{
		AnnotationCacheVisitor acv = new AnnotationCacheVisitor(av);
		annos.add(acv);
		return acv;
	}
	
	protected final AttributeCache attrs = new AttributeCache();
	
	protected final ClassEssentialsContainer ess = new ClassEssentialsContainer();
	
	protected InfoOuterClassContainer outerClass;
	
	protected InfoSourceContainer source;
	
	protected boolean endVisited;
	
	protected List<AnnotationCacheVisitor> annos = new ArrayList<>();
	
	protected class ClassEssentialsContainer
	{
		public int version;
		
		public int access;
		
		public String name;
		
		public String signature;
		
		public String superName;
		
		public String[] interfaces;
	}
	
	protected class InfoOuterClassContainer
	{
		public String owner;
		
		public String name;
		
		public String desc;
	}
	
	protected class InfoSourceContainer
	{
		public String source;
		
		public String debug;
	}
}