package org.kucro3.jam2.util.builder.structure;

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

    // TODO

    public T escape()
    {
        return upper;
    }

    final ImplementationView view;

    final T upper;
}
