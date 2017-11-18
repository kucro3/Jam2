package org.kucro3.jam2.util.context;

import org.kucro3.jam2.util.MethodContext;

import java.lang.reflect.Constructor;

public class ReflectableConstantConstructorContext extends ConstantMethodContext
        implements MethodContext.ReflectableConstructor {
    public ReflectableConstantConstructorContext(String declaringClass,
                                                 int modifier,
                                                 String name,
                                                 String descriptor,
                                                 String signature,
                                                 String[] exceptions,
                                                 Constructor<?> constructor) {
        super(declaringClass, modifier, name, descriptor, signature, exceptions);
        this.constructor = constructor;
    }

    @Override
    public Constructor<?> getConstructor()
    {
        return constructor;
    }

    protected final Constructor<?> constructor;
}
