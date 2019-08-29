package com.ned.simpledatajpaspringboot.book;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public ResponseEntity<Result> getAllBoook(@RequestParam(value = "bookname", required = false) String bookname) {
        if (bookname == null || bookname == "") {
            List<BookDto> allBook = bookAppService.getAllBoook().stream().map(b -> b.toBookDto()).collect(Collectors.toList());
            return ResponseEntity.ok().body(new Result(true, allBook));
        } else {
            Optional<Book> foundBookOpt = bookAppService.findBookBy(bookname);
            if (!foundBookOpt.isPresent()) return ResponseEntity.ok().body(new Result(true));
            Book foundBook = foundBookOpt.get();
            return ResponseEntity.ok().body(new Result(true, foundBook.toBookDto()));
        }
    }

    @RequestMapping(path = "", method = RequestMethod.POST, consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Result> addNewBook(@RequestBody BookDto bookDto) {
        BookDto addedBookDto = bookAppService.addNewBook(bookDto);
        return ResponseEntity.ok().body(new Result(true, addedBookDto));
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
        public List<BookDto> books = new ArrayList<>();

        public Result(boolean available) {
            this(available, (BookDto)null, null);
        }

        public Result(boolean available, String message) {
            this(available, (BookDto)null, message);
        }

        public Result(boolean available, BookDto book) {
            this(available, book, null);
        }

        public Result(boolean available, BookDto book, String message) {
            this(available, Objects.nonNull(book) ? Arrays.asList(book) : null, message);
        }

        public Result(boolean available, List<BookDto> books) {
            this(available, books, null);
        }

        public Result(boolean available, List<BookDto> books, String message) {
            this.available = available;
            this.books = Objects.nonNull(books) ? books : this.books;
            this.message = Objects.nonNull(message) ? message : this.message;
        }
    }
}

