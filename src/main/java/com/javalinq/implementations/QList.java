package com.javalinq.implementations;


import com.javalinq.interfaces.QIterable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sircodesalot on 14-5-30.
 */
public class QList<T> implements QIterable<T> {
  List<T> list = new ArrayList<>();

  public QList() {
  }

  public QList(T... items) {
    if (items != null) {
      for (T item : items) this.add(item);
    }
  }

  public QList(Iterable<T> items) {
    for (T item : items) this.add(item);
  }

  public void remove(int index) {
    this.list.remove(index);
  }

  public void remove(T item) {
    this.list.remove(item);
  }

  @Override
  public Iterator<T> iterator() {
    return this.list.iterator();
  }

  public void add(T item) {
    this.list.add(item);
  }

  public void add(Iterable<T> items) {
    for (T item : items) this.add(item);
  }

  @Override
  public T get(int index) {
    return this.list.get(index);
  }

  @Override
  public <U> U getAs(int index, Class<U> type) {
    return (U) this.list.get(index);
  }

  public int indexOf(T item) {
    return this.list.indexOf(item);
  }

  public void clear() {
    this.list.clear();
  }

  public int size() {
    return list.size();
  }

  @Override
  public long count() {
    return list.size();
  }
}
