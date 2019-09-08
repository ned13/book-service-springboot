package com.ned.simpledatajpaspringboot.book.dto;

import java.time.Instant;
import java.util.Date;

import org.junit.Test;

import  org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
public class BookDtoTest {

    @Test
    public void testConstructor() {
        //Arrange
        final Long id = 1L;
        final String name = "IamName";
        final Date publishDate = Date.from(Instant.now());
        final String contactEmail = "iamemail@abc.com";

        //Act
        BookDto bookDto = new BookDto(id, name, publishDate, contactEmail);

        //Assert
        assertThat(bookDto.getId(), is(id));
        assertThat(bookDto.getName(), is(name));
        assertThat(bookDto.getPublishDate(), is(publishDate));
        assertThat(bookDto.getContactEmail(), is(contactEmail));
    }

    @Test
    public void testSetterGetter() {
        //Arrange
        final Long id = 1L;
        final String name = "IamName";
        final Date publishDate = Date.from(Instant.now());
        final String contactEmail = "iamemail@abc.com";

        //Act
        BookDto bookDto = new BookDto();
        bookDto.setId(id);
        bookDto.setName(name);
        bookDto.setPublishDate(publishDate);
        bookDto.setContactEmail(contactEmail);

        //Assert
        assertThat(bookDto.getId(), is(id));
        assertThat(bookDto.getName(), is(name));
        assertThat(bookDto.getPublishDate(), is(publishDate));
        assertThat(bookDto.getContactEmail(), is(contactEmail));
    }

    @Test
    public void testEqual() {
        //Arrange
        final Long id = 1L;
        final String name = "IamName";
        final Date publishDate = Date.from(Instant.now());
        final String contactEmail = "iamemail@abc.com";

        BookDto bookDto1 = new BookDto(id, name, publishDate, contactEmail);
        BookDto bookDto2 = new BookDto(id, name, publishDate, contactEmail);

        //Act
        //Assert
        assertThat(bookDto1, is(bookDto1));
        assertThat(bookDto1, is(bookDto2));
        assertThat(bookDto1.hashCode(), is(bookDto2.hashCode()));
    }

    @Test
    public void testNotEqual() {
        //Arrange
        final Long id = 1L;
        final String name = "IamName";
        final Date publishDate = Date.from(Instant.now());
        final String contactEmail = "iamemail@abc.com";

        BookDto bookDto1 = new BookDto(id, name, publishDate, contactEmail);
        BookDto bookDto2 = new BookDto(2L, name, publishDate, contactEmail);
        BookDto bookDto3 = new BookDto(id, "IamName2", publishDate, contactEmail);

        //Act
        //Assert
        assertThat(bookDto1, not(is(bookDto2)));
        assertThat(bookDto1, not(is(bookDto3)));
    }

    @Test
    public void testInvalidBookDto() {
        assertThat(BookDto.INVALID_BOOKDTO, is(new BookDto()));
    }


}