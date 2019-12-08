package com.ned.simpledatajpaspringboot.book;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import com.ned.simpledatajpaspringboot.book.applicationservice.BookApplicationService;
import com.ned.simpledatajpaspringboot.book.dto.BookDto;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockReset;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@RunWith(SpringRunner.class)
@SpringBootTest
public class BookApplicationServiceIntegrationTest {

  // @TestConfiguration
  // public static class InnerTestConfiguration {
  //     @Autowired
  //     private BookRepository bookRepo;

  //     @Autowired
  //     private BookFactory bookFac;

  //     @Bean
  //     @Primary
  //     public BookApplicationService bookApplicationService1() {
  //         return new BookApplicationService(bookRepo, bookFac);
  //     }
  // }

  // @Autowired
  // @Qualifier("bookApplicationService1")
  @SpyBean(reset = MockReset.BEFORE)
  private BookApplicationService bookAppService;

  @Test
  public void testListAllBooks() {
    List<BookDto> books = bookAppService.list();

    assertThat(books, hasItem(Matchers.<BookDto>hasProperty("id", is(1L))));
    assertThat(books, hasItem(Matchers.<BookDto>hasProperty("id", is(2L))));
    assertThat(books, hasItem(Matchers.<BookDto>hasProperty("id", is(3L))));
    assertThat(books, hasItem(Matchers.<BookDto>hasProperty("id", is(4L))));
  }

  @Test
  public void testListAllBooksWithSpy() {

    // Arrange
    BookDto bookDto1 = BookDto.builder().id(1L).name("book1").publishDate(Date.from(Instant.now())).contactEmail("book1@abc.com").build();

    BookDto bookDto2 = BookDto.builder().id(2L).name("book2").publishDate(Date.from(Instant.now())).contactEmail("book2@abc.com").build();

    when(bookAppService.list()).thenReturn(Arrays.asList(bookDto1, bookDto2));

    // Act
    List<BookDto> books = bookAppService.list();
    Optional<BookDto> firstBook = bookAppService.getBookBy(1L);

    // Assert
    assertThat(books.size(), is(2));
    assertThat(firstBook.isPresent(), is(true));
    assertThat(firstBook.get().getId(), is(1L));
  }

  @Test
  public void testAddNewBook() {
    // Arrange
    final String NEW_BOOK_NAME = "IamNewBook1234";

    // Act
    BookDto newBookDto = new BookDto();
    newBookDto.setName(NEW_BOOK_NAME);
    newBookDto.setPublishDate(Date.from(Instant.now()));
    bookAppService.addNewBook(newBookDto);

    // Assert
    List<BookDto> books = bookAppService.list();
    assertThat(books, hasItem(Matchers.<BookDto>hasProperty("name", is(NEW_BOOK_NAME))));
  }

  @Test
  public void testFindBookByName() {
    // Arrange
    final String NEW_BOOK_NAME = "ThisIsANewBook";
    BookDto newBookDto = new BookDto();
    newBookDto.setName(NEW_BOOK_NAME);
    newBookDto.setPublishDate(Date.from(Instant.now()));
    bookAppService.addNewBook(newBookDto);

    // Act
    Optional<BookDto> foundBook = bookAppService.findBookBy(NEW_BOOK_NAME);

    // Assert
    assertThat(foundBook.isPresent(), is(true));
    assertThat(foundBook.get().getName(), is(NEW_BOOK_NAME));
  }
}
