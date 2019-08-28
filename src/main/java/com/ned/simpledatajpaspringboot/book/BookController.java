package com.ned.simpledatajpaspringboot.book;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.ned.simpledatajpaspringboot.book.applicationservice.BookApplicationService;
import com.ned.simpledatajpaspringboot.book.domain.Book;
import com.ned.simpledatajpaspringboot.book.dto.BookDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public class BookController {
    private BookApplicationService bookAppService;

    @Autowired
    public BookController(BookApplicationService bookAppService) {
        this.bookAppService = bookAppService;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<Book> getAllBoook(@RequestParam(value = "bookname", required = false) String bookname) {
        if (bookname == null || bookname == "") {
            return bookAppService.getAllBoook();
        } else {
            Optional<Book> foundBook = bookAppService.findBookBy(bookname);
            return foundBook.isPresent() ? Arrays.asList(foundBook.get()) : new ArrayList<Book>();

        }
    }

    @RequestMapping(path = "", method = RequestMethod.POST, consumes = {"application/json"})
    public BookDto addNewBook(@RequestBody BookDto bookDto) {
        return bookAppService.addNewBook(bookDto);
    }

}