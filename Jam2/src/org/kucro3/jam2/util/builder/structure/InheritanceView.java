package org.kucro3.jam2.util.builder.structure;

import org.kucro3.jam2.util.MethodContext;

import java.util.Objects;
import java.util.function.BiConsumer;

public class InheritanceView {
    public static InheritanceView of(Class<?> instance)
    {
        ClassStack stack = ClassStack.treeOf(instance);

        ExtensionView ev = ExtensionView.of(stack);
        stack.reset();
        ImplementationView iv = ImplementationView.of(stack, ev);

        return new InheritanceView(ev, iv);
    }

    public InheritanceView()
    {
        this.extensions = new ExtensionView();
        this.implementations = new ImplementationView(this.extensions);

        this.implementations.push().implement(this.current = Object.class);
    }

    InheritanceView(ExtensionView extensions, ImplementationView implementations)
    {
        this.extensions = extensions;
        this.implementations = implementations;
    }

    public int depth()
    {
        return Math.max(extensions.depth(), implementations.depth());
    }

    public Class<?> peek()
    {
        return current;
    }

    public boolean push()
    {
        if(current == null)
            return false;

        this.extensions.push(this.current);
        current = null;

        return true;
    }

    public boolean contains(MethodContext context)
    {
        return extensions.contains(context) || implementations.contains(context);
    }

    public void append(Class<?> instance)
    {
        Objects.requireNonNull(instance);

        this.implementations.push().implementInterfaces(instance);

        if(this.current != null)
            this.extensions.push(this.current);

        this.current = instance;
    }

    public void foreach(BiConsumer<Integer, MethodContext.Reflectable> consumer,
                        final MethodFilter... filters)
    {
        foreach(consumer, depth(), filters);
    }

    public void foreach(BiConsumer<Integer, MethodContext.Reflectable> consumer,
                        int travellingDepth,
                        final MethodFilter... filters)
    {
        int i;

        if(travellingDepth < 1 || (i = depth()) < 1)
            return;

        if(current == null)
            if(extensions.depth() >= i)
                extensions.get(i).foreach(
                        (m) -> consumer.accept(1, m.getContext()),
                        (src, ctx, fdp) -> MethodFilter.match(src, ctx, 1, filters));

        if(implementations.depth() >= i)
            implementations.get(i).foreach(
                    (m) -> consumer.accept(1, m),
                    (src, ctx, fdp) -> MethodFilter.match(src, ctx, 1, filters));

        i--;

        for(int j = 2; i > 0 && j < travellingDepth; i--, j++)
        {
            final int dp = j;

            if(extensions.depth() >= i)
                extensions.get(dp).foreach(
                        (m) -> consumer.accept(dp, m.getContext()),
                        (src, ctx, fdp) -> MethodFilter.match(src, ctx, dp, filters));

            if(implementations.depth() >= i)
                implementations.get(dp).foreach(
                        (m) -> consumer.accept(dp, m),
                        (src, ctx, fdp) -> MethodFilter.match(src, ctx, dp, filters));
        }
    }

    Class<?> current;

    public ExtensionView getExtensions()
    {
        return extensions;
    }

    public ImplementationView getImplementaitions()
    {
        return implementations;
    }

    final ExtensionView extensions;

    final ImplementationView implementations;
}
