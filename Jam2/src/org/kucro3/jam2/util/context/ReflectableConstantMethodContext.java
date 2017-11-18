package org.kucro3.jam2.util.context;

import org.kucro3.jam2.util.MethodContext;

import java.lang.reflect.Method;

public class ReflectableConstantMethodContext extends ConstantMethodContext
        implements MethodContext.Reflectable {
    public ReflectableConstantMethodContext(String declaringClass,
                                            int modifier,
                                            String name,
                                            String descriptor,
                                            String signature,
                                            String[] exceptions,
                                            Method method)
    {
        super(declaringClass, modifier, name, descriptor, signature, exceptions);
        this.method = method;
    }

    @Override
    public Method getMethod()
    {
        return method;
    }

    protected final Method method;
}
