package com.javalinq.iterators;


import com.javalinq.interfaces.QIterable;

import java.util.Iterator;
import java.util.function.Predicate;

/**
 * Created by sircodesalot on 14-5-30.
 */
public class WhereIterable<T> implements QIterable<T> {
  private final QIterable<T> iterable;
  private final Predicate<T> predicate;

  public WhereIterable(QIterable<T> iterable, Predicate<T> predicate) {
    this.iterable = iterable;
    this.predicate = predicate;
  }

  @Override
  public Iterator<T> iterator() {
    return new WhereIterator(iterable, predicate);
  }

  public class WhereIterator implements Iterator<T> {
    private final Iterator<T> iterator;
    private final Predicate<T> predicate;
    private boolean hasUpdated = false;
    private T nextItem;

    private WhereIterator(Iterable<T> iterable, Predicate<T> predicate) {
      this.iterator = iterable.iterator();
      this.predicate = predicate;
    }

    @Override
    public boolean hasNext() {
      hasUpdated = true;
      while (this.iterator.hasNext()) {
        this.nextItem = iterator.next();
        if (predicate.test(nextItem)) return true;
      }

      nextItem = null;
      return false;
    }

    @Override
    public T next() {
      if (!hasUpdated) {
        this.hasNext();
      }

      hasUpdated = false;
      return nextItem;
    }
  }
}
