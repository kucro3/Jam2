package org.kucro3.util;

public class Pair<T1, T2> {
    public Pair(T1 e1, T2 e2)
    {
        this.e1 = e1;
        this.e2 = e2;
    }

    public T1 first()
    {
        return e1;
    }

    public T2 second()
    {
        return e2;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj == null)
            return false;

        if(!(obj instanceof Pair))
            return false;

        Pair<?, ?> p = (Pair) obj;

        if(e1 == null)
            if(p.e1 != null)
                return false;
            else if(!e1.equals(p.e1))
                return false;
            else;

        if(e2 == null)
            if(p.e2 != null)
                return false;
            else if(!e2.equals(p.e2))
                return false;
            else;

        return true;
    }

    @Override
    public int hashCode()
    {
        return (e1 == null ? 0 : e1.hashCode() * 13) + (e2 == null ? 0 : e2.hashCode());
    }

    final T1 e1;

    final T2 e2;
}
