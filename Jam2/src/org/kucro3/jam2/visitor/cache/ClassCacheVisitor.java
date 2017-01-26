package org.kucro3.jam2.visitor.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.kucro3.jam2.util.Jam2Util;
import org.kucro3.jam2.util.Version;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.MethodVisitor;

public class ClassCacheVisitor extends ClassVisitor implements CacheVisitor, ClassRevisitable {
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
	public void visitInnerClass(String name, String outerName, String innerName, int access)
	{
		InfoInnerClassContainer container = new InfoInnerClassContainer();
		innerClasses.add(container);
		container.name = name;
		container.outerName = outerName;
		container.innerName = innerName;
		container.access = access;
	}
	
	@Override
	public void visitAttribute(Attribute attr)
	{
		attrs.visitAttribute(attr);
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible)
	{
		AnnotationVisitor av = super.visitAnnotation(desc, visible);
		AnnotationCacheVisitor acv = new AnnotationCacheVisitor(av);
		InfoAnnotationContainer iac = new InfoAnnotationContainer();
		iac.desc = desc;
		iac.visible = visible;
		iac.acv = acv;
		annos.add(iac);
		return acv;
	}
	
	@Override
	public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		AnnotationVisitor av = super.visitTypeAnnotation(typeRef, typePath, desc, visible);
		AnnotationCacheVisitor acv = new AnnotationCacheVisitor(av);
		InfoTypeAnnotationContainer itac = new InfoTypeAnnotationContainer();
		itac.typeRef = typeRef;
		itac.typePath = typePath;
		itac.desc = desc;
		itac.visible = visible;
		itac.acv = acv;
		annos.add(itac);
		return acv;
	}
	
	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value)
	{
		FieldVisitor fv = super.visitField(access, name, desc, signature, value);
		FieldCacheVisitor fcv = new FieldCacheVisitor(fv);
		FieldContainer fc = new FieldContainer(name);
		fc.access = access;
		fc.desc = desc;
		fc.signature = signature;
		fc.value = value;
		fc.fcv = fcv;
		fields.put(name, fc);
		return fcv;
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
	{
		MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
		MethodCacheVisitor mcv = new MethodCacheVisitor(mv);
		MethodContainer mc = new MethodContainer(name, desc);
		mc.access = access;
		mc.signature = signature;
		mc.exceptions = exceptions;
		mc.mcv = mcv;
		String fullDesc = name + desc;
		methods.put(fullDesc, mc);
		return mcv;
	}
	
	public ClassEssentialsContainer getEssentials()
	{
		return ess;
	}
	
	public InfoOuterClassContainer getOuterClass()
	{
		return outerClass;
	}
	
	public void clearOuterClass()
	{
		outerClass = null;
	}
	
	public InfoSourceContainer getSource()
	{
		return source;
	}
	
	public void clearSource()
	{
		source = null;
	}
	
	public void clearVisitEndFlag()
	{
		endVisited = false;
	}
	
	public List<InfoInnerClassContainer> getInnerClasses()
	{
		return innerClasses;
	}
	
	public void clearInnerClasses()
	{
		innerClasses.clear();
	}
	
	public void clearAnnotations()
	{
		annos.clear();
	}
	
	public void clearAttributes()
	{
		attrs.clear();
	}
	
	public FieldContainer getField(String name)
	{
		return fields.get(name);
	}
	
	public void removeField(String name)
	{
		fields.remove(name);
	}
	
	public void clearFields()
	{
		fields.clear();
	}
	
	public MethodContainer getMethod(String name, String returnType, String[] arguments)
	{
		return getMethod(Jam2Util.toDescriptor(name, returnType, arguments));
	}
	
	public MethodContainer getMethod(String desc)
	{
		return methods.get(desc);
	}
	
	public void removeMethod(String name, String returnType, String[] arguments)
	{
		removeMethod(Jam2Util.toDescriptor(name, returnType, arguments));
	}
	
	public void removeMethod(String desc)
	{
		methods.remove(desc);
	}
	
	public void clearMethods()
	{
		methods.clear();
	}
	
	@Override
	public void revisitEssentials(ClassVisitor cv)
	{
		cv.visit(ess.version, ess.access, ess.name, ess.signature, ess.superName, ess.interfaces);
	}
	
	@Override
	public void revisitSource(ClassVisitor cv)
	{
		if(source != null)
			cv.visitSource(source.source, source.debug);
	}
	
	@Override
	public void revisitOuterClass(ClassVisitor cv)
	{
		if(outerClass != null)
			cv.visitOuterClass(outerClass.owner, outerClass.name, outerClass.desc);
	}
	
	@Override
	public void revisitAnnotations(ClassVisitor cv)
	{
		for(InfoAnnotationContainer iac : annos)
			iac.visit(cv);
	}
	
	@Override
	public void revisitAttributes(ClassVisitor cv)
	{
		attrs.revisit(cv);
	}
	
	@Override
	public void revisitInnerClasses(ClassVisitor cv)
	{
		for(InfoInnerClassContainer innerClass : innerClasses)
			cv.visitInnerClass(innerClass.name, innerClass.outerName, innerClass.innerName, innerClass.access);
	}
	
	@Override
	public void revisitFields(ClassVisitor cv)
	{
		for(FieldContainer fc : fields.values())
			RevisitationHelper.revisitOptional(fc.fcv, cv.visitField(fc.access, fc.name, fc.desc, fc.signature, fc.value));
	}
	
	@Override
	public void revisitMethods(ClassVisitor cv)
	{
		for(MethodContainer mc : methods.values())
			RevisitationHelper.revisitOptional(mc.mcv, cv.visitMethod(mc.access, mc.name, mc.desc, mc.signature, mc.exceptions));
	}
	
	@Override
	public void revisitEnd(ClassVisitor cv)
	{
		if(endVisited)
			cv.visitEnd();
	}
	
	protected final AttributeCache attrs = new AttributeCache();
	
	protected final ClassEssentialsContainer ess = new ClassEssentialsContainer();
	
	protected InfoOuterClassContainer outerClass;
	
	protected final List<InfoInnerClassContainer> innerClasses = new ArrayList<>();
	
	protected InfoSourceContainer source;
	
	protected boolean endVisited;
	
	protected final List<InfoAnnotationContainer> annos = new ArrayList<>();
	
	protected final Map<String, FieldContainer> fields = new HashMap<>();
	
	protected final Map<String, MethodContainer> methods = new HashMap<>();
	
	public class ClassEssentialsContainer
	{
		public int version;
		
		public int access;
		
		public String name;
		
		public String signature;
		
		public String superName;
		
		public String[] interfaces;
	}
	
	public class InfoOuterClassContainer
	{
		public String owner;
		
		public String name;
		
		public String desc;
	}
	
	public class InfoSourceContainer
	{
		public String source;
		
		public String debug;
	}
	
	public class InfoInnerClassContainer
	{
		public String name;
		
		public String outerName;
		
		public String innerName;
		
		public int access;
	}
	
	public class InfoAnnotationContainer
	{
		public String desc;
		
		public boolean visible;
		
		public AnnotationCacheVisitor acv;
		
		public void visit(ClassVisitor cv)
		{
			acv.revisitOptional(cv.visitAnnotation(desc, visible));
		}
	}
	
	public class InfoTypeAnnotationContainer extends InfoAnnotationContainer
	{
		public int typeRef;
		
		public TypePath typePath;
		
		@Override
		public void visit(ClassVisitor cv)
		{
			acv.revisitOptional(cv.visitTypeAnnotation(typeRef, typePath, desc, visible));
		}
	}
	
	public class MethodContainer
	{
		public MethodContainer(String name, String desc)
		{
			this.name = name;
			this.desc = desc;
		}
		
		public int access;
		
		public final String name;
		
		public final String desc;
		
		public String signature;
		
		public String[] exceptions;
		
		public MethodCacheVisitor mcv;
	}
	
	public class FieldContainer
	{
		public FieldContainer(String name)
		{
			this.name = name;
		}
		
		public int access;
		
		public final String name;
		
		public String desc;
		
		public String signature;
		
		public Object value;
		
		public FieldCacheVisitor fcv;
	}
}