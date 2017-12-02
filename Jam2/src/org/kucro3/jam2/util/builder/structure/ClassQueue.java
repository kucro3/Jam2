package org.kucro3.jam2.util.builder.structure;

import java.util.LinkedList;

class ClassStack extends LinkedList<Class<?>> {
    static ClassStack treeOf(Class<?> type)
    {
        ClassStack stack = new ClassStack();

        stack.push(type);
        while((type = type.getSuperclass()) != null)
            stack.push(type);

        return stack;
    }
}
