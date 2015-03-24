package com.javalinq.implementations;

import com.javalinq.exceptions.IterableMapException;
import com.javalinq.interfaces.QIterable;
import com.javalinq.tools.KeyValuePair;

import java.util.Iterator;

/**
 * Created by sircodesalot on 15/3/16.
 */
public class QMap<T, U> implements QIterable<KeyValuePair<T, U>> {
  private final int DEFAULT_SIZE = 11;

  class Link {
    private final T key;
    private final KeyValuePair<T, U> pair;
    private final Link next;

    public Link(T key, U value, Link next) {
      this.key = key;
      this.pair = new KeyValuePair<>(key, value);
      this.next = next;
    }

    public Link(KeyValuePair<T, U> pair, Link next) {
      this.key = pair.key();
      this.pair = pair;
      this.next = next;
    }

    public T key() { return this.key; }
    public U value()  { return this.pair.value(); }
    public KeyValuePair<T, U> pair() { return this.pair; }
  }

  private int size = DEFAULT_SIZE;
  private int threshold = (DEFAULT_SIZE * 3) / 4;
  private int count = 0;
  private int[] lengths = new int[size];
  private Object[] items = new Object[size];

  private int hash(T key) { return Math.abs(key.hashCode()) % size; }

  private Link find(T key) {
    int hash = hash(key);
    for (Link current = (Link)items[hash]; current != null; current = current.next) {
      if (current.key.equals(key)) {
        return current;
      }
    }

    return null;
  }

  public boolean containsKey(T key) {
    return find(key) != null;
  }

  @Override
  public long count() { return this.count(); }

  public boolean add(T key, U value) {
    if (!containsKey(key)) {
      int hash = hash(key);
      int length = lengths[hash];
      if (length >= threshold) {
        expand();
      }

      items[hash] = new Link(key, value, (Link)items[hash]);
      lengths[hash]++;
      count++;
      return true;
    } else {
      return false;
    }
  }

  public void remove(T key) {
    int hash = hash(key);
    Link current, previous = null;
    for (current = (Link)items[hash]; current != null; current = current.next) {
      if (current.key.equals(key)) {
        break;
      }
      previous = current;
    }

    // If the head was the item we needed to delete.
    if (previous == null) {
      items[hash] = current.next;
    } else {
      previous = current.next;
    }
  }

  public U get(T key) {
    Link item = find(key);
    if (item == null) {
      throw new IterableMapException("No such item.");
    }

    return item.pair.value();
  }

  private void expand() {
    int newSize = size * 19;
    int newThreshold = threshold * 19;
    Object[] newTable = new Object[newSize];
    int[] newLengths = new int[newSize];

    for (KeyValuePair<T, U> item : this) {
      int hash = hash(item.key());
      newTable[hash] = new Link(item, (Link)newTable[hash]);
      newLengths[hash]++;
    }

    this.size = newSize;
    this.threshold = newThreshold;
    this.items = newTable;
    this.lengths = newLengths;
  }

  @Override
  public Iterator<KeyValuePair<T, U>> iterator() {
    return new Iterator<KeyValuePair<T, U>>() {
      private int hash = 0;
      private Link current;
      private int size = QMap.this.size;
      private Object[] items = QMap.this.items;

      @Override
      public boolean hasNext() {
        while (current == null) {
          current = (Link)items[hash++];
          if (hash >= size) {
            return false;
          }
        }

        return true;
      }

      @Override
      public KeyValuePair<T, U> next() {
        KeyValuePair<T, U> pair = current.pair;
        current = current.next;

        return pair;
      }
    };
  }


}
