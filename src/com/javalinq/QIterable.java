package com.javalinq;

import java.util.function.Predicate;

/**
 * Created by sircodesalot on 14-5-30.
 */
public interface QIterable<T> extends Iterable<T> {
    default public QIterable<T> where(Predicate<T> predicate) {
        return new WhereIterable<T>(this, predicate);
    }
}
