package com.ned.simpledatajpaspringboot.book.applicationservice;

//JUnit
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.data.domain.Example;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

//hamcrest
import org.exparity.hamcrest.date.DateMatchers;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;

//mockito
import org.mockito.ArgumentMatcher;
import org.mockito.InOrder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
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
    public void testListAllWithoutAnyBook() {
        //Arrange
        BookRepository bookRepo = mock(BookRepository.class);
        BookFactory bookFac = mock(BookFactory.class);
        BookApplicationService bas = new BookApplicationService(bookRepo, bookFac);
        when(bookRepo.findAll()).thenReturn(new ArrayList<Book>());

        //Act
        List<BookDto> bookDtos = bas.list();

        //Assert
        assertThat(bookDtos.size(), is(0));
    }

    @Test
    public void testListAllWithInternalException() {
        //Arrange
        BookRepository bookRepo = mock(BookRepository.class);
        BookFactory bookFac = mock(BookFactory.class);
        BookApplicationService bookAppService = new BookApplicationService(bookRepo, bookFac);
        when(bookRepo.findAll()).thenThrow(new RuntimeException("This is internal error!!!!"));

        //Act
        try {
            bookAppService.list();
            fail("It should not be here.");
        } catch (Exception ex) {
            //Assert
            assertThat(ex, instanceOf(BookApplicationServiceException.class));
        }

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
        //Just to show how to use InOrder to verify calling sequences.
        InOrder inOrder = inOrder(bookFac, bookRepo);
        inOrder.verify(bookFac).makeBookFrom(willBeAddedBookDto);
        inOrder.verify(bookRepo).save(willBeAddedBook);
        // verify(bookFac).makeBookFrom(willBeAddedBookDto);
        // verify(bookRepo).save(willBeAddedBook);


        assertThat(addedBookDtoOpt.isPresent(), is(true));
        BookDto addedBookDto = addedBookDtoOpt.get();
        assertThat(addedBookDto.getId(), is(willBeAddedBookDto.getId()));
        assertThat(addedBookDto.getName(), is(willBeAddedBookDto.getName()));
        assertThat(addedBookDto.getContactEmail(), is(willBeAddedBookDto.getContactEmail()));
    }

    @Test
    public void testAddNewBookWithNull() {
        //Arrange
        BookRepository bookRepo = mock(BookRepository.class);
        BookFactory bookFac = mock(BookFactory.class);
        BookApplicationService bookAppService = new BookApplicationService(bookRepo, bookFac);

        //Act
        try {
            bookAppService.addNewBook(null);
            fail("null book should not be added.");
        } catch (Exception ex) {
            //Assert
            assertThat(ex, instanceOf(NullPointerException.class));
        }
    }

    @Test
    public void testGetBookByValidId() {
        //Arrange
        BookRepository bookRepo = mock(BookRepository.class);
        BookFactory bookFac = mock(BookFactory.class);
        BookApplicationService bookAppService = new BookApplicationService(bookRepo, bookFac);
        final Long VALID_ID = 2L;
        Book book = new Book();
        book.setId(VALID_ID);
        book.setName("IAmAValidBook");
        when(bookRepo.findById(VALID_ID)).thenReturn(Optional.ofNullable(book));

        //Act
        Optional<BookDto> bookDtoOpt = bookAppService.getBookBy(VALID_ID);

        //Assert
        assertThat(bookDtoOpt.isPresent(), is(true));
        assertThat(bookDtoOpt.get().getId(), is(VALID_ID));
    }

    @Test
    public void testGetBookByNullId() {
        //Arrange
        BookRepository bookRepo = mock(BookRepository.class);
        BookFactory bookFac = mock(BookFactory.class);
        BookApplicationService bookAppService = new BookApplicationService(bookRepo, bookFac);

        //Act
        Optional<BookDto> bookDtoOpt = bookAppService.getBookBy(null);

        //Assert
        assertThat(bookDtoOpt.isPresent(), is(false));
    }

    @Test
    public void testGetBookByNonExistId() {
        //Arrange
        BookRepository bookRepo = mock(BookRepository.class);
        BookFactory bookFac = mock(BookFactory.class);
        BookApplicationService bookAppService = new BookApplicationService(bookRepo, bookFac);

        //Act
        Optional<BookDto> bookDtoOpt = bookAppService.getBookBy(3L);

        //Assert
        assertThat(bookDtoOpt.isPresent(), is(false));
    }

    @Test
    public void testFindBookByValidName() {
        //Arrange
        BookRepository bookRepo = mock(BookRepository.class);
        BookFactory bookFac = mock(BookFactory.class);
        BookApplicationService bookAppService = new BookApplicationService(bookRepo, bookFac);
        final Long VALID_ID = 2L;
        final String VALID_NAME = "IAmAValidBook";
        Book book = new Book();
        book.setId(VALID_ID);
        book.setName(VALID_NAME);
        when(bookRepo.findOne(argThat(bookNameExampleMatcher(VALID_NAME)))).thenReturn(Optional.of(book));

        //Act
        Optional<BookDto> bookDtoOpt = bookAppService.findBookBy(VALID_NAME);

        //Assert
        assertThat(bookDtoOpt.isPresent(), is(true));
        assertThat(bookDtoOpt.get().getId(), is(VALID_ID));
    }

    private ArgumentMatcher<Example<Book>> bookNameExampleMatcher(String name) {
        return new ArgumentMatcher<Example<Book>>() {
            @Override
            public boolean matches(Example<Book> argument) {
                return argument.getProbe().getName() == name;
            }
        };
    }

    @Test
    public void testModifyBookName() {

    }

    @Test
    public void testModifyBookNameWithNonExistId() {

    }

    @Test
    public void testModifyBookNameWithInvalidName() {

    }


}