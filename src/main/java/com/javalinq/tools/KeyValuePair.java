package com.javalinq.tools;

/**
 * Created by sircodesalot on 15/3/16.
 */
public class KeyValuePair<T, U> {
  private final T key;
  private final U value;

  public KeyValuePair(T key, U value) {
    this.key = key;
    this.value = value;
  }

  public T key() { return key; }
  public U value() { return value; }
}
