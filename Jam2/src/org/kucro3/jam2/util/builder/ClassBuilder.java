package org.kucro3.jam2.util.builder;

import org.kucro3.jam2.util.*;
import org.kucro3.jam2.util.builder.AnnotationBuilder.ClassAnnotationBuilder;
import org.kucro3.jam2.util.builder.structure.InheritanceView;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.TypePath;

import java.lang.reflect.Modifier;
import java.util.*;

public class ClassBuilder implements Opcodes, ClassContext {
	public ClassBuilder(ClassContext cctx)
	{
		this(cctx, 0);
	}
	
	public ClassBuilder(ClassContext cctx, int flags)
	{
		this(cctx.getVersion(), cctx.getModifier(), cctx.getName(), cctx.getSignature(),
				cctx.getSuperClass(), cctx.getInterfaces(), flags);
	}
	
	public ClassBuilder(int version, int access, String name, String signature, String superName, String[] interfaces)
	{
		this(version, access, name, signature, superName, interfaces, 0);
	}
	
	public ClassBuilder(int version, int access, String name, String signature, String superName, String[] interfaces, int flags)
	{
		this(version, access, name, signature, superName, interfaces, flags, /* TODO */ null);
	}

	private ClassBuilder(int version, int access, String name, String signature, String superName, String[] interfaces, int flags, InheritanceView view)
	{
		this.cw = new ClassWriter(flags);
		this.name = name;
		this.cw.visit(
				this.version = version,
				this.access = access,
				this.internalName = Jam2Util.fromCanonicalToInternalName(name),
				this.signature = signature,
				this.superclass = Jam2Util.fromCanonicalToInternalName(superName),
				this.interfaces = Jam2Util.fromCanonicalsToInternalNames(interfaces));
		this.view = view;
	}
	
	public ClassBuilder appendSource(String source, String debug)
	{
		cw.visitSource(source, debug);
		return this;
	}
	
	public ClassBuilder appendOuterClass(String owner, String name, String descriptor)
	{
		cw.visitOuterClass(owner, name, descriptor);
		return this;
	}

//	public ClassBuilder appendInnerClass(String name, String outerName, String innerName, int access)
//	{
//		cw.visitInnerClass(name, outerName, innerName, access);
//		return this;
//	}
	
	public ClassAnnotationBuilder appendAnnotation(String desc, boolean visible)
	{
		return new ClassAnnotationBuilder(cw.visitAnnotation(desc, visible), this);
	}
	
	public ClassAnnotationBuilder appendTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible)
	{
		return new ClassAnnotationBuilder(cw.visitTypeAnnotation(typeRef, typePath, desc, visible), this);
	}
	
	public FieldBuilder appendField(int access, String name, String desc, String signature, Object value)
	{
		FieldContext ctx = Contexts.newFieldConstant(internalName, access, name, desc, signature, value);
		if(fields.putIfAbsent(name, ctx) != null)
			throw new IllegalArgumentException("Field duplicated: " + name);
		return new FieldBuilder(this, cw.visitField(access, name, desc, signature, value));
	}
	
	public MethodBuilder appendMethod(int access, String name, String desc, String signature, String[] exceptions)
	{
		MethodContext ctx = Contexts.newMethodConstant(internalName, access, name, desc, signature, exceptions);
		String descriptor = name + desc;
		if(methods.putIfAbsent(descriptor, ctx) != null)
			throw new IllegalArgumentException("Method duplicated: " + descriptor);
		return new MethodBuilder(this, cw.visitMethod(access, name, desc, signature, exceptions));
	}
	
	public byte[] buildAsBytes()
	{
		return cw.toByteArray();
	}
	
	public Class<?> buildAsClass()
	{
		if(builded != null)
			return builded;
		return builded = Jam2Util.newClass(name, cw);
	}

	@Override
	public String getDebug()
	{
		return debug;
	}

	@Override
	public String getEnclosingClass()
	{
		return null;
	}

	@Override
	public String getEnclosingMethodName()
	{
		return null;
	}

	@Override
	public String getEnclosingMethodDescriptor()
	{
		return null;
	}

	@Override
	public String[] getInterfaces()
	{
		return Arrays.copyOf(interfaces, interfaces.length);
	}

	@Override
	public String getSignature()
	{
		return signature;
	}

	@Override
	public String getName()
	{
		return internalName;
	}

	@Override
	public int getModifier()
	{
		return access;
	}

	@Override
	public String getSource()
	{
		return source;
	}

	@Override
	public String getSuperClass()
	{
		return superclass;
	}

	@Override
	public int getVersion()
	{
		return version;
	}

	@Override
	public boolean containsField(String name)
	{
		return fields.containsKey(name);
	}

	@Override
	public boolean containsMethod(String descriptor)
	{
		return methods.containsKey(descriptor);
	}

	@Override
	public boolean hasMethod()
	{
		return !methods.isEmpty();
	}

	@Override
	public boolean hasField()
	{
		return !fields.isEmpty();
	}

	@Override
	public Collection<FieldContext> getFields()
	{
		return fields.values();
	}

	@Override
	public FieldContext getField(String name)
	{
		return fields.get(name);
	}

	@Override
	public Collection<MethodContext> getMethods()
	{
		return methods.values();
	}

	@Override
	public MethodContext getMethod(String descriptor)
	{
		return methods.get(descriptor);
	}

	public static Builder builder()
	{
		return new Builder();
	}

	final String name;

	private final int version;

	private final int access;

	private final String internalName;

	private final String[] interfaces;

	private final String signature;

	private final String superclass;
	
	Class<?> builded;

	private String source;

	private String debug;

	private final ClassWriter cw;

	private final InheritanceView view;

	private final Map<String, MethodContext> methods = new HashMap<>();

	private final Map<String, FieldContext> fields = new HashMap<>();

	public static interface MethodFilter
	{
		public boolean filter(MethodContext.Reflectable context, int depth);
	}

	public static class Builder
	{
		public Builder()
		{
			this.superName = "java.lang.Object";
			this.superclass = Object.class;
		}
		
		public Builder version(int version)
		{
			this.version = version;
			return this;
		}
		
		public Builder access(int access)
		{
			this.access = access;
			return this;
		}
		
		public Builder name(String name)
		{
			this.name = Jam2Util.fromCanonicalToInternalName(name);
			return this;
		}
		
		public Builder signature(String signature)
		{
			this.signature = signature;
			return this;
		}
		
		public Builder superclass(String superName)
		{
			this.superName = superName;
			this.superclass = null;
			return this;
		}

		public Builder extend(Class<?> superclass)
		{
			int modifiers = superclass.getModifiers();

			if(Modifier.isInterface(modifiers))
				throw new IllegalArgumentException("Cannot extend a interface");

			if(Modifier.isFinal(modifiers))
				throw new IllegalArgumentException("Cannot extend a final class");

			this.superName = superclass.getCanonicalName();
			this.superclass = superclass;
			return this;
		}

		public Builder implement(Class<?>... interfaces)
		{
			String[] canonicals = new String[interfaces.length];

			for(int i = 0; i < canonicals.length; i++)
			{
				Class<?> clazz = interfaces[i];
				int modifiers = clazz.getModifiers();

				if(!Modifier.isInterface(modifiers))
					throw new IllegalArgumentException("Cannot implement a non-interface class");

				canonicals[i] = clazz.getCanonicalName();
			}

			this.interfaces = canonicals;
			this.interfaceClasses = Arrays.copyOf(interfaces, interfaces.length);
			return this;
		}
		
		public Builder interfaces(String... interfaces)
		{
			String[] ifs = new String[interfaces.length];

			for(int i = 0; i < ifs.length; i++)
				ifs[i] = interfaces[i];

			this.interfaces = ifs;
			this.interfaceClasses = null;
			return this;
		}

		public ClassBuilder build()
		{
			return new ClassBuilder(version, access, name, signature, superName, interfaces, 0, /* TODO */ null);
		}

		int version = Version.getClassVersion();
		
		int access = ACC_PUBLIC;
		
		String name;
		
		String signature;
		
		String superName;
		
		String[] interfaces;

		Class<?> superclass;

		Class<?>[] interfaceClasses;
	}
}
