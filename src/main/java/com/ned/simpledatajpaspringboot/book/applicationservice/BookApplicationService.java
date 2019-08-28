package com.ned.simpledatajpaspringboot.book.applicationservice;

import com.ned.simpledatajpaspringboot.book.domain.BookRepository;
import com.ned.simpledatajpaspringboot.book.dto.BookDto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.ned.simpledatajpaspringboot.book.domain.Book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class BookApplicationService {
    @Autowired
    private BookRepository bookRepository;

    public List<Book> list() {
        return bookRepository.findAll();
    }

    @Transactional
    public BookDto addNewBook(BookDto bookDto) {
        Book newBook = new Book();
        newBook.setName(bookDto.getName());
        newBook.setPublishDate(bookDto.getPublishDate());
        newBook.setContactEmail(bookDto.getContactEmail());
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
    public void modifyBookName(Long id, String newBookName) {
        Optional<Book> foundBook = bookRepository.findById(id);
        if (!foundBook.isPresent()) return;
        foundBook.get().setName(newBookName);
        bookRepository.save(foundBook.get());
    }
}