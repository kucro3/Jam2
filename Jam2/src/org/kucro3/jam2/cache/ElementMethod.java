package org.kucro3.jam2.cache;

import org.kucro3.jam2.opcode.InstructionContainer;
import org.kucro3.jam2.util.LazyDescriptorAnalyzer;
import org.kucro3.jam2.util.annotation.AnnotationContainer;
import org.kucro3.jam2.visitor.cache.ClassCacheVisitor.MethodContainer;
import org.kucro3.jam2.visitor.cache.ContainerHelper;
import org.kucro3.util.exception.*;

public class ElementMethod {
	ElementMethod(ElementClass owner, MethodContainer method)
	{
		this.owner = owner;
		this.method = method;
		this.lda = new LazyDescriptorAnalyzer(method.fullDesc);
	}
	
	public String getDescriptor()
	{
		return method.desc;
	}
	
	public String getReturnTypeDescriptor()
	{
		return lda.getReturnType();
	}
	
	public String[] getArgumentDescriptors()
	{
		return lda.getArguments();
	}
	
	public String getName()
	{
		return method.name;
	}
	
	public int getAccess()
	{
		return method.access;
	}
	
	public void setAccess(int access)
	{
		method.access = access;
	}
	
	public String getSignature()
	{
		return method.signature;
	}
	
	public void setSignature(String signature)
	{
		method.signature = signature;
	}
	
	public String[] getExceptions()
	{
		return method.exceptions;
	}
	
	public void setExceptions(String[] exceptions)
	{
		method.exceptions = exceptions;
	}
	
	public InstructionContainer getInstructions()
	{
		return method.mcv.getContainer();
	}
	
	public AnnotationContainer getAnnotations()
	{
		return this.method.mcv.getAnnotationContainer();
	}
	
	public AnnotationContainer getAnnotationsOnLocal()
	{
		return this.method.mcv.getLocalAnnotationContainer();
	}

	MethodContainer confirmRenaming(String name, String descriptor)
	{
		MethodContainer _new = owner.ccv.new MethodContainer(name, descriptor),
						_old = method;
		ContainerHelper.copyTo(_old, _new);
		this.method = _new;
		return _old;
	}
	
	public final ElementClass getOwner()
	{
		return owner;
	}
	
	@ImplicitThrows(ElementDuplicatedException.class)
	public void rename(String name)
	{
		rename(name, method.desc);
	}
	
	@ImplicitThrows(ElementDuplicatedException.class)
	public void rename(String name, String descriptor)
	{
		
	}
	
	@ImplicitThrows(ElementDuplicatedException.class)
	public void resetDescriptor(String descriptor)
	{
		rename(method.name, method.desc);
	}
	
	
	
	private final LazyDescriptorAnalyzer lda;
	
	private MethodContainer method;
	
	private final ElementClass owner;
}