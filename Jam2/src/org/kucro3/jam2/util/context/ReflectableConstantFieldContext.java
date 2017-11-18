package org.kucro3.jam2.util.context;

import org.kucro3.jam2.util.FieldContext;

import java.lang.reflect.Field;

public class ReflectableConstantFieldContext extends ConstantFieldContext
        implements FieldContext.Reflectable {
    public ReflectableConstantFieldContext(String declaringClass,
                                           int modifier,
                                           String name,
                                           String descriptor,
                                           String signature,
                                           Object value,
                                           Field field)
    {
        super(declaringClass, modifier, name, descriptor, signature, value);
        this.field = field;
    }

    @Override
    public Field getField()
    {
        return field;
    }

    protected final Field field;
}
