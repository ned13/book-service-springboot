package com.ned.simpledatajpaspringboot.book.dto;

import java.time.Instant;

public class BookDto {
    private Long id;
    private String name;
    private Instant publishDate;
    private String contactEmail;

    public static final BookDto INVALID_BOOKDTO = new BookDto();

    public BookDto() {}

    public BookDto(Long id, String name, Instant publishDate, String contactEmail) {
        this.id = id;
        this.name = name;
        this.publishDate = publishDate;
        this.contactEmail = contactEmail;
    }

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