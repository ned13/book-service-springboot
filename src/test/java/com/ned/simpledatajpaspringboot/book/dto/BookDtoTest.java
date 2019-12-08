package com.ned.simpledatajpaspringboot.book.dto;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.time.Instant;
import java.util.Date;
import org.junit.Test;

public class BookDtoTest {

  @Test
  public void testConstructor() {
    // Arrange
    final Long id = 1L;
    final String name = "IamName";
    final Date publishDate = Date.from(Instant.now());
    final String contactEmail = "iamemail@abc.com";

    // Act
    final BookDto bookDto = new BookDto(id, name, publishDate, contactEmail);

    // Assert
    assertThat(bookDto.getId(), is(id));
    assertThat(bookDto.getName(), is(name));
    assertThat(bookDto.getPublishDate(), is(publishDate));
    assertThat(bookDto.getContactEmail(), is(contactEmail));
  }

  @Test
  public void testSetterGetter() {
    // Arrange
    final Long id = 1L;
    final String name = "IamName";
    final Date publishDate = Date.from(Instant.now());
    final String contactEmail = "iamemail@abc.com";

    // Act
    final BookDto bookDto = new BookDto();
    bookDto.setId(id);
    bookDto.setName(name);
    bookDto.setPublishDate(publishDate);
    bookDto.setContactEmail(contactEmail);

    // Assert
    assertThat(bookDto.getId(), is(id));
    assertThat(bookDto.getName(), is(name));
    assertThat(bookDto.getPublishDate(), is(publishDate));
    assertThat(bookDto.getContactEmail(), is(contactEmail));
  }

  @Test
  public void testEqual() {
    // Arrange
    final Long id = 1L;
    final String name = "IamName";
    final Date publishDate = Date.from(Instant.now());
    final String contactEmail = "iamemail@abc.com";

    final BookDto bookDto1 = new BookDto(id, name, publishDate, contactEmail);
    final BookDto bookDto2 = new BookDto(id, name, publishDate, contactEmail);

    // Act
    // Assert
    assertThat(bookDto1, is(bookDto1));
    assertThat(bookDto1, is(bookDto2));
    assertThat(bookDto1.hashCode(), is(bookDto2.hashCode()));
  }

  @Test
  public void testNotEqual() {
    // Arrange
    final Long id = 1L;
    final String name = "IamName";
    final Date publishDate = Date.from(Instant.now());
    final String contactEmail = "iamemail@abc.com";

    final BookDto bookDto1 = new BookDto(id, name, publishDate, contactEmail);
    final BookDto bookDto2 = new BookDto(2L, name, publishDate, contactEmail);
    final BookDto bookDto3 = new BookDto(id, "IamName2", publishDate, contactEmail);

    // Act
    // Assert
    assertThat(bookDto1, not(is(bookDto2)));
    assertThat(bookDto1, not(is(bookDto3)));
  }

  @Test
  public void testInvalidBookDto() {
    assertThat(BookDto.INVALID_BOOKDTO, is(new BookDto()));
  }

  @Test
  public void testToBuilder() {
    // Arrange
    final BookDto bookDto1 = BookDto.builder().id(1L).name("IamName").publishDate(Date.from(Instant.now())).contactEmail("iamemail@abc.com").build();

    // Act
    final String modifiedName = "ModifiedName";
    final String modifiedContactEmail = "modifiedEmail";
    final BookDto modifiedBookDto1 = bookDto1.toBuilder().name(modifiedName).contactEmail(modifiedContactEmail).build();

    // Assert
    assertThat(modifiedBookDto1.getName(), is(modifiedName));
    assertThat(modifiedBookDto1.getContactEmail(), is(modifiedContactEmail));
  }

  @Test
  public void testWithXxx() {
    // Arrange
    final BookDto bookDto1 = BookDto.builder().id(1L).name("IamName").publishDate(Date.from(Instant.now())).contactEmail("iamemail@abc.com").build();

    // Act
    final String modifiedName = "ModifiedName";
    final String modifiedContactEmail = "modifiedEmail";
    final BookDto modifiedBookDto1 = bookDto1.withName(modifiedName).withContactEmail(modifiedContactEmail);

    // Assert
    assertThat(modifiedBookDto1.getName(), is(modifiedName));
    assertThat(modifiedBookDto1.getContactEmail(), is(modifiedContactEmail));
  }

  @Test
  public void testSingular() {
    // Arrange
    final CommentDto comment1 = CommentDto.builder().createdByUserId(22L).content("IamComment1").createdFrom(Date.from(Instant.now())).build();
    final CommentDto comment2 = CommentDto.builder().createdByUserId(33L).content("IamComment2").createdFrom(Date.from(Instant.now())).build();

    // Act
    final BookDto bookDto = BookDto.builder()
        .id(1L).name("IamName")
        .publishDate(Date.from(Instant.now())).contactEmail("iamemail@abc.com")
        .comment(comment1).comment(comment2)
        .build();

    // Assert
    assertThat(bookDto.getComments(), hasItem(comment1));
    assertThat(bookDto.getComments(), hasItem(comment2));
  }
}
