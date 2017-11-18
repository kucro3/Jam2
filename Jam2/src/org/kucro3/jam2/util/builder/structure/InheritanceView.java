package org.kucro3.jam2.util.builder.structure;

import org.kucro3.jam2.util.Jam2Util;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class InheritanceView {
    public InheritanceView(Class<?> clazz, Class<?> superclass, Class<?>[] interfaces)
    {
        this.clazz = Objects.requireNonNull(clazz);
        this.interfaceclasses = interfaces == null ? EMPTY_INTERFACES : Arrays.copyOf(interfaces, interfaces.length);
        this.superclass = superclass;

        this.name = clazz.getCanonicalName();
        this.interfaces = Jam2Util.toCanonicalNames(interfaces);
        this.supertype = superclass == null ? null : superclass.getCanonicalName();
    }

    public InheritanceView(String clazz, String superclass, String[] interfaces)
    {
        this.name = Objects.requireNonNull(clazz);
        this.supertype = Objects.requireNonNull(superclass);
        this.interfaces = Arrays.copyOf(interfaces, interfaces.length);
    }

    public static InheritanceView of(Class<?> type)
    {
        return new InheritanceView(type, type.getSuperclass(), type.getInterfaces());
    }

    public static InheritanceView all(Class<?> type)
    {
        InheritanceView iv = of(type);
        iv.computeAll();
        return iv;
    }

    public void computeAll()
    {
        InheritanceView iv = this;
        while((iv = iv.tryGetSuperView().orElse(null)) != null);
    }

    public Optional<Class<?>> tryGetTypeClass()
    {
        if(clazz == null)
        {
            Optional<Class<?>> optional = Jam2Util.tryFromCanoncialToClass(name);
            if(optional.isPresent())
                clazz = optional.get();
            return optional;
        }
        else
            return Optional.of(clazz);
    }

    public Optional<Class<?>> tryGetSuperClass()
    {
        if(superclass == null)
        {
            if(superclass == null)
                return Optional.empty();

            Optional<Class<?>> optional = Jam2Util.tryFromCanoncialToClass(supertype);
            if(optional.isPresent())
                superclass = optional.get();
            return optional;
        }
        else
            return Optional.of(superclass);
    }

    public Optional<Class<?>[]> tryGetInterfaceClasses()
    {
        if(interfaceclasses == null)
        {
            Optional<Class<?>[]> optional = Jam2Util.tryFromCanoncialsToClasses(interfaces);
            if(optional.isPresent())
                interfaceclasses = optional.get();
            return optional;
        }
        else
            return Optional.of(interfaceclasses);
    }

    public Optional<InheritanceView> tryGetSuperView()
    {
        if(next == null)
        {
            tryGetSuperClass();

            if(superclass == null)
                return Optional.empty();

            if(superclass == Object.class)
                return Optional.of(next = new ObjectInheritanceView());

            return Optional.of(next = of(superclass));
        }
        else
            return Optional.of(next);
    }

    public String[] getInterfaces()
    {
        return Arrays.copyOf(interfaces, interfaces.length);
    }

    public String getSuperType()
    {
        return supertype;
    }

    public String getType()
    {
        return name;
    }

    private final String[] interfaces;

    private final String supertype;

    private final String name;

    private Class<?>[] interfaceclasses;

    private Class<?> superclass;

    private Class<?> clazz;

    InheritanceView next;

    private static final Class<?>[] EMPTY_INTERFACES = new Class<?>[0];

    private static class ObjectInheritanceView extends InheritanceView
    {
        public ObjectInheritanceView()
        {
            super(Object.class, null, null);
        }

        @Override
        public Optional<InheritanceView> tryGetSuperView()
        {
            return Optional.empty();
        }
    }
}
