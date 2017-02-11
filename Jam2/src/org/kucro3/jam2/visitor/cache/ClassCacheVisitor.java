package org.kucro3.jam2.visitor.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.kucro3.jam2.util.FieldContext;
import org.kucro3.jam2.util.MethodContext;
import org.kucro3.jam2.util.annotation.Annotation;
import org.kucro3.jam2.util.annotation.AnnotationContainer;
import org.kucro3.jam2.util.annotation.TypeAnnotation;
import org.kucro3.jam2.util.context.FullyModifiableFieldContext;
import org.kucro3.jam2.util.context.FullyModifiableMethodContext;
import org.kucro3.jam2.util.context.visitable.VisitableFullyModifiableClassContext;
import org.kucro3.jam2.util.context.visitable.VisitedFieldCompound;
import org.kucro3.jam2.util.context.visitable.VisitedFieldFullyModifiableCompound;
import org.kucro3.jam2.util.context.visitable.VisitedMethodCompound;
import org.kucro3.jam2.util.context.visitable.VisitedMethodFullyModifiableCompound;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.MethodVisitor;

public class ClassCacheVisitor extends VisitableFullyModifiableClassContext implements CacheVisitor, ClassRevisitable {
	public ClassCacheVisitor() 
	{
		super();
	}
	
	public ClassCacheVisitor(ClassVisitor cv)
	{
		super(cv);
	}

	@Override
	public void clear() 
	{
		attrs.clear();
		annos.clearAnnotations();
		map.clearFieldMapped();
		map.clearMethodMapped();
		endVisited = false;
		innerClasses.clear();
		
		debug = null;
		source = null;
		enclosingClass = null;
		enclosingMethodName = null;
		enclosingMethodDescriptor = null;
		interfaces = null;
		modifier = 0;
		name = null;
		signature = null;
		source = null;
		superName = null;
		version = 0;
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
		Annotation anno = new Annotation(desc, visible);
		AnnotationCacheVisitor acv = new AnnotationCacheVisitor(av, anno);
		annos.putAnnotation(anno);
		return acv;
	}
	
	@Override
	public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		AnnotationVisitor av = super.visitTypeAnnotation(typeRef, typePath, desc, visible);
		TypeAnnotation anno = new TypeAnnotation(typeRef, typePath, desc, visible);
		AnnotationCacheVisitor acv = new AnnotationCacheVisitor(av, anno);
		annos.putAnnotation(anno);
		return acv;
	}
	
	public void clearOuterClass()
	{
		enclosingClass = null;
		enclosingMethodName = null;
		enclosingMethodDescriptor = null;
	}
	
	public void clearSource()
	{
		debug = null;
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
		annos.clearAnnotations();
	}
	
	public void clearAttributes()
	{
		attrs.clear();
	}
	
	public void clearFields()
	{
		map.clearFieldMapped();
	}
	
	public AnnotationContainer getAnnotationContainer()
	{
		return annos;
	}
	
	public void clearMethods()
	{
		map.clearMethodMapped();
	}
	
	@Override
	public void visitEnd()
	{
		endVisited = true;
	}
	
	@Override
	public void revisitEssentials(ClassVisitor cv)
	{
		cv.visit(version, modifier, name, signature, superName, interfaces);
	}
	
	@Override
	public void revisitSource(ClassVisitor cv)
	{
		if(source != null)
			cv.visitSource(source, debug);
	}
	
	@Override
	public void revisitOuterClass(ClassVisitor cv)
	{
		if(enclosingClass != null)
			cv.visitOuterClass(enclosingClass, enclosingMethodName, enclosingMethodDescriptor);
	}
	
	@Override
	public void revisitAnnotations(ClassVisitor cv)
	{
		annos.visit(cv);
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
		for(FieldContext fc : super.getFields())
		{
			CachedField cf = (CachedField) fc;
			RevisitationHelper.revisitOptional(cf.getVisitor(),
					cv.visitField(
							fc.getModifier(),
							fc.getName(),
							fc.getDescriptor(),
							fc.getSignature(), 
							fc.getValue()));
		}
	}
	
	@Override
	public void revisitMethods(ClassVisitor cv)
	{
		for(MethodContext mc : super.getMethods())
		{
			CachedMethod cm = (CachedMethod) mc;
			RevisitationHelper.revisitOptional(cm.getVisitor(), 
					cv.visitMethod(
							cm.getModifier(),
							cm.getName(),
							cm.getDescriptor(),
							cm.getSignature(),
							cm.getExceptions()));
		}
	}
	
	@Override
	public void revisitEnd(ClassVisitor cv)
	{
		if(endVisited)
			cv.visitEnd();
	}
	
	@Override
	protected VisitedFieldCompound newFieldCompound(int modifier, String name, String descriptor, String signature,
			Object value, FieldVisitor fv) 
	{
		return new CachedField(
				new FullyModifiableFieldContext(
						getName(),
						modifier,
						name,
						descriptor,
						signature,
						value),
				new FieldCacheVisitor(fv));
	}

	@Override
	protected VisitedMethodCompound newMethodCompound(int modifier, String name, String descriptor, String signature,
			String[] exceptions, MethodVisitor mv) 
	{
		return new CachedMethod(
				new FullyModifiableMethodContext(
						getName(),
						modifier,
						name,
						descriptor,
						signature,
						exceptions),
				new MethodCacheVisitor(mv));
	}
	
	protected final AttributeCache attrs = new AttributeCache();
	
	protected final List<InfoInnerClassContainer> innerClasses = new ArrayList<>();
	
	protected boolean endVisited;
	
	protected final AnnotationContainer annos = new AnnotationContainer();
	
	public class InfoInnerClassContainer
	{
		public String getName()
		{
			return name;
		}
		
		public String getOuterName()
		{
			return outerName;
		}
		
		public String getInnerName()
		{
			return innerName;
		}
		
		public int getModifier()
		{
			return modifier;
		}
		
		public void setName(String name)
		{
			this.name = Objects.requireNonNull(name);
		}
		
		public void setOuterName(String name)
		{
			this.outerName = name;
		}
		
		public void setInnerName(String name)
		{
			this.innerName = name;
		}
		
		public void setModifier(int access)
		{
			this.access = access;
		}
		
		String name;
		
		String outerName;
		
		String innerName;
		
		int access;
	}
	
	protected class CachedMethod extends VisitedMethodFullyModifiableCompound
	{
		public CachedMethod(MethodContext.FullyModifiable mc, MethodCacheVisitor mv)
		{
			super(mc, mv);
		}
		
		@Override
		public void setName(String name)
		{
			ensureRename(name, getDescriptor());
			super.setName(name);
		}
		
		@Override
		public void setDescriptor(String descriptor)
		{
			ensureRename(getName(), descriptor);
			super.setDescriptor(descriptor);
		}
		
		protected final void ensureRename(String name, String descriptor)
		{
			ClassCacheVisitor ccv = ClassCacheVisitor.this;
			ccv.map.putByMethod(name + descriptor, this); // throwable point
			ccv.map.removeByMethod(getName() + getDescriptor());
		}
		
		@Override
		public MethodCacheVisitor getVisitor()
		{
			return (MethodCacheVisitor) super.getVisitor();
		}
	}
	
	protected class CachedField extends VisitedFieldFullyModifiableCompound
	{
		public CachedField(FieldContext.FullyModifiable fc, FieldCacheVisitor fv)
		{
			super(fc, fv);
		}
		
		@Override
		public void setName(String name)
		{
			ensureRename(name);
			super.setName(name);
		}
		
		protected final void ensureRename(String name)
		{
			ClassCacheVisitor ccv = ClassCacheVisitor.this;
			ccv.map.putByField(name, this); // throwable point
			ccv.map.removeByField(getName());
		}
		
		@Override
		public FieldCacheVisitor getVisitor()
		{
			return (FieldCacheVisitor) super.getVisitor();
		}
	}
}