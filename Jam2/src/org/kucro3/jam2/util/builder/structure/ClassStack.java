package org.kucro3.jam2.util.builder.structure;

import java.util.ArrayList;
import java.util.NoSuchElementException;

class ClassStack extends ArrayList<Class<?>> {
    static ClassStack treeOf(Class<?> type)
    {
        ClassStack stack = new ClassStack();

        stack.add(type);
        while((type = type.getSuperclass()) != null)
            stack.add(type);

        return stack;
    }

    public Class<?> pop()
    {
        if(pointer < 0)
            reset();

        if(pointer == 0)
            throw new NoSuchElementException();

        return get(--pointer);
    }

    public void reset()
    {
        pointer = size();
    }

    public boolean empty()
    {
        return pointer == 0;
    }

    int pointer = -1;
}
