package com.slobodianiuk.library.dto;

import com.slobodianiuk.library.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
public class UserDtoTest {

    private static final Long USER_ID = 123L;
    private static final String FIRST_NAME = "Freddie";
    private static final String LAST_NAME = "Mercury";
    private static final String PHONE_NUMBER = "+44208465927";

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
    void testToModel() {
        try (MockedStatic<UserDto> userDtoMockedStatic = Mockito.mockStatic(UserDto.class)) {
            userDtoMockedStatic.when(() -> UserDto.toModel(userDto)).thenReturn(user);

            User actualUser = UserDto.toModel(userDto);
            assertEquals(user, actualUser);
        }
    }

    @Test
    void testFromModel() {

        try (MockedStatic<UserDto> userDtoMockedStatic = Mockito.mockStatic(UserDto.class)) {
            userDtoMockedStatic.when(() -> UserDto.fromModel(user)).thenReturn(userDto);

            UserDto actualUserDto = UserDto.fromModel(user);
            assertEquals(userDto, actualUserDto);
        }
    }
}
