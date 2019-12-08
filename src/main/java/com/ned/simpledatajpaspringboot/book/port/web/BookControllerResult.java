package com.ned.simpledatajpaspringboot.book.port.web;

import com.ned.simpledatajpaspringboot.book.dto.BookDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BookControllerResult {
  public boolean available = false;
  public List<BookDto> books = new ArrayList<>();
  public String message = "";

  public BookControllerResult() {
  }

  public BookControllerResult(boolean available) {
    this(available, (BookDto) null, null);
  }

  public BookControllerResult(boolean available, String message) {
    this(available, (BookDto) null, message);
  }

  public BookControllerResult(boolean available, BookDto book) {
    this(available, book, null);
  }

  public BookControllerResult(boolean available, BookDto book, String message) {
    this(available, Objects.nonNull(book) ? Arrays.asList(book) : null, message);
  }

  public BookControllerResult(boolean available, List<BookDto> books) {
    this(available, books, null);
  }

  public BookControllerResult(boolean available, List<BookDto> books, String message) {
    this.available = available;
    this.books = Objects.nonNull(books) ? new ArrayList<>(books) : this.books;
    this.message = Objects.nonNull(message) ? message : this.message;
  }
}
