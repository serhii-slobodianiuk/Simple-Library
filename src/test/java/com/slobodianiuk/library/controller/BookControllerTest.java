package com.slobodianiuk.library.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.slobodianiuk.library.dto.BookDto;
import com.slobodianiuk.library.model.Book;
import com.slobodianiuk.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = BookController.class)
class BookControllerTest {

    private static final String TITLE = "Test title";
    private static final String AUTHOR = "Test author";
    private static final String ISBN = "111";
    private static final Long ID = 123L;
    private static final String PHONE_NUMBER = "12345";

    @InjectMocks
    BookController bookController;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    BookService bookService;

    Book book;
    BookDto bookDto;

    @BeforeEach
    void init() {
        book = new Book();
        book.setId(ID);
        book.setTitle(TITLE);
        book.setAuthor(AUTHOR);
        book.setIsbn(ISBN);
        bookDto = new BookDto(book.getTitle(), book.getAuthor(), book.getIsbn());
    }

    @Test
    void testGetAllAvailableBooks() throws Exception {

        when(bookService.getAllAvailable()).thenReturn(Collections.singletonList(book));

        MvcResult mvcResult = mockMvc.perform(get("/book"))
                .andExpect(status().isOk()).andReturn();

        String actualResponse = mvcResult.getResponse().getContentAsString();
        List<BookDto> actualBookDtos = mapper.readValue(actualResponse, new TypeReference<>() {
        });
        BookDto actualBookDto = actualBookDtos.get(0);

        assertEquals(1, actualBookDtos.size());
        assertEquals(TITLE, actualBookDto.getTitle());
        assertEquals(AUTHOR, actualBookDto.getAuthor());
        assertEquals(ISBN, actualBookDto.getIsbn());
    }

    @Test
    void testAddBook() throws Exception {

        mockMvc.perform(post("/book")
                .content(mapper.writeValueAsString(bookDto))
                .contentType("application/json;charset=UTF-8"));

    }
}
