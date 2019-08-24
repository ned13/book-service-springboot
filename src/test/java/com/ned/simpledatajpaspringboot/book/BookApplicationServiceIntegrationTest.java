package com.ned.simpledatajpaspringboot.book;

import java.util.List;
import java.util.Optional;

import com.ned.simpledatajpaspringboot.book.applicationservice.BookApplicationService;
import com.ned.simpledatajpaspringboot.book.domain.Book;

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
        List<Book> books = bookAppService.list();

        assertThat(books.size(), is(4));
    }

    @Test
    public void testAddNewBook() {
        //Arrange
        final String NEW_BOOK_NAME = "IamNewBook1234";

        //Act
        bookAppService.addNewBook(NEW_BOOK_NAME);

        //Assert
        List<Book> books = bookAppService.list();
        assertThat(books, hasItem(Matchers.<Book>hasProperty("name", is(NEW_BOOK_NAME))));
    }

    @Test
    public void testFindBookByName() {
        //Arrange
        final String NEW_BOOK_NAME = "ThisIsANewBook";
        bookAppService.addNewBook(NEW_BOOK_NAME);

        //Act
        Optional<Book> foundBook = bookAppService.findBookBy(NEW_BOOK_NAME);

        //Assert
        assertThat(foundBook.isPresent(), is(true));
        assertThat(foundBook.get().getId(), greaterThan(0L));
        assertThat(foundBook.get().getName(), is(NEW_BOOK_NAME));
    }

}

