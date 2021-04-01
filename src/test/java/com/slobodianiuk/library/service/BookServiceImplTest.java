package com.slobodianiuk.library.service;

import com.slobodianiuk.library.exception.NotFoundException;
import com.slobodianiuk.library.model.Book;
import com.slobodianiuk.library.model.User;
import com.slobodianiuk.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    private static final String TITLE = "Test title";
    private static final String AUTHOR = "Test author";
    private static final String ISBN = "12300857251";
    private static final Long ID = 123L;
    private static final String PHONE_NUMBER = "12345";

    @Mock
    BookRepository repository;

    @Mock
    UserBookService userBookService;


    @InjectMocks
    BookServiceImpl bookService;

    Book book;

    @BeforeEach
    void init() {
        book = new Book();
        book.setTitle(TITLE);
        book.setAuthor(AUTHOR);
        book.setIsbn(ISBN);
    }

    @Test
    void testAddBook() {

        book.setId(ID);

        when(repository.save(book)).thenReturn(book);
        Book saved = bookService.add(book);

        assertEquals(ID, saved.getId());
        assertEquals(TITLE, saved.getTitle());
        assertEquals(AUTHOR, saved.getAuthor());
        assertEquals(ISBN, saved.getIsbn());
    }

    @Test
    void testGetAllAvailableBooks() {

        book.setId(ID);

        when(repository.findAllAvailable()).thenReturn(singletonList(book));

        List<Book> foundBooks = bookService.getAllAvailable();

        assertEquals(1, foundBooks.size());
        Book foundBook = foundBooks.get(0);

        assertEquals(ID, foundBook.getId());
        assertEquals(TITLE, foundBook.getTitle());
        assertEquals(AUTHOR, foundBook.getAuthor());
        assertEquals(ISBN, foundBook.getIsbn());

        verify(repository, times(1)).findAllAvailable();
    }

    @Test
    void testUpdateBook() {

        book.setId(ID);

        Book expectedUpdated = new Book();
        expectedUpdated.setIsbn(book.getIsbn());
        expectedUpdated.setAuthor("new author");
        expectedUpdated.setTitle("new title");

        when(repository.findByIsbn(ISBN)).thenReturn(book);
        when(repository.save(book)).thenReturn(expectedUpdated);
        Book actualUpdated = bookService.update(expectedUpdated);

        assertEquals(expectedUpdated.getAuthor(), actualUpdated.getAuthor());
        assertEquals(expectedUpdated.getTitle(), actualUpdated.getTitle());
        verify(repository, times(1)).save(any(Book.class));
    }

    @Test
    void testUpdateBookThrowNotFoundError() {

        book.setId(ID);

        when(repository.findByIsbn(ISBN)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> bookService.update(book));
        verify(repository, never()).save(any(Book.class));
    }

    @Test
    void testDeleteAndReturnBook() {

        User user = new User();
        user.setPhoneNumber(PHONE_NUMBER);

        book.setId(ID);
        book.setUser(user);

        when(repository.findByIsbn(ISBN)).thenReturn(book);
        doNothing().when(userBookService).returnBook(PHONE_NUMBER, ISBN);
        doNothing().when(repository).deleteById(ID);

        bookService.delete(ISBN);

        verify(userBookService, times(1)).returnBook(PHONE_NUMBER, ISBN);
        verify(repository, times(1)).deleteById(ID);
    }

    @Test
    void testDeleteAndReturnBookThrowNotFoundError() {

        User user = new User();
        user.setPhoneNumber(PHONE_NUMBER);

        book.setId(ID);
        book.setUser(user);

        when(repository.findByIsbn(ISBN)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> bookService.delete(ISBN));

        verify(userBookService, never()).returnBook(PHONE_NUMBER, ISBN);
        verify(repository, never()).deleteById(ID);
    }
}
