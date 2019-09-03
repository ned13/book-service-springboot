package com.ned.simpledatajpaspringboot.book.domain;

import com.ned.simpledatajpaspringboot.book.dto.BookDto;

import org.springframework.stereotype.Component;

import io.vavr.control.Option;

@Component
public class BookFactory {
    public Book makeBookFrom(BookDto bookDto) {
        return Option.of(bookDto)
                .map(b -> {
                    Book newBook = new Book();
                    if (bookDto.getId() != null) newBook.setId(bookDto.getId());
                    if (bookDto.getName() != null) newBook.setName(b.getName());
                    if (bookDto.getPublishDate() != null) newBook.setPublishDate(b.getPublishDate().toInstant());
                    if (bookDto.getContactEmail() != null) newBook.setContactEmail(b.getContactEmail());
                    return newBook;
                }).getOrElse(Book.INVALID_BOOK);
    }

}