package labyrinth.utils;

import java.io.Serializable;

public class Pair<T, V> implements Serializable{
    public final T l;
    public final V r;

    public Pair(T l, V r) {

        this.l = l;
        this.r = r;
    }

    @Override
    public boolean equals(Object other){
        if (this == other) {
            return true;
        }
        if (!(other instanceof Pair)) {
            return false;
        }

        Pair otherPair = (Pair) other;
        return this.l.equals(otherPair.l) && this.r.equals(otherPair.r);
    }

    @Override
    public int hashCode() {
        return this.l.hashCode() ^ this.r.hashCode();
    }
}
