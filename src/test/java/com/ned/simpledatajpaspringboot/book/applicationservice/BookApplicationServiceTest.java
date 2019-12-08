package com.ned.simpledatajpaspringboot.book.applicationservice;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

import com.ned.simpledatajpaspringboot.book.domain.Book;
import com.ned.simpledatajpaspringboot.book.domain.BookFactory;
import com.ned.simpledatajpaspringboot.book.domain.BookRepository;
import com.ned.simpledatajpaspringboot.book.dto.BookDto;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Example;

@RunWith(MockitoJUnitRunner.class)
public class BookApplicationServiceTest {
  @Mock BookRepository bookRepo;

  @Mock BookFactory bookFac;

  @InjectMocks BookApplicationService bookAppService;

  @Test
  public void testListAll() {
    // Arrange
    Book book1 = new Book(1L, "Book1");
    Book book2 = new Book(2L, "Book2");
    Book book3 = new Book(3L, "Book3");
    when(bookRepo.findAll()).thenReturn(Arrays.asList(book1, book2, book3));

    // Act
    List<BookDto> bookDtos = bookAppService.list();

    // Assert
    assertThat(bookDtos.size(), is(3));
    assertThat(bookDtos, hasItem(Matchers.<BookDto>hasProperty("name", is(book1.getName()))));
    assertThat(bookDtos, hasItem(Matchers.<BookDto>hasProperty("name", is(book2.getName()))));
    assertThat(bookDtos, hasItem(Matchers.<BookDto>hasProperty("name", is(book3.getName()))));
  }

  @Test
  public void testListAllWithoutAnyBook() {
    // Arrange
    when(bookRepo.findAll()).thenReturn(new ArrayList<Book>());

    // Act
    List<BookDto> bookDtos = bookAppService.list();

    // Assert
    assertThat(bookDtos.size(), is(0));
  }

  @Test
  public void testListAllWithInternalException() {
    // Arrange
    when(bookRepo.findAll()).thenThrow(new RuntimeException("This is internal error!!!!"));

    // Act
    try {
      bookAppService.list();
      fail("It should not be here.");
    } catch (Exception ex) {
      // Assert
      assertThat(ex, instanceOf(BookApplicationServiceException.class));
    }
  }

  @Test
  public void testAddNewBook() {
    // Arrange
    Book willBeAddedBook = new Book();
    willBeAddedBook.setId(1234L);
    willBeAddedBook.setName("IamNewBook");
    willBeAddedBook.setPublishDate(Instant.now());
    willBeAddedBook.setContactEmail("newbook@contant.com");
    when(bookFac.makeBookFrom(any())).thenReturn(willBeAddedBook);
    when(bookRepo.save(any())).thenReturn(willBeAddedBook);
    BookDto willBeAddedBookDto = willBeAddedBook.toBookDto();

    // Act
    Optional<BookDto> addedBookDtoOpt = bookAppService.addNewBook(willBeAddedBookDto);

    // Assert
    // Just to show how to use InOrder to verify calling sequences.
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
    // Arrange

    // Act
    try {
      bookAppService.addNewBook(null);
      fail("null book should not be added.");
    } catch (Exception ex) {
      // Assert
      assertThat(ex, instanceOf(NullPointerException.class));
    }
  }

  @Test
  public void testGetBookByValidId() {
    // Arrange
    final Long VALID_ID = 2L;
    Book book = new Book();
    book.setId(VALID_ID);
    book.setName("IAmAValidBook");
    when(bookRepo.findById(VALID_ID)).thenReturn(Optional.ofNullable(book));

    // Act
    Optional<BookDto> bookDtoOpt = bookAppService.getBookBy(VALID_ID);

    // Assert
    assertThat(bookDtoOpt.isPresent(), is(true));
    assertThat(bookDtoOpt.get().getId(), is(VALID_ID));
  }

  @Test
  public void testGetBookByNullId() {
    // Arrange

    // Act
    Optional<BookDto> bookDtoOpt = bookAppService.getBookBy(null);

    // Assert
    assertThat(bookDtoOpt.isPresent(), is(false));
  }

  @Test
  public void testGetBookByNonExistId() {
    // Arrange

    // Act
    Optional<BookDto> bookDtoOpt = bookAppService.getBookBy(3L);

    // Assert
    assertThat(bookDtoOpt.isPresent(), is(false));
  }

  @Test
  public void testFindBookByValidName() {
    // Arrange
    final Long VALID_ID = 2L;
    final String VALID_NAME = "IAmAValidBook";
    Book book = new Book();
    book.setId(VALID_ID);
    book.setName(VALID_NAME);
    when(bookRepo.findOne(argThat(bookNameExampleMatcher(VALID_NAME)))).thenReturn(Optional.of(book));

    // Act
    Optional<BookDto> bookDtoOpt = bookAppService.findBookBy(VALID_NAME);

    // Assert
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
    // Arrange
    final Long VALID_ID = 2L;
    final String VALID_NAME = "IAmAValidBook";
    Book book = new Book();
    book.setId(VALID_ID);
    book.setName(VALID_NAME);
    when(bookRepo.findById(VALID_ID)).thenReturn(Optional.of(book));

    // Act
    final String NEW_BOOK_NAME = "IAmNewBookName";
    Optional<BookDto> modifiedBookDtoOpt = bookAppService.modifyBookName(VALID_ID, NEW_BOOK_NAME);

    // Assert
    assertThat(modifiedBookDtoOpt.isPresent(), is(true));
    assertThat(modifiedBookDtoOpt.get().getName(), is(NEW_BOOK_NAME));
  }

  @Test
  public void testModifyBookNameWithNonExistId() {
    // Arrange
    final Long NON_EXIST_ID = 999L;

    // Act
    final String NEW_BOOK_NAME = "IAmNewBookName";
    Optional<BookDto> modifiedBookDtoOpt = bookAppService.modifyBookName(NON_EXIST_ID, NEW_BOOK_NAME);

    // Assert
    assertThat(modifiedBookDtoOpt.isPresent(), is(false));
  }

  @Test
  public void testModifyBookNameWithInvalidName() {
    // Arrange
    final Long VALID_ID = 2L;

    // Act
    final String INVALID_BOOK_NAME = null;
    Optional<BookDto> modifiedBookDtoOpt = bookAppService.modifyBookName(VALID_ID, INVALID_BOOK_NAME);

    // Assert
    assertThat(modifiedBookDtoOpt.isPresent(), is(false));
  }
}
