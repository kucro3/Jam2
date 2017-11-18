package org.kucro3.jam2.util.builder;

import org.kucro3.jam2.util.ClassContext;
import org.kucro3.jam2.util.Jam2Util;
import org.kucro3.jam2.util.MethodContext;
import org.kucro3.jam2.util.Version;
import org.kucro3.jam2.util.builder.AnnotationBuilder.ClassAnnotationBuilder;
import org.kucro3.jam2.util.builder.structure.InheritanceView;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.TypePath;

import java.lang.reflect.Modifier;
import java.util.*;

public class ClassBuilder implements Opcodes {
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
		this.cw = new ClassWriter(flags);
		this.cw.visit(version, access, this.internalName = name, signature, superName, interfaces);
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
	
	public ClassBuilder appendInnerClass(String name, String outerName, String innerName, int access)
	{
		cw.visitInnerClass(name, outerName, innerName, access);
		return this;
	}
	
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
		return new FieldBuilder(this, cw.visitField(access, name, desc, signature, value));
	}
	
	public MethodBuilder appendMethod(int access, String name, String desc, String signature, String[] exceptions)
	{
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
		return builded = Jam2Util.newClass(internalName.replace('/', '.'), cw);
	}
	
	public static Builder builder()
	{
		return new Builder();
	}
	
	String internalName;
	
	Class<?> builded;
	
	private final ClassWriter cw;

	private InheritanceView superInheritanceView;

	private final Map<Class<?>, Collection<MethodContext>> methods = new HashMap<>();

	public static class Builder
	{
		public Builder()
		{
			this.superName = "java/lang/Object";
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
			this.superName = Jam2Util.fromCanonicalToInternalName(superName);
			return this;
		}

		public Builder extend(Class<?> superclass)
		{
			int modifiers = superclass.getModifiers();

			if(Modifier.isInterface(modifiers))
				throw new IllegalArgumentException("Cannot extend a interface");

			if(Modifier.isFinal(modifiers))
				throw new IllegalArgumentException("Cannot extend a final class");

			return superclass(superclass.getCanonicalName());
		}

		public Builder implement(Class<?>... interfaces)
		{
			String[] internals = new String[interfaces.length];

			for(int i = 0; i < internals.length; i++)
			{
				Class<?> clazz = interfaces[i];
				int modifiers = clazz.getModifiers();

				if(!Modifier.isInterface(modifiers))
					throw new IllegalArgumentException("Cannot implement a non-interface class");

				internals[i] = clazz.getCanonicalName();
			}

			return interfaces(internals);
		}
		
		public Builder interfaces(String... interfaces)
		{
			String[] ifs = new String[interfaces.length];

			for(int i = 0; i < ifs.length; i++)
				ifs[i] = Jam2Util.fromCanonicalToInternalName(interfaces[i]);

			this.interfaces = ifs;
			return this;
		}
		
		public ClassBuilder build()
		{
			return new ClassBuilder(version, access, name, signature, superName, interfaces);
		}
		
		int version = Version.getClassVersion();
		
		int access = ACC_PUBLIC;
		
		String name;
		
		String signature;
		
		String superName;
		
		String[] interfaces;
	}
}
