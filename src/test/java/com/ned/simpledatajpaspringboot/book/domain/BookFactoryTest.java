package com.ned.simpledatajpaspringboot.book.domain;

import org.junit.Test;
import org.exparity.hamcrest.date.DateMatchers;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.validation.ConstraintViolationException;

import com.ned.simpledatajpaspringboot.book.dto.BookDto;

public class BookFactoryTest {
    private BookFactory bookFac = new BookFactory();

    @Test
    public void testMakeBookFromValidBootDto() {
        //Arrange
        final Long ID = 1L;
        final String NAME = "IamName";
        final Date PUBLISH_DATE = Date.from(Instant.now());
        final String CONTACT_EMAIL = "iamemail@abc.com";
        BookDto bookDto = new BookDto(ID, NAME, PUBLISH_DATE, CONTACT_EMAIL);

        //Act
        Book madeBook = bookFac.makeBookFrom(bookDto);

        //Assert
        assertThat(madeBook.getId(), is(ID));
        assertThat(madeBook.getName(), is(NAME));
        assertThat(Date.from(madeBook.getPublishDate()), DateMatchers.within(2, ChronoUnit.SECONDS, PUBLISH_DATE));
        assertThat(madeBook.getContactEmail(), is(CONTACT_EMAIL));
    }

    @Test
    public void testMakeBookFromInvalidBootDto() {
        //Arrange
        final Long ID = 1L;
        final String NAME = "IamName";
        final Date PUBLISH_DATE = Date.from(Instant.now());
        final String INVALID_CONTACT_EMAIL = "notinvalidemail";
        BookDto bookDto = new BookDto(ID, NAME, PUBLISH_DATE, INVALID_CONTACT_EMAIL);

        //Act
        try {
            bookFac.makeBookFrom(bookDto);
            fail();
        } catch (ConstraintViolationException ex) {
            //Assert
            assertThat(ex, instanceOf(ConstraintViolationException.class));
            ex.getConstraintViolations();
        }
    }

    @Test
    public void testMakeBookByValidIdName() {
        //Arrange
        final String VALID_NAME = "IamBookName";

        //Act
        Book madeBook = bookFac.makeBookBy(VALID_NAME);

        //Assert
        assertThat(madeBook.getName(), is(VALID_NAME));
    }

    @Test
    public void testMakeBookWithInvalidName() {
        //Arrange
        final String INVALID_NAME = "";

        //Act
        try {
            bookFac.makeBookBy(INVALID_NAME);
            fail();
        } catch (ConstraintViolationException ex) {
            //Assert
            assertThat(ex, instanceOf(ConstraintViolationException.class));
            ex.getConstraintViolations();
        }
    }

    @Test
    public void testMockBookFactory() {
        //Arrange
        BookFactory mockBookFac = mock(BookFactory.class);
        Book mockRetBook1 = new Book();
        Book mockRetBook2 = new Book();
        when(mockBookFac.makeBookBy(anyString())).thenReturn(mockRetBook1);
        when(mockBookFac.makeBookFrom(any())).thenReturn(mockRetBook2);

        //Act
        Book retBook1 = mockBookFac.makeBookBy("kkk");
        Book retBook2 = mockBookFac.makeBookFrom(null);

        //Assert
        assertThat(retBook1, is(mockRetBook1));
        assertThat(retBook2, is(mockRetBook2));
    }

    @Test
    public void testSpyBookFactory() {
        //Arrange
        BookFactory spyBookFac = spy(bookFac);
        Book spyRetBook = new Book();
        spyRetBook.setId(333L);
        final String SPY_BOOK_NAME = "SpyBookName";
        spyRetBook.setName(SPY_BOOK_NAME);
        when(spyBookFac.makeBookFrom(any())).thenReturn(spyRetBook);
        final String NORMAL_BOOK_NAME = "NormalBookName";

        //Act
        Book retBook1 = spyBookFac.makeBookFrom(BookDto.INVALID_BOOKDTO);
        Book retBook2 = spyBookFac.makeBookBy(NORMAL_BOOK_NAME);

        //Assert
        assertThat(retBook1.getName(), is(SPY_BOOK_NAME));
        assertThat(retBook2.getName(), is(NORMAL_BOOK_NAME));
    }

}