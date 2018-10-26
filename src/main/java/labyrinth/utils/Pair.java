package labyrinth.utils;

import java.io.Serializable;

public class Pair<T, V> implements Serializable{
    public final T l;
    public final V r;

    public Pair(T l, V r) {

        this.l = l;
        this.r = r;
    }
}
