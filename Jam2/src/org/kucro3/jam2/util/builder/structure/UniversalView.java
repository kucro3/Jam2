package org.kucro3.jam2.util.builder.structure;

import java.util.function.BiConsumer;

public interface UniversalView<T> {
    public T get(int depth);

    public int getDepth();

    public default T peek()
    {
        return get(getDepth());
    }

    public void foreach(BiConsumer<Integer, T> consumer, int travellingDepth);

    public default void foreach(BiConsumer<Integer, T> consumer)
    {
        foreach(consumer, getDepth());
    }
}
