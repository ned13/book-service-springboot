package com.ned.simpledatajpaspringboot.book.port.web;

//JUnit
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.ned.simpledatajpaspringboot.book.applicationservice.BookApplicationService;
import com.ned.simpledatajpaspringboot.book.dto.BookDto;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookApplicationService bookAppService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllBook() throws Exception {
        //Arrange
        BookDto bookDto1 = new BookDto(1L, "book1", Date.from(Instant.now()), "book1@abc.com");
        BookDto bookDto2 = new BookDto(2L, "book2", Date.from(Instant.now()), "book2@abc.com");
        BookDto bookDto3 = new BookDto(3L, "book3", Date.from(Instant.now()), "book3@abc.com");
        List<BookDto> bookDtos = Arrays.asList(bookDto1, bookDto2, bookDto3);
        when(bookAppService.list()).thenReturn(bookDtos);

        //Act
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/books")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{'available': true}"))
                .andReturn();

        String resultStr = mvcResult.getResponse().getContentAsString();
        BookControllerResult result = objectMapper.readValue(resultStr, BookControllerResult.class);

        //Assert
        assertThat(result.available, is(true));
        assertThat(result.books.size(), is(3));
        assertThat(result.books, hasItem(Matchers.<BookDto>hasProperty("id", is(bookDto1.getId()))));
        assertThat(result.books, hasItem(Matchers.<BookDto>hasProperty("id", is(bookDto2.getId()))));
        assertThat(result.books, hasItem(Matchers.<BookDto>hasProperty("id", is(bookDto3.getId()))));
    }

    @Test
    public void testGetBookByName() throws Exception {
        //Arrange
        BookDto bookDto1 = new BookDto(1L, "book1", Date.from(Instant.now()), "book1@abc.com");
        when(bookAppService.findBookBy(bookDto1.getName())).thenReturn(Optional.of(bookDto1));

        //Act
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/books")
                .param("bookname", bookDto1.getName())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String resultStr = mvcResult.getResponse().getContentAsString();
        BookControllerResult result = objectMapper.readValue(resultStr, BookControllerResult.class);

        //Assert
        assertThat(result.available, is(true));
        assertThat(result.books.size(), is(1));
        assertThat(result.books, hasItem(Matchers.<BookDto>hasProperty("id", is(bookDto1.getId()))));
    }

    @Test
    public void testAddNewBook() throws Exception {
        BookDto bookDto1 = new BookDto(1L, "book1", Date.from(Instant.now()), "book1@abc.com");
        when(bookAppService.addNewBook(bookDto1)).thenReturn(Optional.of(bookDto1));

        //Act
        //How to use MockMvc to post application/json.
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDto1))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String resultStr = mvcResult.getResponse().getContentAsString();
        BookControllerResult result = objectMapper.readValue(resultStr, BookControllerResult.class);

        //Assert
        assertThat(result.available, is(true));
        assertThat(result.books.size(), is(1));
        assertThat(result.books, hasItem(Matchers.<BookDto>hasProperty("id", is(bookDto1.getId()))));
    }


}