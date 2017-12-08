package org.kucro3.jam2.util.builder.structure;

import org.kucro3.jam2.util.MethodContext;

import java.util.function.BiConsumer;

public interface UniversalView<T> {
    public T get(int depth);

    public int depth();

    public default T peek()
    {
        return get(0);
    }

    public void foreach(BiConsumer<Integer, T> consumer, int travellingDepth);

    public default void foreach(BiConsumer<Integer, T> consumer)
    {
        foreach(consumer, depth());
    }

    public boolean contains(MethodContext context);
}
