package org.kucro3.jam2.util.builder.structure;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class BuilderStylizedExtensionView<T> {
    public BuilderStylizedExtensionView(T upper, ExtensionView view)
    {
        this.view = view;
        this.upper = upper;
    }

    public ExtensionView getView()
    {
        return view;
    }

    public BuilderStylizedExtensionView<T> foreach(BiConsumer<Integer, ExtensionView.Extension> consumer)
    {
        view.foreach(consumer);
        return this;
    }

    public BuilderStylizedExtensionView<T> foreach(BiConsumer<Integer, ExtensionView.Extension> consumer,
                                                   int travellingDepth)
    {
        view.foreach(consumer, travellingDepth);
        return this;
    }

    public BuilderStylizedExtensionView<T> get(int index, Consumer<ExtensionView.Extension> consumer)
    {
        consumer.accept(view.get(index));
        return this;
    }

    public BuilderStylizedExtension<BuilderStylizedExtensionView<T>> get(int index)
    {
        return new BuilderStylizedExtension<>(this, view.get(index));
    }

    public BuilderStylizedExtensionView<T> method(String descriptor,
                                                  Consumer<Optional<ExtensionView.ExtendedMethod>> consumer)
    {
        consumer.accept(view.getMethod(descriptor));
        return this;
    }

    public BuilderStylizedExtensionView<T> push(Class<?> instance, Consumer<ExtensionView.Extension> consumer)
    {
        consumer.accept(view.push(instance));
        return this;
    }

    public BuilderStylizedExtension<BuilderStylizedExtensionView<T>> push(Class<?> instance)
    {
        return new BuilderStylizedExtension<>(this, view.push(instance));
    }

    public T escape()
    {
        return upper;
    }

    final ExtensionView view;

    final T upper;

    public static class BuilderStylizedExtension<T>
    {
        public BuilderStylizedExtension(T upper, ExtensionView.Extension extension)
        {
            this.upper = upper;
            this.extension = extension;
        }

        public ExtensionView.Extension getExtension()
        {
            return extension;
        }

        public BuilderStylizedExtension<T> foreach(Consumer<ExtensionView.ExtendedMethod> consumer,
                                                   MethodFilter... filters)
        {
            extension.foreach(consumer, filters);
            return this;
        }

        public BuilderStylizedExtension<T> method(String descriptor,
                                                  Consumer<Optional<ExtensionView.ExtendedMethod>> consumer)
        {
            consumer.accept(extension.getMethod(descriptor));
            return this;
        }

        public BuilderStylizedExtension<T> type(Consumer<Class<?>> consumer)
        {
            consumer.accept(extension.getType());
            return this;
        }

        public T escape()
        {
            return upper;
        }

        final ExtensionView.Extension extension;

        final T upper;
    }
}
