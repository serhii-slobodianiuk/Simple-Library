package com.slobodianiuk.library.service;

import com.google.common.collect.Lists;
import com.slobodianiuk.library.exception.NotFoundException;
import com.slobodianiuk.library.model.Book;
import com.slobodianiuk.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserBookService userBookService;

    @Override
    public Book add(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> getAllAvailable() {
        return Lists.newArrayList(bookRepository.findAllAvailable());
    }

    @Override
    public Book update(Book book) {

        Book foundBook = bookRepository.findByIsbn(book.getIsbn());
        if (foundBook != null) {
            if (book.getAuthor() != null) {
                foundBook.setAuthor(book.getAuthor());
            }
            if (book.getTitle() != null) {
                foundBook.setTitle(book.getTitle());
            }
            return bookRepository.save(foundBook);
        } else {
            throw new NotFoundException("Book for isbn " + book.getIsbn() + " not found");
        }
    }

    @Override
    public void delete(String isbn) {
        Book foundBook = bookRepository.findByIsbn(isbn);
        if (foundBook != null) {
            if (foundBook.getUser() != null) {
                userBookService.returnBook(foundBook.getUser().getPhoneNumber(), foundBook.getIsbn());
            }
            bookRepository.deleteById(foundBook.getId());
        } else {
            throw new NotFoundException("Book for isbn " + isbn + " not found");
        }
    }
}
