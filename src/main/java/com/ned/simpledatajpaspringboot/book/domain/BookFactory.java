package com.ned.simpledatajpaspringboot.book.domain;

import com.ned.simpledatajpaspringboot.book.dto.BookDto;

import io.vavr.control.Option;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BookFactory {
  private Validator bookValidator;

  public BookFactory() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    this.bookValidator = factory.getValidator();
  }

  public Book makeBookFrom(BookDto bookDto) {
    return Option.of(bookDto)
        .map(
            b -> {
              log.info("make a book according to " + b.toString());
              Book newBook = new Book();
              if (b.getId() != null) newBook.setId(b.getId());
              if (b.getName() != null) newBook.setName(b.getName());
              if (b.getPublishDate() != null) newBook.setPublishDate(b.getPublishDate().toInstant());
              if (b.getContactEmail() != null) newBook.setContactEmail(b.getContactEmail());

              Set<ConstraintViolation<Book>> violations = this.bookValidator.validate(newBook);
              if (violations.size() > 0) throw new ConstraintViolationException(violations);

              return newBook;
            })
        .getOrElse(Book.INVALID_BOOK);
  }

  public Book makeBookBy(String name) {
    Book newBook = new Book();
    newBook.setName(name);
    Set<ConstraintViolation<Book>> violations = this.bookValidator.validate(newBook);
    if (violations.size() > 0) throw new ConstraintViolationException(violations);
    return newBook;

    // return Option.of(name)
    //     .filter(n -> n != null && !n.isEmpty())
    //     .map(n -> {
    //       Book newBook = new Book();
    //       newBook.setName(name);
    //       Set<ConstraintViolation<Book>> violations = this.bookValidator.validate(newBook);
    //       if (violations.size() > 0) throw new ConstraintViolationException(violations);
    //       return newBook;
    //     })
    //     .getOrElse(Book.INVALID_BOOK);
  }
}
