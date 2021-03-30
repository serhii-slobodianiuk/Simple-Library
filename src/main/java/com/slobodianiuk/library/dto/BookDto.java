package com.slobodianiuk.library.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slobodianiuk.library.model.Book;

public class BookDto {

    @JsonProperty
    private String title;

    @JsonProperty
    private String author;

    @JsonProperty
    private String isbn;

    public BookDto(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    public static BookDto fromModel(Book book) {
        return new BookDto(book.getTitle(), book.getAuthor(), book.getIsbn());
    }

    public static Book toModel(BookDto dto) {
        Book book = new Book();
        book.setIsbn(dto.isbn);
        book.setAuthor(dto.author);
        book.setTitle(dto.title);
        return book;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
