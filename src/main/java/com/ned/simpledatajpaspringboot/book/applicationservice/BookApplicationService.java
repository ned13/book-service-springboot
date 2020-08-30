package com.ned.simpledatajpaspringboot.book.applicationservice;

import com.ned.simpledatajpaspringboot.book.domain.Book;
import com.ned.simpledatajpaspringboot.book.domain.BookFactory;
import com.ned.simpledatajpaspringboot.book.domain.BookRepository;
import com.ned.simpledatajpaspringboot.book.dto.BookDto;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BookApplicationService {
  private BookRepository bookRepository;
  private BookFactory bookFactory;

  @Autowired
  public BookApplicationService(BookRepository bookRepository, BookFactory bookFactory) {
    this.bookRepository = bookRepository;
    this.bookFactory = bookFactory;
  }

  public List<BookDto> list() {
    log.info("User lists all books.");
    try {
      return bookRepository.findAll().stream().map(book -> book.toBookDto()).collect(Collectors.toList());
    } catch (Exception ex) {
      throw new BookApplicationServiceException(ex);
    }
  }

  @Transactional
  public Optional<BookDto> addNewBook(BookDto willBeAddedBookDto) {
    Objects.requireNonNull(willBeAddedBookDto, "willBeAddedBookDto shuold not be null.");
    Book newBook = bookFactory.makeBookFrom(willBeAddedBookDto);
    Book savedBook = bookRepository.save(newBook);
    return Optional.ofNullable(savedBook.toBookDto());
  }

  public Optional<BookDto> getBookBy(Long id) {
    return Optional.ofNullable(id)
      .flatMap(localId -> bookRepository.findById(localId))
      .map(foundBook -> foundBook.toBookDto());
  }

  public Optional<BookDto> findBookBy(String name) {
    return Optional.ofNullable(name)
        .flatMap(
            localName -> {
              Book exampleBook = new Book();
              exampleBook.setName(name);
              return bookRepository.findOne(Example.of(exampleBook));
            })
        .map(b -> b.toBookDto());
  }

  @Transactional
  public Optional<BookDto> modifyBookName(Long id, String newBookName) {
    return Optional.ofNullable(id)
        .filter(localId -> Strings.isNotBlank(newBookName))
        .flatMap(localId -> bookRepository.findById(localId))
        .map(
            foundBook -> {
              foundBook.setName(newBookName);
              bookRepository.save(foundBook);
              return foundBook.toBookDto();
            });
  }
}
