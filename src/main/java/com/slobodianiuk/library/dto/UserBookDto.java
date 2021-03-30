package com.slobodianiuk.library.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserBookDto {

    @JsonProperty
    private String phoneNumber;

    @JsonProperty
    private String isbn;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public String toString() {
        return "UserBookDto{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", isbn='" + isbn + '\'' +
                '}';
    }
}