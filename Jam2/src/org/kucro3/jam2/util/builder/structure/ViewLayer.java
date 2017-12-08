package org.kucro3.jam2.util.builder.structure;

import org.kucro3.jam2.util.MethodContext;

public interface ViewLayer {
    public default boolean formInterface()
    {
        return this instanceof ImplementationView.Implementations;
    }

    public default boolean fromSuperclass()
    {
        return this instanceof ExtensionView.Extension;
    }

    public boolean contains(MethodContext context);
}
