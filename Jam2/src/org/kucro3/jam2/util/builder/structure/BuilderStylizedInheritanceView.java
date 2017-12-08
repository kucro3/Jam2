package org.kucro3.jam2.util.builder.structure;

import org.kucro3.jam2.util.MethodContext;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class BuilderStylizedInheritanceView<T> {
    public BuilderStylizedInheritanceView(T upper, InheritanceView view)
    {
        this.upper = upper;
        this.view = view;
    }

    public BuilderStylizedInheritanceView<T> append(Class<?> instance)
    {
        view.append(instance);
        return this;
    }

    public BuilderStylizedInheritanceView<T> append(Supplier<Class<?>> instance)
    {
        return append(instance.get());
    }

    public BuilderStylizedInheritanceView<T> push()
    {
        view.push();
        return this;
    }

    public BuilderStylizedInheritanceView<T> peek(Consumer<Class<?>> consumer)
    {
        consumer.accept(view.peek());
        return this;
    }

    public BuilderStylizedInheritanceView<T> foreach(BiConsumer<Integer, MethodContext.Reflectable> consumer,
                                                     int travellingDepth,
                                                     final MethodFilter... filters)
    {
        view.foreach(consumer, travellingDepth, filters);
        return this;
    }

    public BuilderStylizedInheritanceView<T> foreach(BiConsumer<Integer, MethodContext.Reflectable> consumer,
                                                     IntSupplier travellingDepth,
                                                     MethodFilter... filters)
    {
        return foreach(consumer, travellingDepth.getAsInt(), filters);
    }

    public BuilderStylizedInheritanceView<T> foreach(BiConsumer<Integer, MethodContext.Reflectable> consumer,
                                                     Supplier<Integer> travellingDepth,
                                                     MethodFilter... filters)
    {
        return foreach(consumer, travellingDepth.get(), filters);
    }

    public BuilderStylizedInheritanceView<T> foreach(BiConsumer<Integer, MethodContext.Reflectable> consumer,
                                                     final MethodFilter... filters)
    {
        view.foreach(consumer, filters);
        return this;
    }

    public BuilderStylizedInheritanceView<T> extensions(Consumer<ExtensionView> consumer)
    {
        consumer.accept(view.getExtensions());
        return this;
    }

    public BuilderStylizedExtensionView<BuilderStylizedInheritanceView<T>> extensions()
    {
        return new BuilderStylizedExtensionView<>(this, view.getExtensions());
    }

    public BuilderStylizedInheritanceView<T> implementations(Consumer<ImplementationView> consumer)
    {
        consumer.accept(view.getImplementaitions());
        return this;
    }

    public BuilderStylizedImplementationView<BuilderStylizedInheritanceView<T>> implementations()
    {
        return new BuilderStylizedImplementationView<>(this, view.getImplementaitions());
    }

    public InheritanceView getView()
    {
        return view;
    }

    public T escape()
    {
        return upper;
    }

    final InheritanceView view;

    final T upper;
}
