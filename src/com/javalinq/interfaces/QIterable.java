package com.javalinq.interfaces;

import com.javalinq.exceptions.QueryException;
import com.javalinq.iterators.DistinctIterable;
import com.javalinq.iterators.DistinctIterableOnProperty;
import com.javalinq.iterators.MapIterable;
import com.javalinq.iterators.WhereIterable;
import com.javalinq.tools.Partition;

import java.util.Iterator;
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

}
