package com.slobodianiuk.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slobodianiuk.library.dto.UserBookDto;
import com.slobodianiuk.library.exception.BusinessServiceException;
import com.slobodianiuk.library.service.BookService;
import com.slobodianiuk.library.service.UserBookService;
import com.slobodianiuk.library.service.UserService;
import com.sun.jdi.InternalException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserBookController.class)
public class UserBookControllerTest {

    private static final String TITLE = "Test title";
    private static final String AUTHOR = "Test author";
    private static final String ISBN = "111";
    private static final String PHONE_NUMBER = "+44208465927";

    @InjectMocks
    UserBookController userBookController;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserService userService;

    @MockBean
    BookService bookService;

    @MockBean
    UserBookService userBookService;

    UserBookDto userBookDto;

    @BeforeEach
    void init() {
        userBookDto = new UserBookDto();
        userBookDto.setPhoneNumber(PHONE_NUMBER);
        userBookDto.setIsbn(ISBN);
    }

    @Test
    void testTakeBook() throws Exception {

        doNothing().when(userBookService)
                .takeBook(userBookDto.getPhoneNumber(), userBookDto.getIsbn());

        mockMvc.perform(post("/userBook")
                .content(mapper.writeValueAsString(userBookDto))
                .contentType("application/json;charset=UTF-8"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userBookService).takeBook(anyString(), anyString());
    }

    @Test
    public void testTakeBookAndThrowBusinessServiceException() throws Exception {

        doThrow(new BusinessServiceException("Bad Request Error"))
                .when(userBookService)
                .takeBook(userBookDto.getPhoneNumber(), userBookDto.getIsbn());

        mockMvc.perform(post("/userBook")
                .content(mapper.writeValueAsString(userBookDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testTakeBookAndThrowInternalServerError() throws Exception {

        doThrow(new InternalException("Internal Server Error"))
                .when(userBookService)
                .takeBook(userBookDto.getPhoneNumber(), userBookDto.getIsbn());

        mockMvc.perform(post("/userBook")
                .content(mapper.writeValueAsString(userBookDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andDo(print());
    }

    @Test
    void testReturnBook() throws Exception {
        doNothing().when(userBookService)
                .returnBook(userBookDto.getPhoneNumber(), userBookDto.getIsbn());

        mockMvc.perform(delete("/userBook")
                .param("phone", PHONE_NUMBER)
                .param("isbn", ISBN)
                .contentType("application/json;charset=UTF-8"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userBookService).returnBook(anyString(), anyString());
    }

    @Test
    public void testReturnBookAndThrowBusinessServiceException() throws Exception {

        doThrow(new BusinessServiceException("Bad Request Error"))
                .when(userBookService)
                .returnBook(PHONE_NUMBER, ISBN);

        mockMvc.perform(delete("/userBook")
                .param("phone", PHONE_NUMBER)
                .param("isbn", ISBN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testReturnBookAndThrowInternalServerError() throws Exception {

        doThrow(new InternalException("Bad Request Error"))
                .when(userBookService)
                .returnBook(PHONE_NUMBER, ISBN);

        mockMvc.perform(delete("/userBook")
                .param("phone", PHONE_NUMBER)
                .param("isbn", ISBN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andDo(print());
    }
}