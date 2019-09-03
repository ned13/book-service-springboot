package com.ned.simpledatajpaspringboot.book.applicationservice;

import com.ned.simpledatajpaspringboot.book.domain.BookRepository;
import com.ned.simpledatajpaspringboot.book.dto.BookDto;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.ned.simpledatajpaspringboot.book.domain.Book;
import com.ned.simpledatajpaspringboot.book.domain.BookFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class BookApplicationService {
    private BookRepository bookRepository;
    private BookFactory bookFactory;

    @Autowired
    public BookApplicationService(BookRepository bookRepository, BookFactory bookFactory) {
        this.bookRepository = bookRepository;
        this.bookFactory = bookFactory;
    }

    public List<BookDto> list() {
        return bookRepository.findAll().stream()
            .map(book -> book.toBookDto())
            .collect(Collectors.toList());
    }

    @Transactional
    public Optional<BookDto> addNewBook(BookDto willBeAddedBookDto) {
        Objects.requireNonNull(willBeAddedBookDto, "willBeAddedBookDto shuold not be null.");
        Book newBook = bookFactory.makeBookFrom(willBeAddedBookDto);
        Book savedBook = bookRepository.save(newBook);
        return Optional.of(savedBook.toBookDto());
    }

    public Optional<BookDto> getBookBy(Long id) {
        Objects.requireNonNull(id, "id should not be null.");
        return bookRepository.findById(id)
            .map(book -> book.toBookDto());
    }

    public Optional<BookDto> findBookBy(String name) {
        Book exampleBook = new Book();
        exampleBook.setName(name);
        return bookRepository.findOne(Example.of(exampleBook)).map(b -> b.toBookDto());
    }

    public List<BookDto> getAllBoook() {
        return this.bookRepository.findAll()
            .stream()
            .map(book -> book.toBookDto())
            .collect(Collectors.toList());
    }

    @Transactional
    public BookDto modifyBookName(Long id, String newBookName) {
        Optional<Book> foundBookOpt = bookRepository.findById(id);
        if (!foundBookOpt.isPresent()) return BookDto.INVALID_BOOKDTO;
        Book foundBook = foundBookOpt.get();
        foundBook.setName(newBookName);
        return bookRepository.save(foundBook).toBookDto();
    }
}