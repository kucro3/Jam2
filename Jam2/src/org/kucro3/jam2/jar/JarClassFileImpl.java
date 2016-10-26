package org.kucro3.jam2.jar;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.kucro3.jam2.opcode.Instruction;
import org.kucro3.jam2.opcode.InstructionContainer;
import org.kucro3.jam2.util.Jam2Util;
import org.kucro3.jam2.util.Version;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

class JarClassFileImpl implements ClassFile {
	JarClassFileImpl(Class<?> loadedClass, ClassReader reader, String name, String location, boolean cached)
	{
		this.loadedClass = loadedClass;
		this.cr = reader;
		this.cached = cached;
		this.location = location;
		this.name = name;
		
		if(cached)
			this.cfv = new ClassFileVisitor(this);
		else
			this.cfv = null;
		
		if(this.cr != null)
			this.cr.accept(this.cfv, 0);
	}
	
	public Class<?> getLoadedClass()
	{
		return loadedClass;
	}
	
	public String getSource()
	{
		return source;
	}
	
	public String getDebug()
	{
		return debug;
	}
	
	public Collection<ClassField> getFields()
	{
		return fields.values();
	}
	
	public Collection<ClassMethod> getMethods()
	{
		return methods.values();
	}
	
	public boolean containsField(String name)
	{
		return fields.containsKey(name);
	}
	
	public ClassField getField(String name)
	{
		return fields.get(name);
	}
	
	public boolean containsMethod(String name, Class<?> returnType, Class<?>... arguments)
	{
		return methods.containsKey(Jam2Util.toDescriptor(name, returnType, arguments));
	}
	
	public ClassMethod getMethod(String name, Class<?> returnType, Class<?>... arguments)
	{
		return methods.get(Jam2Util.toDescriptor(name, returnType, arguments));
	}
	
	public ClassMethod getMethod(String name, String returnType, String... arguments)
	{
		return methods.get(Jam2Util.toDescriptor(name, returnType, arguments));
	}
	
	public ClassMethod getMethod(String name, String descriptor)
	{
		return methods.get(name + descriptor);
	}
	
	String getLocation()
	{
		return location;
	}
	
	public String getClassName()
	{
		return name;
	}
	
	boolean isInstructionContained()
	{
		return cached;
	}
	
	public Class<?> force()
	{
		if(loadedClass != null)
			return loadedClass;
		return Jam2Util.getInstance().defClass(cr.b, 0, cr.b.length);
	}
	
	private final String name;
	
	private final String location;
	
	private final Class<?> loadedClass;
	
	private final ClassReader cr;
	
	private final boolean cached;
	
	final ClassFileVisitor cfv;
	
	// initialized by CFV
	
	String source;
	
	String debug;
	
	final Map<String, ClassField> fields = new HashMap<>();
	
	final Map<String, ClassMethod> methods = new HashMap<>();
	
	class ClassFieldImpl implements ClassField {
		ClassFieldImpl(int access, String name, String desc, String signature, Object value)
		{
			this.access = access;
			this.name = name;
			this.descriptor = desc;
			this.signature = signature;
			this.value = value;
		}
		
		public int getAccess()
		{
			return access;
		}
		
		public String getName()
		{
			return name;
		}
		
		public String getDescriptor()
		{
			return descriptor;
		}
		
		public String getSignature()
		{
			return signature;
		}
		
		public Object getValue()
		{
			return value;
		}
		
		private final int access;
		
		private final String name;
		
		private final String descriptor;
		
		private final String signature;
		
		private final Object value;
	}
	
	class ClassMethodImpl implements ClassMethod {
		ClassMethodImpl(int access, String name, String descriptor, String signature, String[] exceptions)
		{
			this.access = access;
			this.name = name;
			this.descriptor = descriptor;
			this.signature = signature;
			this.exceptions = exceptions;
			this.descAnalyzer = new LazyDescriptorAnalyzer(descriptor);
		}
		
		public int getAccess()
		{
			return access;
		}
		
		public String getName()
		{
			return name;
		}
		
		public String getReturnType()
		{
			return descAnalyzer.getReturnType();
		}
		
		public String[] getArguments()
		{
			return descAnalyzer.getArguments();
		}
		
		public String getDescriptor()
		{
			return descriptor;
		}
		
		public String getSignature()
		{
			return signature;
		}
		
		public String[] getExceptions()
		{
			return exceptions;
		}
		
		@Deprecated
		public Instruction[] getInstructions()
		{
			return insns;
		}
		
		public InstructionContainer getInstructionContainer()
		{
			return container;
		}
		
		private final LazyDescriptorAnalyzer descAnalyzer;
		
		private final int access;
		
		private final String name;
		
		private final String descriptor;
		
		private final String signature;
		
		private final String[] exceptions;
		
		// initialized by CMV
		Instruction[] insns;
		
		InstructionContainer container;
	}
	
	class ClassFileVisitor extends ClassVisitor {
		ClassFileVisitor(JarClassFileImpl owner)
		{
			this(owner, null);
		}
		
		ClassFileVisitor(JarClassFileImpl owner, ClassVisitor cv)
		{
			super(Version.getASMVersion(), cv);
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
			ClassMethodImpl cm = new ClassMethodImpl(access, name, desc, signature, exceptions);
			owner.methods.put(name + desc, cm);
			return new ClassMethodVisitor(cm, mv, owner.isInstructionContained() ? new InstructionContainer(mv) : null);
		}
		
		@Override
		public FieldVisitor visitField(int access, String name, String desc, String signature, Object value)
		{
			FieldVisitor fv = super.visitField(access, name, desc, signature, value);
			ClassField cf = new ClassFieldImpl(access, name, desc, signature, value);
			owner.fields.put(name, cf);
			return new ClassFieldVisitor(cf, fv);
		}
		
		final JarClassFileImpl owner;
	}
	
	class ClassFieldVisitor extends FieldVisitor {
		ClassFieldVisitor(ClassField owner, FieldVisitor fv)
		{
			super(Version.getASMVersion(), fv);
			this.owner = owner;
		}
		
		public ClassField getOwner()
		{
			return owner;
		}
		
		private final ClassField owner;
	}
	
	class ClassMethodVisitor extends MethodVisitor {
		ClassMethodVisitor(ClassMethodImpl cm, MethodVisitor mv, InstructionContainer container)
		{
			super(Version.getASMVersion(), container == null ? mv : container);
			this.container = container;
			this.owner = cm;
		}
		
		public ClassMethod getOwner()
		{
			return owner;
		}
		
		@Override
		public void visitEnd()
		{
			this.owner.insns = container.toInstructions();
			this.owner.container = container;
			super.visitEnd();
		}
		
		private final InstructionContainer container;
		
		private final ClassMethodImpl owner;
	}
}
