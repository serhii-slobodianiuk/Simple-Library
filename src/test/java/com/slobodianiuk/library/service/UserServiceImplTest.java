package com.slobodianiuk.library.service;

import com.slobodianiuk.library.exception.NotFoundException;
import com.slobodianiuk.library.model.User;
import com.slobodianiuk.library.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    private static final long ID = 123L;
    private static final String FIRST_NAME = "Freddie";
    private static final String LAST_NAME = "Mercury";
    private static final String PHONE_NUMBER = "+442084659275";

    @Mock
    UserRepository repository;

    @InjectMocks
    UserServiceImpl userService;

    User user;

    @BeforeEach
    void init() {
        user = new User();
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setPhoneNumber(PHONE_NUMBER);
    }

    @Test
    void testAddUser() {
        user.setId(ID);

        when(repository.save(user)).thenReturn(user);
        User savedUser = userService.add(user);

        assertEquals(ID, savedUser.getId());
        assertEquals(FIRST_NAME, savedUser.getFirstName());
        assertEquals(LAST_NAME, savedUser.getLastName());
        assertEquals(PHONE_NUMBER, savedUser.getPhoneNumber());
    }

    @Test
    void testGetAllUsers() {
        user.setId(ID);

        when(userService.getAll()).thenReturn(Collections.singletonList(user));

        List<User> foundUsers = userService.getAll();

        assertEquals(1, foundUsers.size());
        User foundUser = foundUsers.get(0);

        assertEquals(ID, foundUser.getId());
        assertEquals(FIRST_NAME, foundUser.getFirstName());
        assertEquals(LAST_NAME, foundUser.getLastName());
        assertEquals(PHONE_NUMBER, foundUser.getPhoneNumber());

        verify(repository, times(1)).findAll();
    }

    @Test
    void testUpdateUser() {
        user.setId(ID);

        User expectedUpdated = new User();
        expectedUpdated.setPhoneNumber(user.getPhoneNumber());
        expectedUpdated.setFirstName("new name");
        expectedUpdated.setLastName("new last_name");

        when(repository.findByPhoneNumber(PHONE_NUMBER)).thenReturn(user);
        when(userService.update(user)).thenReturn(expectedUpdated);

        User actualUpdated = userService.update(expectedUpdated);

        assertEquals(expectedUpdated.getFirstName(), actualUpdated.getFirstName());
        assertEquals(expectedUpdated.getLastName(), actualUpdated.getLastName());
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    void deleteUser() {
        user.setId(ID);

        when(repository.findByPhoneNumber(PHONE_NUMBER)).thenReturn(user);

        userService.delete(PHONE_NUMBER);
        verify(repository, times(1)).deleteById(ID);
    }

    @Test
    void testUpdateUserThrowNotFoundError() {
        user.setId(ID);

        when(repository.findByPhoneNumber(PHONE_NUMBER)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> userService.update(user));
        verify(repository, never()).save(any(User.class));
    }

    @Test
    void testDeleteUserAndThrowNotFoundError() {
        user.setId(ID);

        when(repository.findByPhoneNumber(PHONE_NUMBER)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> userService.delete(PHONE_NUMBER));
        verify(repository, never()).deleteById(ID);
    }
}
