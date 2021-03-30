package com.slobodianiuk.library.service;

import com.slobodianiuk.library.exception.BusinessServiceException;
import com.slobodianiuk.library.model.Book;
import com.slobodianiuk.library.model.User;
import com.slobodianiuk.library.repository.BookRepository;
import com.slobodianiuk.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserBookServiceImpl implements UserBookService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Override
    public void takeBook(String phoneNumber, String isbn) {
        User user = userRepository.findByPhoneNumber(phoneNumber);
        if (user == null) {
            throw new BusinessServiceException("User for phoneNumber " + phoneNumber + " not found");
        }

        List<Book> userBooks = user.getBooks().stream()
                .filter(book -> book.getIsbn().equals(isbn)).collect(Collectors.toList());

        if (userBooks.isEmpty()) {
            Book book = bookRepository.findByIsbn(isbn);
            if (book == null) {
                throw new BusinessServiceException("Book for isbn " + isbn + " not found");
            }
            book.setUser(user);
            bookRepository.save(book);
        } else {
            throw new BusinessServiceException("Book for isbn " + isbn +
                    " already has been taken by user with phoneNumber " + phoneNumber);
        }
    }

    @Override
    public void returnBook(String phoneNumber, String isbn) {
        User user = userRepository.findByPhoneNumber(phoneNumber);
        if (user == null) {
            throw new BusinessServiceException("User for phoneNumber " + phoneNumber + " not found");
        }

        List<Book> userBooks = user.getBooks().stream()
                .filter(book -> book.getIsbn().equals(isbn)).collect(Collectors.toList());

        if (!userBooks.isEmpty()) {
            Book book = userBooks.get(0);
            book.setUser(null);
            bookRepository.save(book);
        } else {
            throw new BusinessServiceException("Book for isbn " + isbn +
                    " has not been taken by user with phoneNumber " + phoneNumber);
        }
    }
}
