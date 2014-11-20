package com.javalinq.tools;

import com.javalinq.implementations.QList;
import com.javalinq.interfaces.QIterable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by sircodesalot on 14-5-30.
 */
public class Partition<U, T> implements QIterable<QIterable<T>> {
  public Map<U, QList<T>> map = new HashMap<>();

  public Partition(QIterable<T> iterable, Function<T, U> onProperty) {
    for (T item : iterable) {
      U propertyValue = onProperty.apply(item);
      QList<T> listForKey = getListForKey(propertyValue);

      listForKey.add(item);
    }
  }

  public QList<T> getListForKey(U key) {
    if (map.containsKey(key)) {
      return map.get(key);
    }

    QList<T> newListForKey = new QList<>();
    map.put(key, newListForKey);

    return newListForKey;
  }

  public QIterable<T> flatten() {
    return this.flatten(entry -> entry);
  }

  public boolean containsKey(U key) {
    return map.containsKey(key);
  }

  public QIterable<T> get(U key) {
    return this.map.get(key);
  }

  @Override
  public Iterator<QIterable<T>> iterator() {
    final Iterator<U> keyIterator = map.keySet().iterator();

    return new Iterator<QIterable<T>>() {
      @Override
      public boolean hasNext() {
        return (keyIterator.hasNext());
      }

      @Override
      public QIterable<T> next() {
        return get(keyIterator.next());
      }
    };
  }
}
