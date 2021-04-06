package com.slobodianiuk.library.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.slobodianiuk.library.dto.UserDto;
import com.slobodianiuk.library.model.User;
import com.slobodianiuk.library.repository.UserRepository;
import com.slobodianiuk.library.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    private static final Long USER_ID = 123L;
    private static final String FIRST_NAME = "Freddie";
    private static final String LAST_NAME = "Mercury";
    private static final String PHONE_NUMBER = "+44208465927";

    @InjectMocks
    UserController userController;

    @Mock
    UserRepository userRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserService userService;

    User user;
    UserDto userDto;

    @BeforeEach
    void init() {
        user = new User();
        user.setId(USER_ID);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setPhoneNumber(PHONE_NUMBER);

        userDto = new UserDto(user.getFirstName(),
                user.getLastName(), user.getPhoneNumber());
    }

    @Test
    void testGetAllUsers() throws Exception {
        when(userService.getAll()).thenReturn(Collections.singletonList(user));

        MvcResult mvcResult = mockMvc.perform(get("/user"))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        String actualResponse = mvcResult.getResponse().getContentAsString();
        List<UserDto> actualUserDtos = mapper.readValue(actualResponse, new TypeReference<>() {
        });

        UserDto actualUserDto = actualUserDtos.get(0);

        assertEquals(1, actualUserDtos.size());
        assertEquals(FIRST_NAME, actualUserDto.getFirstName());
        assertEquals(LAST_NAME, actualUserDto.getLastName());
        assertEquals(PHONE_NUMBER, actualUserDto.getPhoneNumber());
    }

    @Test
    void testAddUser() throws Exception {
        when(userService.add(any(User.class))).thenReturn(user);

        MvcResult mvcResult = mockMvc.perform(post("/user")
                .content(mapper.writeValueAsString(user))
                .contentType("application/json;charset=UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String actualResponse = mvcResult.getResponse().getContentAsString();
        UserDto actualUserDto = mapper.readValue(actualResponse, new TypeReference<>() {
        });

        assertEquals(FIRST_NAME, actualUserDto.getFirstName());
        assertEquals(LAST_NAME, actualUserDto.getLastName());
        assertEquals(PHONE_NUMBER, actualUserDto.getPhoneNumber());

        verify(userService).add(any(User.class));
    }
}