package com.ned.simpledatajpaspringboot.applicationservice;

import com.ned.simpledatajpaspringboot.domain.BookRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.ned.simpledatajpaspringboot.domain.Book;

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

    public void addNewBook(String name) {
        Book newBook = new Book();
        newBook.setName(name);
        newBook.setPublishDate(Instant.now());

        bookRepository.save(newBook);
    }

    public Optional<Book> findBookBy(String name) {
        Book exampleBook = new Book();
        exampleBook.setName(name);
        return bookRepository.findOne(Example.of(exampleBook));
    }
}