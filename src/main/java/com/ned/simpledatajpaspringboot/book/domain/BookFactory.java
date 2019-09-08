package com.ned.simpledatajpaspringboot.book.domain;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.ned.simpledatajpaspringboot.book.dto.BookDto;

import org.springframework.stereotype.Component;

import io.vavr.control.Option;

@Component
public class BookFactory {
    private Validator bookValidator;

    public BookFactory() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.bookValidator = factory.getValidator();
    }

    public Book makeBookFrom(BookDto bookDto) {
        return Option.of(bookDto)
                .map(b -> {
                    Book newBook = new Book();
                    if (bookDto.getId() != null) newBook.setId(bookDto.getId());
                    if (bookDto.getName() != null) newBook.setName(b.getName());
                    if (bookDto.getPublishDate() != null) newBook.setPublishDate(b.getPublishDate().toInstant());
                    if (bookDto.getContactEmail() != null) newBook.setContactEmail(b.getContactEmail());

                    Set<ConstraintViolation<Book>> violations = this.bookValidator.validate(newBook);
                    if (violations.size() > 0) throw new ConstraintViolationException(violations);

                    return newBook;
                }).getOrElse(Book.INVALID_BOOK);
    }

    public Book makeBookBy(String name) {
        Book newBook = new Book();
        newBook.setName(name);
        Set<ConstraintViolation<Book>> violations = this.bookValidator.validate(newBook);
        if (violations.size() > 0) throw new ConstraintViolationException(violations);
        return newBook;
    }

}