package com.slobodianiuk.library.service;

public interface UserBookService {

    void takeBook(String phoneNumber, String isbn);

    void returnBook(String phoneNumber, String isbn);
}
