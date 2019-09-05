package com.ned.simpledatajpaspringboot.book.domain;

import static org.junit.Assert.assertThat;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Random;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.ned.simpledatajpaspringboot.book.dto.BookDto;

import org.exparity.hamcrest.date.DateMatchers;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import io.vavr.collection.Stream;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;


public class BookTest {
    private static Validator validator;

    @BeforeClass
    public static void beforeTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        BookTest.validator = factory.getValidator();
    }

    @Test
    public void testSetterAndGetter() {
        //Arrange
        Book aBook = new Book();
        final long ID = 1234L;
        final String NAME = "ThisIsABook.";
        final Instant PUBLISH_DATE = Instant.now();
        final String CONTACT_EMAIL = "iamcontact@abc.com";

        //Act
        aBook.setId(ID);
        aBook.setName(NAME);
        aBook.setPublishDate(PUBLISH_DATE);
        aBook.setContactEmail(CONTACT_EMAIL);

        //Assert
        assertThat(aBook.getId(), is(ID));
        assertThat(aBook.getName(), is(NAME));
        assertThat(aBook.getPublishDate(), is(PUBLISH_DATE));
        assertThat(aBook.getContactEmail(), is(CONTACT_EMAIL));
    }

    @Test
    public void testEmptyName() {
        //Arrange
        Book aBook = new Book();
        aBook.setName("");

        //Act
        Set<ConstraintViolation<Book>> violations = validator.validate(aBook);

        //Assert
        assertThat(violations, hasItem(Matchers.<ConstraintViolation<Book>>hasProperty("message", is(Book.INVALID_NAME_LENGTH))));
    }

    @Test
    public void testExceedingMaxLengthName() {
        //Arrange
        Book aBook = new Book();
        Random rand = new Random();

        //generate book name with random char between 32-126, which is printable characters
        String bookName = Stream.continually(() -> rand.nextInt(94)+32)
            .take(101)
            .map(i -> (char)i.intValue())
            .toCharSeq()
            .mkString("");

        aBook.setName(bookName);

        //Act
        Set<ConstraintViolation<Book>> violations = validator.validate(aBook);

        //Assert
        assertThat(violations, hasItem(Matchers.<ConstraintViolation<Book>>hasProperty("message", is(Book.INVALID_NAME_LENGTH))));
    }

    @Test
    public void testValidEmail() {
        //Arrange
        Book aBook = new Book();
        aBook.setName("IamBookName");
        aBook.setContactEmail("abc@sha.com");

        //Act
        Set<ConstraintViolation<Book>> violations = validator.validate(aBook);

        //Assert
        assertThat(violations.size(), is(0));
    }

    @Test
    public void testInvalidEmail() {
        //Arrange
        Book book1 = new Book();
        book1.setName("IamBookName");
        book1.setContactEmail("invalidmail!@#@@sha.com");

        //Act
        Set<ConstraintViolation<Book>> book1Violations = validator.validate(book1);

        //Assert
        assertThat(book1Violations, hasItem(Matchers.<ConstraintViolation<Book>>hasProperty("message", is(Book.INVALID_EMAIL))));
    }

    @Test
    public void testToBookDtoTypeMap() {
        Book.getTypeMap().validate();
    }

    @Test
    public void testToBookDto() {
        //Arrange
        Book aBook = new Book();
        final long ID = 1235L;
        final String NAME = "TOBookDto";
        final Instant PUBLISH_DATE = Instant.now();
        final String CONTACT_EMAIL = "tobookdto@abc.com";
        aBook.setId(ID);
        aBook.setName(NAME);
        aBook.setPublishDate(PUBLISH_DATE);
        aBook.setContactEmail(CONTACT_EMAIL);

        //Act
        BookDto bookDto = aBook.toBookDto();

        //Assert
        assertThat(aBook.getId(), is(bookDto.getId()));
        assertThat(aBook.getName(), is(bookDto.getName()));
        assertThat(Date.from(aBook.getPublishDate()),  DateMatchers.within(2, ChronoUnit.SECONDS, bookDto.getPublishDate()));
        assertThat(aBook.getContactEmail(), is(bookDto.getContactEmail()));
    }

    @Test
    public void testInvalidBookToInavlidBookDto() {
        //Act
        BookDto bookDto = Book.INVALID_BOOK.toBookDto();

        //Assert
        assertThat(bookDto, is(BookDto.INVALID_BOOKDTO));
    }
}
