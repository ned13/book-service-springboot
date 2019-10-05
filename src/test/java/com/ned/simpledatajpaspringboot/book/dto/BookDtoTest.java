package com.ned.simpledatajpaspringboot.book.dto;

import java.time.Instant;
import java.util.Date;

import org.junit.Test;

import lombok.val;

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

    @Test
    public void testToBuilder() {
        //Arrange
        val id = 1L;
        val name = "IamName";
        val publishDate = Date.from(Instant.now());
        val contactEmail = "iamemail@abc.com";

        BookDto bookDto1 = BookDto.builder().id(id).name(name).publishDate(publishDate).contactEmail(contactEmail).build();

        //Act
        val modifiedName = "ModifiedName";
        val modifiedContactEmail = "modifiedEmail";
        val modifiedBookDto1 = bookDto1.toBuilder().name(modifiedName).contactEmail(modifiedContactEmail).build();

        //Assert
        assertThat(modifiedBookDto1.getName(), is(modifiedName));
        assertThat(modifiedBookDto1.getContactEmail(), is(modifiedContactEmail));
    }

    @Test
    public void testWithXXX() {
        //Arrange
        val id = 1L;
        val name = "IamName";
        val publishDate = Date.from(Instant.now());
        val contactEmail = "iamemail@abc.com";

        BookDto bookDto1 = BookDto.builder().id(id).name(name).publishDate(publishDate).contactEmail(contactEmail).build();

        //Act
        val modifiedName = "ModifiedName";
        val modifiedContactEmail = "modifiedEmail";
        val modifiedBookDto1 = bookDto1.withName(modifiedName).withContactEmail(modifiedContactEmail);

        //Assert
        assertThat(modifiedBookDto1.getName(), is(modifiedName));
        assertThat(modifiedBookDto1.getContactEmail(), is(modifiedContactEmail));
    }

    @Test
    public void testSingular() {
        // Arrange
        val id = 1L;
        val name = "IamName";
        val publishDate = Date.from(Instant.now());
        val contactEmail = "iamemail@abc.com";

        val comment1Id = 22L;
        val comment1Content = "IamComment1";
        val comment1Date = Date.from(Instant.now());
        val comment1 = CommentDto.builder().createdByUserId(comment1Id).content(comment1Content)
                .createdFrom(comment1Date).build();

        val comment2Id = 33L;
        val comment2Content = "IamComment2";
        val comment2Date = Date.from(Instant.now());
        val comment2 = CommentDto.builder().createdByUserId(comment2Id).content(comment2Content)
                .createdFrom(comment2Date).build();

        // Act
        val bookDto = BookDto.builder().id(id).name(name).publishDate(publishDate).contactEmail(contactEmail).comment(comment1).comment(comment2).build();

        //Assert
        assertThat(bookDto.getComments(), hasItem(comment1));
        assertThat(bookDto.getComments(), hasItem(comment2));
    }


}