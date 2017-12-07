package org.kucro3.jam2.util.builder.structure;

import org.kucro3.jam2.util.MethodContext;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

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
