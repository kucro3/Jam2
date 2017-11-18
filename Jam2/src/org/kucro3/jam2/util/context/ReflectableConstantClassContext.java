package org.kucro3.jam2.util.context;

import org.kucro3.jam2.util.ClassContext;
import org.kucro3.jam2.util.ClassMemberMap;
import org.kucro3.jam2.util.FieldContext;
import org.kucro3.jam2.util.MethodContext;

public class ReflectableConstantClassContext extends ConstantClassContext
        implements ClassContext.Reflectable {
    public ReflectableConstantClassContext(String name,
                                           Class<?> clz)
    {
        super(name);
        this.clz = clz;
    }

    public ReflectableConstantClassContext(String name,
                                           ClassMemberMap<MethodContext, FieldContext> map,
                                           Class<?> clz)
    {
        super(name, map);
        this.clz = clz;
    }

    public ReflectableConstantClassContext(int version,
                                           int modifier,
                                           String name,
                                           String signature,
                                           String superName,
                                           String[] interfaces,
                                           Class<?> clz)
    {
        super(version, modifier, name, signature, superName, interfaces);
        this.clz = clz;
    }

    public ReflectableConstantClassContext(int version,
                                           int modifier,
                                           String name,
                                           String signature,
                                           String superName,
                                           String[] interfaces,
                                           String enclosingClass,
                                           Class<?> clz)
    {
        super(version, modifier, name, signature, superName, interfaces, enclosingClass);
        this.clz = clz;
    }

    public ReflectableConstantClassContext(int version,
                                           int modifier,
                                           String name,
                                           String signature,
                                           String superName,
                                           String[] interfaces,
                                           String enclosingClass,
                                           String enclosingMethodName,
                                           String enclosingMethodDescriptor,
                                           Class<?> clz) {
        super(version, modifier, name, signature, superName, interfaces, enclosingClass, enclosingMethodName, enclosingMethodDescriptor);
        this.clz = clz;
    }

    public ReflectableConstantClassContext(int version,
                                           int modifier,
                                           String name,
                                           String signature,
                                           String superName,
                                           String[] interfaces,
                                           String enclosingClass,
                                           String enclosingMethodName,
                                           String enclosingMethodDescriptor,
                                           String source,
                                           String debug,
                                           Class<?> clz) {
        super(version, modifier, name, signature, superName, interfaces, enclosingClass, enclosingMethodName, enclosingMethodDescriptor, source, debug);
        this.clz = clz;
    }

    @Override
    public Class<?> getTypeClass()
    {
        return clz;
    }

    protected final Class<?> clz;
}
