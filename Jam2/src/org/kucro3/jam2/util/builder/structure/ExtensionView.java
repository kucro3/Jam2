package org.kucro3.jam2.util.builder.structure;

import org.kucro3.jam2.util.Contexts;
import org.kucro3.jam2.util.Jam2Util;
import org.kucro3.jam2.util.MethodContext;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ExtensionView implements UniversalView<ExtensionView.Extension> {
    public static ExtensionView of(Class<?> instance)
    {
        return of(ClassStack.treeOf(instance));
    }

    static ExtensionView of(ClassStack stack)
    {
        ExtensionView view = new ExtensionView();

        stack.pop();
        while(!stack.empty())
            view.push(stack.pop());

        return view;
    }

    public Extension push(Class<?> type)
    {
        Extension prev = null, ret;
        if(extensions.size() > 0)
            prev = extensions.get(extensions.size() - 1);
        extensions.add(ret = new Extension(type, prev));
        return ret;
    }

    public Optional<ExtendedMethod> getMethod(String descriptor)
    {
        ExtendedMethod ret = null;
        for(int i = extensions.size() - 1; i >= 0; i--)
            if((ret = extensions.get(i).getMethod0(descriptor)) != null)
                break;
        return Optional.ofNullable(ret);
    }

    @Override
    public Extension get(int depth)
    {
        return extensions.get(extensions.size() - depth);
    }

    @Override
    public int depth()
    {
        return extensions.size();
    }

    @Override
    public void foreach(BiConsumer<Integer, Extension> consumer, int travellingDepth)
    {
        int dp = 1;
        for(int i = extensions.size(); (i > 0) && (dp <= travellingDepth); i--, dp++)
            consumer.accept(dp, extensions.get(i - 1));
    }

    final ArrayList<Extension> extensions = new ArrayList<>();

    public static final class Extension
    {
        Extension(Class<?> superclass, Extension prev)
        {
            this.superclass = superclass;
            this.prev = prev;

            this.initialize();
        }

        public Collection<ExtendedMethod> getMethods()
        {
            return Collections.unmodifiableCollection(methods.values());
        }

        public Class<?> getType()
        {
            return superclass;
        }

        public Optional<ExtendedMethod> getMethod(String descriptor)
        {
            return Optional.ofNullable(getMethod0(descriptor));
        }

        public void foreach(Consumer<ExtendedMethod> consumer)
        {
            getMethods().forEach(consumer);
        }

        ExtendedMethod getMethod0(String descriptor)
        {
            return methods.get(descriptor);
        }

        final void initialize()
        {
            Method[] methods = this.superclass.getDeclaredMethods();
            for(Method method : methods)
            {
                String descriptor = Jam2Util.toDescriptor(method);
                ExtendedMethod e = new ExtendedMethod(this, Contexts.newMethodConstant(method));

                Extension prev = this.prev;
                while(prev != null)
                {
                    ExtendedMethod overrided;
                    if((overrided = prev.methods.get(descriptor)) != null)
                    {
                        e.overrided = overrided;
                        break;
                    }
                    prev = prev.prev;
                }

                this.methods.put(descriptor, e);
            }
        }

        final Class<?> superclass;

        final Map<String, ExtendedMethod> methods = new HashMap<>();

        Extension prev;
    }

    public static final class ExtendedMethod
    {
        ExtendedMethod(Extension owner, MethodContext.Reflectable context)
        {
            this.owner = owner;
            this.context = context;
        }

        public MethodContext.Reflectable getContext()
        {
            return context;
        }

        public boolean isOverriding()
        {
            return getOverrided().isPresent();
        }

        public Optional<ExtendedMethod> getOverrided()
        {
            return Optional.ofNullable(overrided);
        }

        public Optional<Extension> getOwner()
        {
            return Optional.ofNullable(owner);
        }

        ExtendedMethod overrided;

        final MethodContext.Reflectable context;

        final Extension owner;
    }
}
