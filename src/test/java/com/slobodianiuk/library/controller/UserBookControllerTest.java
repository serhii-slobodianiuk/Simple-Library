package com.slobodianiuk.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slobodianiuk.library.dto.UserBookDto;
import com.slobodianiuk.library.service.BookService;
import com.slobodianiuk.library.service.UserBookService;
import com.slobodianiuk.library.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
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
                .andExpect(status().isOk())
                .andReturn();

        verify(userBookService).takeBook(anyString(), anyString());
    }
}