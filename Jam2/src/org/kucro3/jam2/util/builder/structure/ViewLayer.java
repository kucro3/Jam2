package org.kucro3.jam2.util.builder.structure;

public interface ViewLayer {
    public default boolean formInterface()
    {
        return this instanceof ImplementationView.Implementations;
    }

    public default boolean fromSuperclass()
    {
        return this instanceof ExtensionView.Extension;
    }
}
