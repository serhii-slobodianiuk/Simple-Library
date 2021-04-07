package com.slobodianiuk.library.dto;

import com.slobodianiuk.library.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class BookDtoTest {

    private static final String TITLE = "Test title";
    private static final String AUTHOR = "Test author";
    private static final String ISBN = "12300857251";
    private static final Long ID = 123L;

    Book book;
    BookDto bookDto;

    @BeforeEach
    void init() {
        book = new Book();
        book.setTitle(TITLE);
        book.setAuthor(AUTHOR);
        book.setIsbn(ISBN);

        bookDto = new BookDto(
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn());
    }

    @Test
    void testToModel() {
        try (MockedStatic<BookDto> userDtoMockedStatic = Mockito.mockStatic(BookDto.class)) {
            userDtoMockedStatic.when(() -> BookDto.toModel(bookDto)).thenReturn(book);

            Book actualBook = BookDto.toModel(bookDto);
            assertEquals(book, actualBook);
        }
    }
}
