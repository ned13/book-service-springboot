package com.ned.simpledatajpaspringboot.book.applicationservice;

public class BookApplicationServiceException extends RuntimeException {
  public BookApplicationServiceException(Exception ex) {
    super(ex);
  }
}
