package org.kucro3.jam2.util.builder.structure;

import org.kucro3.jam2.util.Contexts;
import org.kucro3.jam2.util.Jam2Util;
import org.kucro3.jam2.util.MethodContext;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ImplementationView implements UniversalView<ImplementationView.Implementations> {
    public static ImplementationView of(Class<?> instance)
    {
        return of(instance, null);
    }

    public static ImplementationView of(Class<?> instance, ExtensionView extensions)
    {
        return of(ClassStack.treeOf(instance), extensions);
    }

    static ImplementationView of(ClassStack stack)
    {
        return of(stack, null);
    }

    static ImplementationView of(ClassStack stack, ExtensionView extensions)
    {
        ImplementationView view = new ImplementationView(extensions);

        Class<?> tmp;
        while(!stack.empty())
        {
            tmp = stack.pop();
            Implementations impls = view.push();
            for(Class<?> implemented : tmp.getInterfaces())
                impls.implement(implemented);
        }

        return view;
    }

    ImplementationView(ExtensionView extensions)
    {
        this.extensions = extensions;
    }

    @Override
    public Implementations get(int depth)
    {
        return implemented.get(depth - 1);
    }

    @Override
    public int getDepth()
    {
        return implemented.size();
    }

    @Override
    public void foreach(BiConsumer<Integer, Implementations> consumer, int travellingDepth)
    {
        int dp = 1;
        for(int i = implemented.size(); (i > 0) && (travellingDepth >= dp); i--, dp++)
            consumer.accept(dp, implemented.get(i - 1));
    }

    public Implementations push()
    {
        Implementations prev = null, ret;
        if(implemented.size() > 0)
            prev = implemented.get(implemented.size() - 1);
        implemented.add(ret = new Implementations(prev));
        return ret;
    }

    private final ArrayList<Implementations> implemented = new ArrayList<>();

    final ExtensionView extensions;

    public final class Implementations
    {
        Implementations(Implementations prev)
        {
            this.prev = prev;
        }

        public int foreach(Consumer<MethodContext.Reflectable> consumer)
        {
            int count = 0;

            for(Map.Entry<String, MethodContext.Reflectable> entry : implementedMethods.entrySet())
            {
                consumer.accept(entry.getValue());
                count++;
            }

            return count;
        }

        public boolean implement(Class<?> type)
        {
            if(!type.isInterface())
                return false;

            for(Class<?> superinterface : type.getInterfaces())
                implement(superinterface);

            for(Method method : type.getMethods())
                if(!Modifier.isStatic(method.getModifiers()))
                    implement(Contexts.newMethodConstant(method));

            return true;
        }

        void implement(MethodContext.Reflectable context)
        {
            String descriptor = Jam2Util.toDescriptor(context.getMethod());
            Method mthd = context.getMethod();

            Optional<ExtensionView.ExtendedMethod> optional;
            if(extensions != null)
                if((optional = extensions.getMethod(descriptor)).isPresent())
                {
                    ExtensionView.ExtendedMethod extended = optional.get();
                    if(extended.isOverriding())
                        return;
                    extended.overrided = new ExtensionView.ExtendedMethod(null, context);
                    return;
                }

            if(!mthd.isDefault())
                if(exists(descriptor) || existed(mthd.getDeclaringClass()))
                    return;

            implementedMethods.put(descriptor, context);
            implementedClasses.add(mthd.getDeclaringClass());
        }

        public Set<Class<?>> getLastImplemented()
        {
            return Collections.unmodifiableSet(implementedClasses);
        }

        public Set<Class<?>> getAllImplemented()
        {
            Set<Class<?>> set = new HashSet<>();
            computeAllImplemented(set);
            return Collections.unmodifiableSet(set);
        }

        public boolean isImplemented(Class<?> type)
        {
            return exists(type);
        }

        final void computeAllImplemented(Set<Class<?>> set)
        {
            set.addAll(implementedClasses);
            if(prev != null)
                prev.computeAllImplemented(set);
        }

        boolean existed(Class<?> implemenedClass)
        {
            return prev == null ? false : prev.exists(implemenedClass);
        }

        boolean exists(Class<?> implementedClass)
        {
            return implementedClasses.contains(implementedClass) || (prev != null ? prev.exists(implementedClass) : false);
        }

        boolean exists(String descriptor)
        {
            return implementedMethods.containsKey(descriptor) || (prev != null ? prev.exists(descriptor) : false);
        }

        final Map<String, MethodContext.Reflectable> implementedMethods = new HashMap<>();

        final Set<Class<?>> implementedClasses = new HashSet<>();

        Implementations prev;
    }
}
