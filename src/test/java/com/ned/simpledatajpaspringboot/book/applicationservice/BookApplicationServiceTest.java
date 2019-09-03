package com.ned.simpledatajpaspringboot.book.applicationservice;


import org.exparity.hamcrest.date.DateMatchers;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import io.vavr.collection.Stream;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.ned.simpledatajpaspringboot.book.domain.Book;
import com.ned.simpledatajpaspringboot.book.domain.BookFactory;
import com.ned.simpledatajpaspringboot.book.domain.BookRepository;
import com.ned.simpledatajpaspringboot.book.dto.BookDto;

public class BookApplicationServiceTest {


    @Test
    public void testListAll() {
        //Arrange
        BookRepository bookRepo = mock(BookRepository.class);
        BookFactory bookFac = mock(BookFactory.class);
        BookApplicationService bas = new BookApplicationService(bookRepo, bookFac);
        Book book1 = new Book(1L, "Book1");
        Book book2 = new Book(2L, "Book2");
        Book book3 = new Book(3L, "Book3");
        when(bookRepo.findAll()).thenReturn(Arrays.asList(book1, book2, book3));

        //Act
        List<BookDto> bookDtos = bas.list();

        //Assert
        assertThat(bookDtos.size(), is(3));
        assertThat(bookDtos, hasItem(Matchers.<BookDto>hasProperty("name", is(book1.getName()))));
        assertThat(bookDtos, hasItem(Matchers.<BookDto>hasProperty("name", is(book2.getName()))));
        assertThat(bookDtos, hasItem(Matchers.<BookDto>hasProperty("name", is(book3.getName()))));
    }

    @Test
    public void testAddNewBook() {
        //Arrange
        BookRepository bookRepo = mock(BookRepository.class);
        BookFactory bookFac = mock(BookFactory.class);
        BookApplicationService bookAppService = new BookApplicationService(bookRepo, bookFac);
        Book willBeAddedBook = new Book();
        willBeAddedBook.setId(1234L);
        willBeAddedBook.setName("IamNewBook");
        willBeAddedBook.setPublishDate(Instant.now());
        willBeAddedBook.setContactEmail("newbook@contant.com");
        when(bookFac.makeBookFrom(any())).thenReturn(willBeAddedBook);
        when(bookRepo.save(any())).thenReturn(willBeAddedBook);
        BookDto willBeAddedBookDto = willBeAddedBook.toBookDto();

        //Act
        Optional<BookDto> addedBookDtoOpt = bookAppService.addNewBook(willBeAddedBookDto);

        //Assert
        verify(bookFac).makeBookFrom(willBeAddedBookDto);
        verify(bookRepo).save(willBeAddedBook);
        assertThat(addedBookDtoOpt.isPresent(), is(true));
        BookDto addedBookDto = addedBookDtoOpt.get();
        assertThat(addedBookDto.getId(), is(willBeAddedBookDto.getId()));
        assertThat(addedBookDto.getName(), is(willBeAddedBookDto.getName()));
        assertThat(addedBookDto.getContactEmail(), is(willBeAddedBookDto.getContactEmail()));
    }

}