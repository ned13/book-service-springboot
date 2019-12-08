package com.ned.simpledatajpaspringboot.book.domain;

import com.ned.simpledatajpaspringboot.book.dto.BookDto;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

@Entity
@Slf4j
public class Book {

  public static final String INVALID_NAME_LENGTH = "Invalid name length";
  public static final String INVALID_EMAIL = "Invalid email";
  public static final Book INVALID_BOOK = new Book();
  private static final ModelMapper modelMapper = new ModelMapper(); // TODO: this object needs to be Singleton.
  public static final TypeMap<Book, BookDto> typeMap = Book.modelMapper.createTypeMap(Book.class, BookDto.class);

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_generator")
  @SequenceGenerator(name = "book_generator", sequenceName = "book_seq")
  @Column
  private Long id;

  @NotNull
  @Size(min = 1, max = 100, message = INVALID_NAME_LENGTH)
  @Column
  private String name;

  @Column private Instant publishDate;

  @Email(message = INVALID_EMAIL)
  @Column
  private String contactEmail;

  @Version private Integer version;

  static {
    Book.typeMap.addMappings(
        mapper -> {
          mapper.using((ctx) -> Date.from((Instant) ctx.getSource()));
          mapper.when(Conditions.isNotNull());
          mapper.map(Book::getPublishDate, BookDto::setPublishDate);
          mapper.skip(BookDto::setComments);
        });
  }

  // standard constructors
  public Book() {
  }

  public Book(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  @Override
  public boolean equals(Object otherObj) {
    if (this == otherObj) return true;
    if (!(otherObj instanceof Book)) return false;
    Book otherBook = (Book) otherObj;
    return Objects.equals(this.id, otherBook.id)
        && Objects.equals(this.name, otherBook.name)
        && Objects.equals(this.publishDate, otherBook.publishDate)
        && Objects.equals(this.contactEmail, otherBook.contactEmail);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.name);
  }

  public static TypeMap<Book, BookDto> getTypeMap() {
    return Book.typeMap;
  }

  public BookDto toBookDto() {
    if (this == Book.INVALID_BOOK) return BookDto.INVALID_BOOKDTO;
    return modelMapper.map(this, BookDto.class);
  }

  // standard getters and setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Instant getPublishDate() {
    return publishDate;
  }

  public void setPublishDate(Instant publishDate) {
    this.publishDate = publishDate;
  }

  public String getContactEmail() {
    return contactEmail;
  }

  public void setContactEmail(String contactEmail) {
    this.contactEmail = contactEmail;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }
}
