package com.javalinq.iterators;

import com.javalinq.interfaces.QIterable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Function;

/**
 * Created by sircodesalot on 14-5-30.
 */
public class DistinctIterableOnProperty<T, U> implements QIterable<T> {
    private final Iterable<T> iterable;
    private final Function<T, U> onProperty;
    public DistinctIterableOnProperty(Iterable<T> iterable, Function<T, U> onProperty) {
        this.iterable = iterable;
        this.onProperty = onProperty;
    }

    @Override
    public Iterator<T> iterator() {
        return new DistinctIteratorOnProperty(iterable, onProperty);
    }

    private class DistinctIteratorOnProperty implements Iterator<T> {
        private final Set<U> seenItems = new HashSet<>();
        private final Iterator<T> iterator;
        private final Function<T, U> onProperty;
        private T nextItem;

        public DistinctIteratorOnProperty(Iterable<T> iterable, Function<T, U> onProperty) {
            this.iterator = iterable.iterator();
            this.onProperty = onProperty;
        }

        @Override
        public boolean hasNext() {
            while (this.iterator.hasNext()) {
                this.nextItem = this.iterator.next();
                U nextItemProperty = onProperty.apply(nextItem);

                if (this.seenItems.add(nextItemProperty)) return true;
            }

            nextItem = null;
            return false;
        }

        @Override
        public T next() {
            return this.nextItem;
        }
    }
}
