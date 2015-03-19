package com.javalinq.interfaces;


import com.javalinq.exceptions.QueryException;
import com.javalinq.implementations.QList;
import com.javalinq.implementations.QSet;
import com.javalinq.iterators.MapIterable;
import com.javalinq.iterators.WhereIterable;
import com.javalinq.tools.Partition;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by sircodesalot on 14-5-30.
 */
public interface QIterable<T> extends Iterable<T>, Serializable {
  default public T get(int index) {
    Iterator<T> iterator = this.iterator();
    for (int count = 0; count < index; count++) {
      iterator.next();
    }

    return iterator.next();
  }

  default public <U> U getAs(int index, Class<U> type) {
    return (U) this.get(index);
  }

  default public QIterable<T> where(Predicate<T> predicate) {
    return new WhereIterable<T>(this, predicate);
  }

  default public <U> QIterable<U> map(Function<T, U> projection) {
    return new MapIterable<T, U>(this, projection);
  }

  default public QIterable<T> distinct() {
    QIterable<T> self = this;
    return new QIterable<T>() {
      QSet<T> seenItems = new QSet<>();

      @Override
      public Iterator<T> iterator() {
        return self.where(seenItems::add).iterator();
      }
    };
  }

  default public <U> QIterable<T> distinct(Function<T, U> onProperty) {
    QSet<U> seenItems = new QSet<>();
    return this.where(item -> seenItems.add(onProperty.apply(item)));
  }

  default public T first() {
    Iterator<T> iterator = this.iterator();
    if (iterator.hasNext()) return iterator.next();

    throw new QueryException("Sequence contains no items");
  }

  default public T second() {
    return this.get(1);
  }

  default public T second(Predicate<T> predicate) {
    return this.where(predicate).second();
  }

  default public <U> U secondAs(Class<U> type) {
    return (U)this.get(1);
  }

  default public <U> U firstAs(Class<U> type) {
    return (U) this.first();
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

  default public <U> U singleAs(Class<U> type) {
    return (U) this.single();
  }

  default public T last() {
    Iterator<T> iterator = this.iterator();
    if (!iterator.hasNext())
      throw new QueryException("Sequence contains no items");

    T returnValue = null;
    while (iterator.hasNext()) {
      returnValue = iterator.next();
    }

    return returnValue;
  }

  default public <U> U lastAs(Class<U> type) {
    return (U) this.last();
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

  default public T lastOrNull() {
    Iterator<T> iterator = this.iterator();

    T returnValue = null;
    while (iterator.hasNext()) {
      returnValue = iterator.next();
    }

    return returnValue;
  }

  default public T first(Predicate<T> predicate) {
    return this.where(predicate).first();
  }

  default public T single(Predicate<T> predicate) {
    return this.where(predicate).single();
  }

  default public T last(Predicate<T> predicate) {
    return this.where(predicate).last();
  }

  default public T firstOrNull(Predicate<T> predicate) {
    return this.where(predicate).firstOrNull();
  }

  default public T singleOrNull(Predicate<T> predicate) {
    return this.where(predicate).singleOrNull();
  }

  default public T lastOrNull(Predicate<T> predicate) {
    return this.where(predicate).lastOrNull();
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

  default public boolean any() {
    return this.iterator().hasNext();
  }

  default public boolean any(Predicate<T> predicate) {
    return this.where(predicate).any();
  }

  default public boolean all(Predicate<T> predicate) {
    return !this.where(item -> !predicate.test(item)).any();
  }

  default public double sum(Function<T, Double> onProperty) {
    double sum = 0;
    for (T item : this) sum += onProperty.apply(item);
    return sum;
  }

  default public double avg(Function<T, Double> onProperty) {
    double sum = 0;
    long count = 0;

    for (T item : this) {
      sum += onProperty.apply(item);
      count++;
    }

    return sum / (double) count;
  }


  default public <U extends Comparable> T max(Function<T, U> onProperty) {
    T maxItem = null;

    for (T item : this) {
      if (maxItem == null) {
        maxItem = item;
        continue;
      }

      U lhs = onProperty.apply(item);
      U rhs = onProperty.apply(maxItem);

      if (lhs.compareTo(rhs) > 0) {
        maxItem = item;
      }
    }

    return maxItem;
  }

  default public <U extends Comparable> T min(Function<T, U> onProperty) {
    T maxItem = null;

    for (T item : this) {
      if (maxItem == null) {
        maxItem = item;
        continue;
      }

      U lhs = onProperty.apply(item);
      U rhs = onProperty.apply(maxItem);

      if (lhs.compareTo(rhs) < 0) {
        maxItem = item;
      }
    }

    return maxItem;
  }

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

  default public QIterable<T> concat(Iterable<T> rhs) {
    QList<T> allItems = new QList<>();
    for (T item : this) allItems.add(item);
    for (T item : rhs) allItems.add(item);

    return allItems;
  }

  default public QIterable<T> unionDistinct(Iterable<T> rhs) {
    QSet<T> allItems = new QSet<>();
    for (T item : this) allItems.add(item);
    for (T item : rhs) allItems.add(item);

    return allItems;
  }

  default public QIterable<T> intersect(Iterable<T> rSet) {
    // Move the rset into a set (if it isn't one already).
    QSet<T> rhs;
    if (rSet instanceof QSet) rhs = (QSet<T>) rSet;
    else {
      rhs = new QSet<>();
      for (T item : rSet) {
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

  default public long count() {
    int count = 0;
    for (T item : this) count++;
    return count;
  }

  default public QIterable<T> reverse() {
    // Return a query iterable using the stack
    return new QIterable<T>() {
      @Override
      public Iterator<T> iterator() {
        // Before constructing the iterator,
        // Push all items onto the stack. Pop as we go.
        Stack<T> stack = new Stack<>();
        for (T item : QIterable.this) stack.push(item);

        return new Iterator<T>() {
          @Override
          public boolean hasNext() {
            return !stack.empty();
          }

          @Override
          public T next() {
            return stack.pop();
          }
        };
      }
    };
  }

  default public <U> U reduce(U collector, Reducer<T, U> reducer) {
    for (T item : this) {
      collector = reducer.reduce(collector, item);
    }

    return collector;
  }

  default public long count(Predicate<T> predicate) {
    return this.where(predicate).count();
  }

  default public QList<T> toList() {
    return new QList<>(this);
  }

  default public QSet<T> toSet() {
    return new QSet<>(this);
  }

  class MyCollection<T> implements QIterable<T> {

    @Override
    public Iterator<T> iterator() {
      return null;
    }
  }

}
