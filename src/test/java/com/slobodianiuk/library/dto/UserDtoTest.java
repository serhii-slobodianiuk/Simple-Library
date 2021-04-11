package com.slobodianiuk.library.dto;

import com.slobodianiuk.library.model.Book;
import com.slobodianiuk.library.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class UserDtoTest {

    private static final Long USER_ID = 123L;
    private static final String FIRST_NAME = "Freddie";
    private static final String LAST_NAME = "Mercury";
    private static final String PHONE_NUMBER = "+44208465927";

    private static final Long BOOK_ID = 324L;
    private static final String ISBN = "55555";
    private static final String TITLE = "Fairy Tales";
    private static final String AUTHOR = "Folklore";

    User user;
    UserDto userDto;
    Book book;
    BookDto bookDto;
    List<Book> listBook;

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
        assertThat(user = UserDto.toModel(userDto)).isEqualTo(user);
    }

    @Test
    void testFromModel() {
        book = new Book();
        book.setId(BOOK_ID);
        book.setIsbn(ISBN);
        book.setAuthor(AUTHOR);
        book.setTitle(TITLE);

        bookDto = new BookDto(
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn());

        listBook = new ArrayList<>();
        listBook.add(book);
        user.setBooks(listBook);

        try (MockedStatic<BookDto> userDtoMockedStatic = Mockito.mockStatic(BookDto.class)) {
            userDtoMockedStatic.when(() -> BookDto.fromModel(any(Book.class))).thenReturn(bookDto);

            List<Book> books = user.getBooks();
            Book actualBook = books.get(0);

            assertEquals(1, books.size());
            assertEquals(actualBook, book);
            assertThat(userDto = UserDto.fromModel(user)).isEqualTo(userDto);
        }
    }
}