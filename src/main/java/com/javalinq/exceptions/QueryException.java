package com.javalinq.exceptions;

/**
 * Created by sircodesalot on 14-5-30.
 */
public class QueryException extends RuntimeException {
  public QueryException(String format, Object... args) {
    super(String.format(format, args));
  }
}
