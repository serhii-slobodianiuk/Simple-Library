package com.slobodianiuk.library.service;

import com.slobodianiuk.library.exception.NotFoundException;
import com.slobodianiuk.library.model.Book;
import com.slobodianiuk.library.model.User;
import com.slobodianiuk.library.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    private static final long USER_ID = 123L;
    private static final String FIRST_NAME = "Freddie";
    private static final String LAST_NAME = "Mercury";
    private static final String PHONE_NUMBER = "+442084659275";

    private static final long BOOK_ID = 123L;
    private static final String TITLE = "Test title";
    private static final String AUTHOR = "Test author";
    private static final String ISBN = "111";


    @Mock
    UserRepository userRepository;

    @Mock
    UserBookService userBookService;

    @InjectMocks
    UserServiceImpl userService;

    User user;
    Book book;
    List<Book> listBook;

    @BeforeEach
    void init() {
        user = new User();
        user.setId(USER_ID);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setPhoneNumber(PHONE_NUMBER);

        book = new Book();
        book.setId(BOOK_ID);
        book.setIsbn(ISBN);
        book.setAuthor(AUTHOR);
        book.setTitle(TITLE);

        listBook = new ArrayList<>();
    }

    @Test
    void testAddUser() {
        when(userRepository.save(user)).thenReturn(user);
        User savedUser = userService.add(user);

        assertEquals(USER_ID, savedUser.getId());
        assertEquals(FIRST_NAME, savedUser.getFirstName());
        assertEquals(LAST_NAME, savedUser.getLastName());
        assertEquals(PHONE_NUMBER, savedUser.getPhoneNumber());
    }

    @Test
    void testGetAllUsers() {
        when(userService.getAll()).thenReturn(Collections.singletonList(user));

        List<User> foundUsers = userService.getAll();

        assertEquals(1, foundUsers.size());
        User foundUser = foundUsers.get(0);

        assertEquals(USER_ID, foundUser.getId());
        assertEquals(FIRST_NAME, foundUser.getFirstName());
        assertEquals(LAST_NAME, foundUser.getLastName());
        assertEquals(PHONE_NUMBER, foundUser.getPhoneNumber());

        verify(userRepository).findAll();
    }

    @Test
    void testUpdateUser() {
        User expectedUpdated = new User();
        expectedUpdated.setPhoneNumber(user.getPhoneNumber());
        expectedUpdated.setFirstName("new name");
        expectedUpdated.setLastName("new last_name");

        when(userRepository.findByPhoneNumber(PHONE_NUMBER)).thenReturn(user);
        when(userService.update(user)).thenReturn(expectedUpdated);

        User actualUpdated = userService.update(expectedUpdated);

        assertEquals(expectedUpdated.getFirstName(), actualUpdated.getFirstName());
        assertEquals(expectedUpdated.getLastName(), actualUpdated.getLastName());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdateUserThrowNotFoundError() {
        when(userRepository.findByPhoneNumber(PHONE_NUMBER)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> userService.update(user));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testDeleteUser() {
        listBook.add(book);
        user.setBooks(listBook);

        when(userRepository.findByPhoneNumber(PHONE_NUMBER)).thenReturn(user);
        userService.delete(PHONE_NUMBER);
        userBookService.returnBook(PHONE_NUMBER, ISBN);

        verify(userRepository).findByPhoneNumber(PHONE_NUMBER);
    }

    @Test
    void testDeleteUserAndThrowNotFoundError() {
        user.setId(USER_ID);

        when(userRepository.findByPhoneNumber(PHONE_NUMBER)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> userService.delete(PHONE_NUMBER));
        verify(userRepository, never()).deleteById(USER_ID);
    }
}
