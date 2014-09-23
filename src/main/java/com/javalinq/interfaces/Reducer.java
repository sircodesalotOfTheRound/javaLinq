package com.javalinq.interfaces;

/**
 * Created by sircodesalot on 14-6-4.
 */
public interface Reducer<T, U> {
  U reduce(U collector, T item);
}
