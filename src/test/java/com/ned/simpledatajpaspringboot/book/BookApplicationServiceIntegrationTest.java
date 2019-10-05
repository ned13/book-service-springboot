package com.ned.simpledatajpaspringboot.book;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.ned.simpledatajpaspringboot.book.applicationservice.BookApplicationService;
import com.ned.simpledatajpaspringboot.book.domain.BookFactory;
import com.ned.simpledatajpaspringboot.book.domain.BookRepository;
import com.ned.simpledatajpaspringboot.book.dto.BookDto;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockReset;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;

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

        //Arrange
        BookDto bookDto1 = BookDto.builder()
            .id(1L).name("book1")
            .publishDate(Date.from(Instant.now()))
            .contactEmail("book1@abc.com")
            .build();

        BookDto bookDto2 = BookDto.builder()
            .id(2L).name("book2")
            .publishDate(Date.from(Instant.now()))
            .contactEmail("book2@abc.com")
            .build();

        when(bookAppService.list()).thenReturn(Arrays.asList(bookDto1, bookDto2));

        //Act
        List<BookDto> books = bookAppService.list();
        Optional<BookDto> aBook = bookAppService.getBookBy(1L);

        //Assert
        assertThat(books.size(), is(2));
        assertThat(aBook.isPresent(), is(true));
        assertThat(aBook.get().getId(), is(1L));
    }



    @Test
    public void testAddNewBook() {
        //Arrange
        final String NEW_BOOK_NAME = "IamNewBook1234";

        //Act
        BookDto newBookDto = new BookDto();
        newBookDto.setName(NEW_BOOK_NAME);
        newBookDto.setPublishDate(Date.from(Instant.now()));
        bookAppService.addNewBook(newBookDto);

        //Assert
        List<BookDto> books = bookAppService.list();
        assertThat(books, hasItem(Matchers.<BookDto>hasProperty("name", is(NEW_BOOK_NAME))));
    }

    @Test
    public void testFindBookByName() {
        //Arrange
        final String NEW_BOOK_NAME = "ThisIsANewBook";
        BookDto newBookDto = new BookDto();
        newBookDto.setName(NEW_BOOK_NAME);
        newBookDto.setPublishDate(Date.from(Instant.now()));
        bookAppService.addNewBook(newBookDto);

        //Act
        Optional<BookDto> foundBook = bookAppService.findBookBy(NEW_BOOK_NAME);

        //Assert
        assertThat(foundBook.isPresent(), is(true));
        assertThat(foundBook.get().getName(), is(NEW_BOOK_NAME));
    }

}

