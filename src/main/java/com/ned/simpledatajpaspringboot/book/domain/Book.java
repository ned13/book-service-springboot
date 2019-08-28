package com.ned.simpledatajpaspringboot.book.domain;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.ned.simpledatajpaspringboot.book.dto.BookDto;

import org.modelmapper.ModelMapper;

@Entity
public class Book {

    public static final String INVALID_NAME_LENGTH = "Invalid name length";
    public static final String INVALID_EMAIL = "Invalid email";
    public static final ModelMapper mapper = new ModelMapper();
    public static final Book INVALID_BOOK = new Book();

    @Id
    @GeneratedValue
    @Column
    private Long id;

    @NotNull
    @Size(min = 1, max = 100, message = INVALID_NAME_LENGTH)
    @Column
    private String name;

    @Column
    private Instant publishDate;

    @Email(message = INVALID_EMAIL)
    @Column
    private String contactEmail;

    // standard constructors
    public Book() {
    }

    public BookDto toBookDto() {
        if (this == Book.INVALID_BOOK) return BookDto.INVALID_BOOKDTO;
        return mapper.map(this, BookDto.class);
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






}