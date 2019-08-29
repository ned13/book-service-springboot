package com.ned.simpledatajpaspringboot.book;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.ned.simpledatajpaspringboot.book.applicationservice.BookApplicationService;
import com.ned.simpledatajpaspringboot.book.domain.Book;
import com.ned.simpledatajpaspringboot.book.dto.BookDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping(path = "", method = RequestMethod.POST, consumes = {"application/json"}, produces = {"application/json"})
    public BookDto addNewBook(@RequestBody BookDto bookDto) {
        return bookAppService.addNewBook(bookDto);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT, consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Result> modifyBookName(@PathVariable("id") long id, @RequestBody BookDto bookDto) {
        if (id == 0) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Result(false, "Invalid id=" + id));
        String bookName = bookDto.getName();
        if (bookName == null || bookName == "") return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Result(false, "Invalid bookName=" + bookName));
        BookDto modifiedBookDto = bookAppService.modifyBookName(id, bookName);
        return ResponseEntity.ok().body(new Result(true, modifiedBookDto));
    }


    public class Result {
        public boolean available = false;
        public String message = "";
        public BookDto book = BookDto.INVALID_BOOKDTO;

        public Result(boolean available) {
            this(available, null, null);
        }

        public Result(boolean available, BookDto book) {
            this(available, book, null);
        }

        public Result(boolean available, String message) {
            this(available, null, message);
        }


        public Result(boolean available, BookDto book, String message) {
            this.available = available;
            this.book = book != null ? book : this.book;
            this.message = message != null ? message : this.message;
        }





    }
}

