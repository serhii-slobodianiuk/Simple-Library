package com.slobodianiuk.library.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.slobodianiuk.library.dto.BookDto;
import com.slobodianiuk.library.exception.BusinessServiceException;
import com.slobodianiuk.library.model.Book;
import com.slobodianiuk.library.repository.BookRepository;
import com.slobodianiuk.library.service.BookService;
import com.sun.jdi.InternalException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    @Mock
    BookRepository bookRepository;

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
                .andDo(print())
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
        when(bookService.add(any(Book.class))).thenReturn(book);

        MvcResult mvcResult = mockMvc.perform(post("/book")
                .content(mapper.writeValueAsString(bookDto))
                .contentType("application/json;charset=UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String actualResponse = mvcResult.getResponse().getContentAsString();
        BookDto actualBookDto = mapper.readValue(actualResponse, new TypeReference<>() {
        });

        assertEquals(TITLE, actualBookDto.getTitle());
        assertEquals(AUTHOR, actualBookDto.getAuthor());
        assertEquals(ISBN, actualBookDto.getIsbn());

        verify(bookService).add(any(Book.class));
    }

    @Test
    void testAddBookAndThrowInternalServerError() throws Exception {

        when(bookService.add(any(Book.class)))
                .thenThrow(new BusinessServiceException("Internal Server Error"));

        mockMvc.perform(post("/book")
                .content(mapper.writeValueAsString(bookDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andDo(print());
    }

    @Test
    void testUpdateBook() throws Exception {

        when(bookService.update(any(Book.class))).thenReturn(book);

        MvcResult mvcResult = mockMvc.perform(put("/book")
                .content(mapper.writeValueAsString(bookDto))
                .contentType("application/json;charset=UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String actualResponse = mvcResult.getResponse().getContentAsString();
        Book actualBook = mapper.readValue(actualResponse, new TypeReference<>() {
        });

        assertEquals(TITLE, actualBook.getTitle());
        assertEquals(AUTHOR, actualBook.getAuthor());
        assertEquals(ISBN, actualBook.getIsbn());

        verify(bookService).update(any(Book.class));
    }

    @Test
    void testUpdateBookAndThrowInternalServerError() throws Exception {

        when(bookService.update(any(Book.class)))
                .thenThrow(new BusinessServiceException("Internal Server Error"));

        mockMvc.perform(post("/book")
                .content(mapper.writeValueAsString(bookDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andDo(print());
    }

    @Test
    void testDeleteBook() throws Exception {

        doNothing().when(bookService).delete(ISBN);

        mockMvc.perform(delete("/book")
                .param("isbn", ISBN)
                .contentType("application/json;charset=UTF-8"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(bookService).delete(anyString());
    }

    @Test
    public void testDeleteBookAndThrowInternalServerError() throws Exception {
        doThrow(new InternalException("Internal Server Error"))
                .when(bookService).delete(ISBN);

        mockMvc.perform(delete("/book")
                .param("isbn", ISBN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andDo(print());
    }

}
