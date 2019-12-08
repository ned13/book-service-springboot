package com.ned.simpledatajpaspringboot.book.port.web;

import com.ned.simpledatajpaspringboot.book.applicationservice.BookApplicationService;
import com.ned.simpledatajpaspringboot.book.dto.BookDto;

import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class BookController {
  private BookApplicationService bookAppService;

  @Autowired
  public BookController(BookApplicationService bookAppService) {
    this.bookAppService = bookAppService;
  }

  @RequestMapping(path = "", method = RequestMethod.GET)
  public ResponseEntity<BookControllerResult> getAllBoook(@RequestParam(value = "bookname", required = false) final String bookname) {
    log.info("receive http requst /books");
    if (bookname == null || bookname.isEmpty()) {
      List<BookDto> allBook = bookAppService.list();
      return ResponseEntity.ok().body(new BookControllerResult(true, allBook));
    } else {
      Optional<BookDto> foundBookDtoOpt = bookAppService.findBookBy(bookname);
      if (!foundBookDtoOpt.isPresent()) {
        return ResponseEntity.ok().body(new BookControllerResult(true));
      }

      BookDto foundBookDto = foundBookDtoOpt.get();
      return ResponseEntity.ok().body(new BookControllerResult(true, foundBookDto));
    }
  }

  @RequestMapping(
      path = "",
      method = RequestMethod.POST,
      consumes = {"application/json"},
      produces = {"application/json"})
  public ResponseEntity<BookControllerResult> addNewBook(@RequestBody BookDto bookDto) {
    return bookAppService
        .addNewBook(bookDto)
        .map(addedBookDto -> ResponseEntity.ok().body(new BookControllerResult(true, addedBookDto)))
        .orElse(ResponseEntity.ok().body(new BookControllerResult(false, "book is not added.")));
  }

  @RequestMapping(
      path = "/{id}",
      method = RequestMethod.PUT,
      consumes = {"application/json"},
      produces = {"application/json"})
  public ResponseEntity<BookControllerResult> modifyBookName(@PathVariable("id") long id, @RequestBody BookDto bookDto) {
    if (id == 0) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new BookControllerResult(false, "Invalid id=" + id));
    }

    String bookName = bookDto.getName();
    if (bookName == null || bookName.isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new BookControllerResult(false, "Invalid bookName=" + bookName));
    }

    return bookAppService
        .modifyBookName(id, bookName)
        .map(modifiedBookDto -> ResponseEntity.ok().body(new BookControllerResult(true, modifiedBookDto)))
        .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BookControllerResult(false, "No book name is modified.")));
  }
}
