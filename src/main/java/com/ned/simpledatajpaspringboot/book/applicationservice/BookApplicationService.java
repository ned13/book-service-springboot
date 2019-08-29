package com.ned.simpledatajpaspringboot.book.applicationservice;

import com.ned.simpledatajpaspringboot.book.domain.BookRepository;
import com.ned.simpledatajpaspringboot.book.dto.BookDto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.ned.simpledatajpaspringboot.book.domain.Book;
import com.ned.simpledatajpaspringboot.book.domain.BookFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class BookApplicationService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookFactory bookFactory;

    public List<Book> list() {
        return bookRepository.findAll();
    }

    @Transactional
    public BookDto addNewBook(BookDto bookDto) {
        Book newBook = bookFactory.makeBookFrom(bookDto);
        Book savedBook = bookRepository.save(newBook);
        return savedBook.toBookDto();
    }

    public Optional<Book> findBookBy(String name) {
        Book exampleBook = new Book();
        exampleBook.setName(name);
        return bookRepository.findOne(Example.of(exampleBook));
    }

    public List<Book> getAllBoook() {
        return this.bookRepository.findAll();
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