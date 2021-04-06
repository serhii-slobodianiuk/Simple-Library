package com.slobodianiuk.library.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slobodianiuk.library.model.Book;
import com.slobodianiuk.library.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDto {

    @JsonProperty
    private String firstName;

    @JsonProperty
    private String lastName;

    @JsonProperty
    private String phoneNumber;

    @JsonProperty
    private List<BookDto> books;

    public UserDto(String firstName, String lastName, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.books = new ArrayList<>();
    }

    public static User toModel(UserDto dto) {
        User user = new User();
        user.setFirstName(dto.firstName);
        user.setLastName(dto.lastName);
        user.setPhoneNumber(dto.phoneNumber);
        return user;
    }

    public static UserDto fromModel(User user) {
        UserDto dto = new UserDto(user.getFirstName(), user.getLastName(), user.getPhoneNumber());
        List<Book> books = user.getBooks();
        if (books != null) {
            books.forEach(book -> dto.books.add(BookDto.fromModel(book)));
        }
        return dto;
    }

    public List<BookDto> getBooks() {
        return books;
    }

    public void setBooks(List<BookDto> books) {
        this.books = books;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
