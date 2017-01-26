package org.kucro3.jam2.visitor.cache;

import java.util.LinkedList;
import java.util.List;

import org.kucro3.jam2.util.Version;
import org.objectweb.asm.AnnotationVisitor;

public class AnnotationCacheVisitor extends AnnotationVisitor implements CacheVisitor {
	public AnnotationCacheVisitor()
	{
		super(Version.getASMVersion());
	}
	
	public AnnotationCacheVisitor(AnnotationVisitor av)
	{
		super(Version.getASMVersion(), av);
	}
	
	@Override
	public void visit(String name, Object value)
	{
		super.visit(name, value);
		actions.add(new ActionVisit(name, value));
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String name, String desc)
	{
		AnnotationVisitor av = super.visitAnnotation(name, desc);
		AnnotationCacheVisitor acv = new AnnotationCacheVisitor(av);
		actions.add(new ActionVisitAnnotationInAnnotation(name, desc, acv));
		return acv;
	}
	
	@Override
	public AnnotationVisitor visitArray(String name)
	{
		AnnotationVisitor av = super.visitArray(name);
		AnnotationCacheVisitor acv = new AnnotationCacheVisitor(av);
		actions.add(new ActionVisitArray(name, acv));
		return acv;
	}
	
	@Override
	public void visitEnd()
	{
		super.visitEnd();
		actions.add(new ActionVisitEnd());
	}
	
	@Override
	public void visitEnum(String name, String desc, String value)
	{
		super.visitEnum(name, desc, value);
		actions.add(new ActionVisitEnum(name, desc, value));
	}
	
	@Override
	public void clear() 
	{
		actions.clear();
	}

	public void revisit(AnnotationVisitor t) 
	{
		for(Action act : actions)
			act.revisit(t);
	}
	
	public void revisitOptional(AnnotationVisitor t)
	{
		if(t != null)
			revisit(t);
	}
	
	private final List<Action> actions = new LinkedList<>();
	
	class ActionVisit implements Action
	{
		ActionVisit(String name, Object value)
		{
			this.name = name;
			this.value = value;
		}

		@Override
		public void revisit(AnnotationVisitor av)
		{
			av.visit(name, value);
		}
		
		final String name;
		
		final Object value;
	}
	
	class ActionVisitArray implements Action
	{
		ActionVisitArray(String name, AnnotationCacheVisitor acv)
		{
			this.acv = acv;
			this.name = name;
		}
		
		@Override
		public void revisit(AnnotationVisitor av) 
		{
			AnnotationVisitor v = av.visitArray(name);
			if(acv != null)
				acv.revisit(v);
		}
		
		final AnnotationCacheVisitor acv;
		
		final String name;
	}
	
	class ActionVisitEnum implements Action
	{
		ActionVisitEnum(String name, String desc, String value)
		{
			this.name = name;
			this.desc = desc;
			this.value = value;
		}
		
		@Override
		public void revisit(AnnotationVisitor av) 
		{
			av.visitEnum(name, desc, value);
		}
		
		final String name;
		
		final String desc;
		
		final String value;
	}
}