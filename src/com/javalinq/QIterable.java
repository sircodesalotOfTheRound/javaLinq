package com.javalinq;

import com.javalinq.iterators.MapIterable;
import com.javalinq.iterators.WhereIterable;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by sircodesalot on 14-5-30.
 */
public interface QIterable<T> extends Iterable<T> {
    default public QIterable<T> where(Predicate<T> predicate) {
        return new WhereIterable<T>(this, predicate);
    }

    default public <U> MapIterable<T, U> map(Function<T, U> projection) {
        return new MapIterable<T, U>(this, projection);
   }
}
