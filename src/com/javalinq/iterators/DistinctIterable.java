package com.javalinq.iterators;


import com.javalinq.interfaces.QIterable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by sircodesalot on 14-5-30.
 */
public class DistinctIterable<T> implements QIterable<T> {
    private final Iterable<T> iterable;

    public DistinctIterable(Iterable<T> iterable) {
        this.iterable = iterable;
    }

    @Override
    public Iterator<T> iterator() {
        final Iterator<T> iterator = this.iterable.iterator();
        Set<T> seenItems = new HashSet<>();

        return new Iterator<T>() {
            private T nextItem;

            @Override
            public boolean hasNext() {
                while (iterator.hasNext()) {
                    nextItem = iterator.next();
                    if (seenItems.add(nextItem)) return true;
                }

                nextItem = null;
                return false;
            }

            @Override
            public T next() {
                return nextItem;
            }
        };
    }
}
