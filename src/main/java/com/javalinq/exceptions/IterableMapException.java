package com.javalinq.exceptions;

/**
 * Created by sircodesalot on 15/3/16.
 */
public class IterableMapException extends RuntimeException {
  public IterableMapException(String message, Object ... args) {
    super(String.format(message, args));
  }
}
