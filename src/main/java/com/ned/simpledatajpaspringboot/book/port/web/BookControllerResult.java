package com.ned.simpledatajpaspringboot.book.port.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.ned.simpledatajpaspringboot.book.dto.BookDto;

public class BookControllerResult {
    public boolean available = false;
    public String message = "";
    public List<BookDto> books = new ArrayList<>();

    public BookControllerResult() {

    }

    public BookControllerResult(boolean available) {
        this(available, (BookDto)null, null);
    }

    public BookControllerResult(boolean available, String message) {
        this(available, (BookDto)null, message);
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
        this.books = Objects.nonNull(books) ? books : this.books;
        this.message = Objects.nonNull(message) ? message : this.message;
    }
}