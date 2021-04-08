package com.slobodianiuk.library.service;

import com.slobodianiuk.library.exception.BusinessServiceException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserBookServiceImplTest {

    private static final long USER_ID = 123L;
    private static final String FIRST_NAME = "Freddie";
    private static final String LAST_NAME = "Mercury";
    private static final String PHONE_NUMBER = "+442084659275";

    private static final Long BOOK_ID = 123L;
    private static final String ISBN = "55555";
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
        user.setId(USER_ID);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setPhoneNumber(PHONE_NUMBER);

        book = new Book();
        book.setId(BOOK_ID);
        book.setIsbn(ISBN);
        book.setAuthor(AUTHOR);
        book.setTitle(TITLE);

        listBook = new ArrayList<>(1);
    }

    @Test
    void testTakeBook() {
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

    @Test
    void testTakeBookAndThrowBusinessServiceException() {
        user.setBooks(listBook);

        Book book1 = new Book();
        book1.setIsbn("55555");
        book1.setTitle("qwerty");
        book1.setAuthor("Me");
        List<Book> ls = new ArrayList<>();
        ls.add(book1);

        User user1 = new User();
        user1.setPhoneNumber(PHONE_NUMBER);
        user1.setBooks(ls);

        when(userRepository.findByPhoneNumber(PHONE_NUMBER))
                .thenReturn(user).thenReturn(null).thenReturn(user1);

        assertThrows(BusinessServiceException.class, () -> userBookService.takeBook(PHONE_NUMBER, ISBN));
        assertThrows(BusinessServiceException.class, () -> userBookService.takeBook(PHONE_NUMBER, ISBN));
        assertThrows(BusinessServiceException.class, () -> userBookService.takeBook(PHONE_NUMBER, ISBN));

        verify(userRepository, times(3)).findByPhoneNumber(PHONE_NUMBER);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void testReturnBook() {

        listBook.add(book);
        user.setBooks(listBook);

        when(userRepository.findByPhoneNumber(PHONE_NUMBER)).thenReturn(user);
        when(bookRepository.save(book)).thenReturn(book);

        userBookService.returnBook(PHONE_NUMBER, ISBN);

        assertEquals(PHONE_NUMBER, user.getPhoneNumber());
        assertEquals(ISBN, book.getIsbn());

        verify(userRepository).findByPhoneNumber(PHONE_NUMBER);
        verify(bookRepository).save(book);
    }

    @Test
    void testReturnBookAndThrowBusinessServiceException() {
        user.setBooks(listBook);

        when(userRepository.findByPhoneNumber(PHONE_NUMBER)).thenReturn(user).thenReturn(null);

        assertThrows(BusinessServiceException.class, () -> userBookService.returnBook(PHONE_NUMBER, ISBN));
        assertThrows(BusinessServiceException.class, () -> userBookService.returnBook(PHONE_NUMBER, ISBN));

        verify(userRepository, times(2)).findByPhoneNumber(PHONE_NUMBER);
        verify(bookRepository, never()).save(any(Book.class));

    }
}