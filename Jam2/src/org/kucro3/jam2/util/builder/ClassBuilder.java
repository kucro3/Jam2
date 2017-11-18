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
		this(version, access, name, signature, superName, interfaces, flags, new InheritanceView(name, superName, Arrays.copyOf(interfaces, interfaces.length)));
	}

	private ClassBuilder(int version, int access, String name, String signature, String superName, String[] interfaces, int flags, InheritanceView view)
	{
		this.cw = new ClassWriter(flags);
		this.name = name;
		this.cw.visit(version,
				access,
				Jam2Util.fromCanonicalToInternalName(name),
				signature,
				Jam2Util.fromCanonicalToInternalName(superName),
				Jam2Util.fromCanonicalsToInternalNames(interfaces));
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
		return builded = Jam2Util.newClass(name, cw);
	}

	@Override
	public String getDebug() {
		return null;
	}

	@Override
	public String getEnclosingClass() {
		return null;
	}

	@Override
	public String getEnclosingMethodName() {
		return null;
	}

	@Override
	public String getEnclosingMethodDescriptor() {
		return null;
	}

	@Override
	public String[] getInterfaces() {
		return new String[0];
	}

	@Override
	public String getSignature() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public int getModifier() {
		return 0;
	}

	@Override
	public String getSource() {
		return null;
	}

	@Override
	public String getSuperClass() {
		return null;
	}

	@Override
	public int getVersion() {
		return 0;
	}

	@Override
	public boolean containsField(String name) {
		return false;
	}

	@Override
	public boolean containsMethod(String descriptor) {
		return false;
	}

	@Override
	public boolean hasMethod() {
		return false;
	}

	@Override
	public boolean hasField() {
		return false;
	}

	@Override
	public Collection<FieldContext> getFields() {
		return null;
	}

	@Override
	public FieldContext getField(String name) {
		return null;
	}

	@Override
	public Collection<MethodContext> getMethods() {
		return null;
	}

	@Override
	public MethodContext getMethod(String descriptor) {
		return null;
	}
	
	public static Builder builder()
	{
		return new Builder();
	}

	String name;
	
	Class<?> builded;
	
	private final ClassWriter cw;

	private InheritanceView view;

	private final Map<String, MethodContext> method = new HashMap<>();

	private final Map<String, FieldContext> fields = new HashMap<>();

	private final Map<Class<?>, Collection<MethodContext>> superMethods = new HashMap<>();

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
			this.update();
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
			this.update();
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
			this.update();
			return this;
		}
		
		public Builder interfaces(String... interfaces)
		{
			String[] ifs = new String[interfaces.length];

			for(int i = 0; i < ifs.length; i++)
				ifs[i] = interfaces[i];

			this.interfaces = ifs;
			this.interfaceClasses = null;
			this.update();
			return this;
		}

		public Builder computeInhertianceView()
		{
			(view = new InheritanceView(name, superName, interfaces)).computeAll();
			return this;
		}

		public InheritanceView getInheritanceView()
		{
			if(view == null)
				computeInhertianceView();

			return view;
		}
		
		public ClassBuilder build()
		{
			return new ClassBuilder(version, access, name, signature, superName, interfaces, 0, getInheritanceView());
		}

		void update()
		{
			this.view = null;
		}

		int version = Version.getClassVersion();
		
		int access = ACC_PUBLIC;
		
		String name;
		
		String signature;
		
		String superName;
		
		String[] interfaces;

		Class<?> superclass;

		Class<?>[] interfaceClasses;

		InheritanceView view;
	}
}
