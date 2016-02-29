package fr.limsi.rorqual.core.utils;

/**
 * Created by christophe on 29/02/16.
 */
public class Timeit {

    long start;
    long end;

    public Timeit start() {
        start = System.currentTimeMillis();
        return this;
    }

    public Timeit stop() {
        end = System.currentTimeMillis();
        return this;
    }

    public long value() {
        return end - start;
    }
}
