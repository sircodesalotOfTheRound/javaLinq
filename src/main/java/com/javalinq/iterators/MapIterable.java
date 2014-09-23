package com.javalinq.iterators;

import com.javalinq.interfaces.QIterable;

import java.util.Iterator;
import java.util.function.Function;

/**
 * Created by sircodesalot on 14-5-30.
 */
public class MapIterable<T, U> implements QIterable<U> {
  private final Function<T, U> projection;
  private final Iterable<T> iterable;

  public MapIterable(Iterable<T> iterable, Function<T, U> projection) {
    this.iterable = iterable;
    this.projection = projection;
  }

  @Override
  public Iterator<U> iterator() {
    final Iterator<T> iterator = this.iterable.iterator();

    // Return a projection iterator.
    return new Iterator<U>() {
      @Override
      public boolean hasNext() {
        return iterator.hasNext();
      }

      @Override
      public U next() {
        return projection.apply(iterator.next());
      }
    };
  }
}
