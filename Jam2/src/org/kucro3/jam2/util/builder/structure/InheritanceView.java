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

    public int getDepth()
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

    public void append(Class<?> instance)
    {
        Objects.requireNonNull(instance);

        this.implementations.push().implementInterfaces(instance);

        if(this.current != null)
            this.extensions.push(this.current);

        this.current = instance;
    }

    public void foreach(BiConsumer<Integer, MethodContext.Reflectable> consumer)
    {
        foreach(consumer, getDepth());
    }

    public void foreach(BiConsumer<Integer, MethodContext.Reflectable> consumer, int travellingDepth)
    {
        if(travellingDepth < 1 || getDepth() < 1)
            return;

        if(current == null)
            if(extensions.depth() > 0)
                extensions.get(1).foreach((m) -> consumer.accept(1, m.getContext()));

        if(implementations.depth() > 0)
            implementations.get(1).foreach((m) -> consumer.accept(1, m));

        for(int i = 2; i <= travellingDepth; i++)
        {
            final int dp = i;

            if(extensions.depth() >= dp)
                extensions.get(dp).foreach((m) -> consumer.accept(dp, m.getContext()));

            if(implementations.depth() >= dp)
                implementations.get(dp).foreach((m) -> consumer.accept(dp, m));
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
