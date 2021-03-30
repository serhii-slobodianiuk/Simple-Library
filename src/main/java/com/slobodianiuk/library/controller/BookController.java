package com.slobodianiuk.library.controller;

import com.slobodianiuk.library.dto.BookDto;
import com.slobodianiuk.library.exception.NotFoundException;
import com.slobodianiuk.library.model.Book;
import com.slobodianiuk.library.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/book")
    public ResponseEntity<List<BookDto>> getAllBooks() {

        return ResponseEntity
                .ok(bookService
                        .getAllAvailable()
                        .stream()
                        .map(BookDto::fromModel)
                        .collect(Collectors.toList()));
    }

    @PostMapping("/book")
    public ResponseEntity<Object> addBook(@RequestBody BookDto dto) {
        try {
            Book book = bookService.add(BookDto.toModel(dto));
            return ResponseEntity.ok(BookDto.fromModel(book));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/book")
    public ResponseEntity<Object> updateBook(@RequestBody BookDto dto) {
        try {
            Book book = bookService.update(BookDto.toModel(dto));
            return ResponseEntity.ok(BookDto.fromModel(book));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/book")
    public ResponseEntity<Object> deleteBook(@RequestParam("isbn") String isbn) {
        try {
            bookService.delete(isbn);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
