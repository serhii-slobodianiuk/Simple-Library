package com.slobodianiuk.library.service;

import com.slobodianiuk.library.model.Book;
import com.slobodianiuk.library.model.User;
import com.slobodianiuk.library.repository.BookRepository;
import com.slobodianiuk.library.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserBookServiceImplTest {

    private static final long USER_ID = 123L;
    private static final String FIRST_NAME = "Freddie";
    private static final String LAST_NAME = "Mercury";
    private static final String PHONE_NUMBER = "+442084659275";

    private static final Long BOOK_ID = 123L;
    private static final String ISBN = "12300857251";
    private static final String TITLE = "Fairy Tales";
    private static final String AUTHOR = "Folklore";

    @Mock
    UserRepository userRepository;

    @Mock
    BookRepository bookRepository;

    @InjectMocks
    UserBookServiceImpl userBookService;

    User user;
    Book book;
    List<Book> listBook;

    @BeforeEach
    void init() {
        user = new User();
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setPhoneNumber(PHONE_NUMBER);

        book = new Book();
        book.setIsbn(ISBN);
        book.setAuthor(AUTHOR);
        book.setTitle(TITLE);

        listBook = new ArrayList<>();
    }

    @Test
    void testTakeBook() {

        user.setId(USER_ID);
        book.setId(BOOK_ID);

        Book book1 = new Book();
        book1.setIsbn("9999999");
        book1.setTitle("qwerty");
        book1.setAuthor("Me");

        listBook.add(book1);
        user.setBooks(listBook);

        when(userRepository.findByPhoneNumber(PHONE_NUMBER)).thenReturn(user);
        when(bookRepository.findByIsbn(ISBN)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);

        userBookService.takeBook(user.getPhoneNumber(), book.getIsbn());

        assertEquals(PHONE_NUMBER, user.getPhoneNumber());
        assertEquals(ISBN, book.getIsbn());

        verify(userRepository, times(1)).findByPhoneNumber(anyString());
        verify(bookRepository, times(1)).findByIsbn(anyString());
        verify(bookRepository, times(1)).save(any(Book.class));

    }
}