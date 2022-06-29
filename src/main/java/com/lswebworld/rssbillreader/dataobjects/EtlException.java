package com.lswebworld.rssbillreader.dataobjects;

/**
 * Etl Exception Class.
 */
public class EtlException extends Exception {

  public EtlException() {
    super();
  }

  public EtlException(String message) {
    super(message);
  }

  public EtlException(String message, Throwable inner) {
    super(message, inner);
  }


}
