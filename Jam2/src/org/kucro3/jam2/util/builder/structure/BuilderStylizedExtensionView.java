package org.kucro3.jam2.util.builder.structure;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

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

    public BuilderStylizedExtensionView<T> foreach(BiConsumer<Integer, ExtensionView.Extension> consumer,
                                                   IntSupplier supplier)
    {
        return foreach(consumer, supplier.getAsInt());
    }

    public BuilderStylizedExtensionView<T> foreach(BiConsumer<Integer, ExtensionView.Extension> consumer,
                                                   Supplier<Integer> supplier)
    {
        return foreach(consumer, supplier.get());
    }

    public BuilderStylizedExtensionView<T> get(int index,
                                               Consumer<ExtensionView.Extension> consumer)
    {
        consumer.accept(view.get(index));
        return this;
    }

    public BuilderStylizedExtensionView<T> get(IntSupplier index,
                                               Consumer<ExtensionView.Extension> consumer)
    {
        return get(index.getAsInt(), consumer);
    }

    public BuilderStylizedExtensionView<T> get(Supplier<Integer> index,
                                               Consumer<ExtensionView.Extension> consumer)
    {
        return get(index.get(), consumer);
    }

    public BuilderStylizedExtension<BuilderStylizedExtensionView<T>> get(int index)
    {
        return new BuilderStylizedExtension<>(this, view.get(index));
    }

    public BuilderStylizedExtension<BuilderStylizedExtensionView<T>> get(IntSupplier index)
    {
        return get(index.getAsInt());
    }

    public BuilderStylizedExtension<BuilderStylizedExtensionView<T>> get(Supplier<Integer> index)
    {
        return get(index.get());
    }

    public BuilderStylizedExtensionView<T> method(String descriptor,
                                                  Consumer<Optional<ExtensionView.ExtendedMethod>> consumer)
    {
        consumer.accept(view.getMethod(descriptor));
        return this;
    }

    public BuilderStylizedExtensionView<T> method(Supplier<String> descriptor,
                                                  Consumer<Optional<ExtensionView.ExtendedMethod>> consumer)
    {
        return method(descriptor.get(), consumer);
    }

    public BuilderStylizedExtensionView<T> push(Class<?> instance, Consumer<ExtensionView.Extension> consumer)
    {
        consumer.accept(view.push(instance));
        return this;
    }

    public BuilderStylizedExtensionView<T> push(Supplier<Class<?>> instance,
                                                Consumer<ExtensionView.Extension> consumer)
    {
        return push(instance.get(), consumer);
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

        public BuilderStylizedExtension<T> method(Supplier<String> descriptor,
                                                  Consumer<Optional<ExtensionView.ExtendedMethod>> consumer)
        {
            return method(descriptor.get(), consumer);
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
