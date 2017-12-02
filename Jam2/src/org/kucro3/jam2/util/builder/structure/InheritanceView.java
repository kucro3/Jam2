package org.kucro3.jam2.util.builder.structure;

public class InheritanceView {
    public static InheritanceView of(Class<?> instance)
    {
        ClassStack stack = ClassStack.treeOf(instance);

        ExtensionView ev = ExtensionView.of(stack);
        stack.reset();
        ImplementationView iv = ImplementationView.of(stack, ev);

        return new InheritanceView(ev, iv);
    }

    public InheritanceView()
    {
        this.extensions = new ExtensionView();
        this.implementations = new ImplementationView(this.extensions);
    }

    InheritanceView(ExtensionView extensions, ImplementationView implementations)
    {
        this.extensions = extensions;
        this.implementations = implementations;
    }

    public int getDepth()
    {
        return Math.max(extensions.getDepth(), implementations.getDepth());
    }

    public ExtensionView getExtensions()
    {
        return extensions;
    }

    public ImplementationView getImplementaitions()
    {
        return implementations;
    }

    final ExtensionView extensions;

    final ImplementationView implementations;
}
