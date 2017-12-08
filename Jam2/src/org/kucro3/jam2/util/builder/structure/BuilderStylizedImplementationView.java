package org.kucro3.jam2.util.builder.structure;

import org.kucro3.jam2.util.MethodContext;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class BuilderStylizedImplementationView<T> {
    public BuilderStylizedImplementationView(T upper, ImplementationView view)
    {
        this.view = view;
        this.upper = upper;
    }

    public ImplementationView getView()
    {
        return view;
    }

    public BuilderStylizedImplementationView<T> foreach(BiConsumer<Integer, ImplementationView.Implementations> consumer,
                                                        final int travellingDepth)
    {
        view.foreach(consumer, travellingDepth);
        return this;
    }

    public BuilderStylizedImplementationView<T> foreach(BiConsumer<Integer, ImplementationView.Implementations> consumer,
                                                        IntSupplier travellingDepth)
    {
        return foreach(consumer, travellingDepth.getAsInt());
    }

    public BuilderStylizedImplementationView<T> foreach(BiConsumer<Integer, ImplementationView.Implementations> consumer,
                                                        Supplier<Integer> travellingDepth)
    {
        return foreach(consumer, travellingDepth.get());
    }

    public BuilderStylizedImplementationView<T> foreach(BiConsumer<Integer, ImplementationView.Implementations> consumer)
    {
        view.foreach(consumer);
        return this;
    }

    public BuilderStylizedImplementationView<T> get(int depth,
                                                    Consumer<ImplementationView.Implementations> consumer)
    {
        consumer.accept(view.get(depth));
        return this;
    }

    public BuilderStylizedImplementationView<T> get(IntSupplier depth,
                                                    Consumer<ImplementationView.Implementations> consumer)
    {
        return get(depth.getAsInt(), consumer);
    }

    public BuilderStylizedImplementationView<T> get(Supplier<Integer> depth,
                                                    Consumer<ImplementationView.Implementations> consumer)
    {
        return get(depth.get(), consumer);
    }

    public BuilderStylizedImplementationView<T> push(Consumer<ImplementationView.Implementations> consumer)
    {
        consumer.accept(view.push());
        return this;
    }

    public BuilderStylizedImplementations<BuilderStylizedImplementationView<T>> push()
    {
        return new BuilderStylizedImplementations<>(this, view.push());
    }

    public T escape()
    {
        return upper;
    }

    final ImplementationView view;

    final T upper;

    public static class BuilderStylizedImplementations<T>
    {
        public BuilderStylizedImplementations(T upper, ImplementationView.Implementations impls)
        {
            this.upper = upper;
            this.impls = impls;
        }

        public ImplementationView.Implementations getImplementations()
        {
            return impls;
        }

        public BuilderStylizedImplementations<T> foreach(Consumer<MethodContext.Reflectable> consumer,
                                                         MethodFilter... filters)
        {
            impls.foreach(consumer, filters);
            return this;
        }

        public BuilderStylizedImplementations<T> allImplemented(Consumer<Set<Class<?>>> consumer)
        {
            consumer.accept(impls.getAllImplemented());
            return this;
        }

        public BuilderStylizedImplementations<T> lastImplemented(Consumer<Set<Class<?>>> consumer)
        {
            consumer.accept(impls.getLastImplemented());
            return this;
        }

        public BuilderStylizedImplementations<T> implementInterfaces(Class<?> type)
        {
            impls.implementInterfaces(type);
            return this;
        }

        public BuilderStylizedImplementations<T> implementInterfaces(Supplier<Class<?>> type)
        {
            return implementInterfaces(type.get());
        }

        public BuilderStylizedImplementations<T> implement(Class<?> type)
        {
            impls.implement(type);
            return this;
        }

        public BuilderStylizedImplementations<T> implement(Supplier<Class<?>> type)
        {
            return implement(type.get());
        }

        public BuilderStylizedImplementations<T> isImplemented(Class<?> type, Consumer<Boolean> consumer)
        {
            consumer.accept(impls.isImplemented(type));
            return this;
        }

        public T escape()
        {
            return upper;
        }

        final ImplementationView.Implementations impls;

        final T upper;
    }
}
