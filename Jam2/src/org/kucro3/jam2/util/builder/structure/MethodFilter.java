package org.kucro3.jam2.util.builder.structure;

import org.kucro3.jam2.util.MethodContext;

public interface MethodFilter {
    public boolean filter(ViewLayer from, MethodContext.Reflectable context, int depth);

    public static boolean match(ViewLayer from, MethodContext.Reflectable context, int depth, MethodFilter... filters)
    {
        for(MethodFilter filter : filters)
            if(!filter.filter(from, context, depth))
                return false;

        return true;
    }
}
