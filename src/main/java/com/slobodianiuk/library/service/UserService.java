package com.slobodianiuk.library.service;

import com.slobodianiuk.library.model.User;

import java.util.List;

public interface UserService {

    User add(User user);

    List<User> getAll();

    User update(User user);

    void delete(String phoneNumber);
}