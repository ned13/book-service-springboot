package com.ned.simpledatajpaspringboot.book;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.ned.simpledatajpaspringboot.book.applicationservice.BookApplicationService;
import com.ned.simpledatajpaspringboot.book.dto.BookDto;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertThat;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@RunWith(SpringRunner.class)
@SpringBootTest
public class BookApplicationServiceIntegrationTest {
    @Autowired
    private BookApplicationService bookAppService;

    @Test
    public void whenApplicationStarts_thenHibernateCreatesInitialRecords() {
        List<BookDto> books = bookAppService.list();

        assertThat(books.size(), is(4));
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
        assertThat(foundBook.get().getId(), greaterThan(0L));
        assertThat(foundBook.get().getName(), is(NEW_BOOK_NAME));
    }

}

