package com.slobodianiuk.library.service;

import com.slobodianiuk.library.model.Book;

import java.util.List;

public interface BookService {

    Book add(Book book);

    List<Book> getAllAvailable();

    Book update(Book book);

    void delete(String isbn);

}
