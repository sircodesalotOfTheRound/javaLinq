package com.javalinq.interfaces;

import com.javalinq.exceptions.QueryException;
import com.javalinq.implementations.QList;
import com.javalinq.implementations.QSet;
import com.javalinq.iterators.DistinctIterable;
import com.javalinq.iterators.DistinctIterableOnProperty;
import com.javalinq.iterators.MapIterable;
import com.javalinq.iterators.WhereIterable;
import com.javalinq.tools.Partition;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by sircodesalot on 14-5-30.
 */
public interface QIterable<T> extends Iterable<T> {
    default public QIterable<T> where(Predicate<T> predicate) {
        return new WhereIterable<T>(this, predicate);
    }

    default public <U> QIterable<U> map(Function<T, U> projection) {
        return new MapIterable<T, U>(this, projection);
    }

    default public QIterable<T> distinct() {
        return new DistinctIterable<>(this);
    }

    default public <U> QIterable<T> distinct(Function<T, U> onProperty) {
        return new DistinctIterableOnProperty<>(this, onProperty);
    }

    default public T first() {
        Iterator<T> iterator = this.iterator();
        if (iterator.hasNext()) return iterator.next();

        throw new QueryException("Sequence contains no items");
    }

    default public T single() {
        Iterator<T> iterator = this.iterator();
        if (iterator.hasNext()) {
            T returnValue = iterator.next();

            if (iterator.hasNext()) throw new QueryException("Sequence contains more than one item");
            return returnValue;
        }

        throw new QueryException("Sequence contains no items");
    }

    default public T firstOrNull() {
        Iterator<T> iterator = this.iterator();
        if (iterator.hasNext()) return iterator.next();

        return null;
    }

    default public T singleOrNull() {
        Iterator<T> iterator = this.iterator();
        if (iterator.hasNext()) {
            T returnValue = iterator.next();

            if (iterator.hasNext()) throw new QueryException("Sequence contains more than one item");
            return returnValue;
        }

        return null;
    }

    default public T first(Predicate<T> predicate) {
        return this.where(predicate).first();
    }

    default public T single(Predicate<T> predicate) {
        return this.where(predicate).single();
    }

    default public T firstOrNull(Predicate<T> predicate) {
        return this.where(predicate).firstOrNull();
    }

    default public T singleOrNull(Predicate<T> predicate) {
        return this.where(predicate).singleOrNull();
    }

    default public <U> QIterable<U> cast(Class<U> toType) {
        return this.map(item -> (U) item);
    }

    default public <U> QIterable<U> ofType(Class<U> type) {
        return this.where(item -> type.isAssignableFrom(item.getClass())).cast(type);
    }

    default public <U> Partition<U, T> parition(Function<T, U> onProperty) {
        return new Partition<>(this, onProperty);
    }

    default public <U> QIterable<U> flatten(Function<T, QIterable<U>> onProperty) {
        QList<U> resultSet = new QList<>();

        for (T item : this) {
            for (U subSetItem : onProperty.apply(item)) {
                resultSet.add(subSetItem);
            }
        }

        return resultSet;
    }

    default public boolean any() { return this.iterator().hasNext(); }
    default public boolean any(Predicate<T> predicate) { return this.where(predicate).any(); }
    default public boolean all(Predicate<T> predicate) { return !this.where(item -> !predicate.test(item)).any(); }

    default public QIterable<T> except(Iterable<T> rSet) {
        // First build the set of rSet to except.
        // Items may already be a set, so just use that if available.
        QSet<T> exceptItems;
        if (rSet instanceof QSet)
            exceptItems = (QSet<T>) rSet;
        else {
            exceptItems = new QSet<>();
            for (T item : rSet) exceptItems.add(item);
        }

        QList<T> subSet = new QList<>();
        for (T item : this) {
           if (!exceptItems.contains(item)) {
                subSet.add(item);
           }
        }

        return subSet;
    }

    default public QIterable<T> intersect(QIterable<T> rSet) {
        // Move the rset into a set (if it isn't one already).
        QSet<T> rhs;
        if (rSet instanceof QSet) rhs = (QSet<T>) rSet;
        else {
            rhs = new QSet<>();
            for (T item : this) {
                rhs.add(item);
            }
        }

        // For each item in this set, if it's in the rset,
        // add it to the result set.
        QList<T> resultSet = new QList<>();
        for (T item : this) {
            if (rhs.contains(item)) resultSet.add(item);
        }

        return resultSet;
    }

    default public <U extends Comparable> QIterable<T> sort(Function<T, U> onProperty) {
        List<T> items = new ArrayList<T>();
        for (T item : this) items.add(item);

        items.sort(new Comparator<T>() {
            @Override
            public int compare(T lhs, T rhs) {
                U lProperty = onProperty.apply(lhs);
                U rProperty = onProperty.apply(rhs);

                return lProperty.compareTo(rProperty);
            }
        });

        return new QIterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return items.iterator();
            }
        };
    }

}
