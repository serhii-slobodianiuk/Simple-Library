package com.slobodianiuk.library.service;

import com.google.common.collect.Lists;
import com.slobodianiuk.library.exception.NotFoundException;
import com.slobodianiuk.library.model.Book;
import com.slobodianiuk.library.model.User;
import com.slobodianiuk.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserBookService userBookService;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User add(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getAll() {
        return Lists.newArrayList(userRepository.findAll());
    }

    @Override
    public User update(User user) {
        User foundUser = userRepository.findByPhoneNumber(user.getPhoneNumber());
        if (foundUser != null) {
            if (user.getFirstName() != null) {
                foundUser.setFirstName(user.getFirstName());
            }
            if (user.getLastName() != null) {
                foundUser.setLastName(user.getLastName());
            }
            return userRepository.save(foundUser);
        } else {
            throw new NotFoundException("User for phone number " + user.getPhoneNumber() + " not found");
        }
    }

    @Override
    public void delete(String phoneNumber) {
        User foundUser = userRepository.findByPhoneNumber(phoneNumber);
        if (foundUser != null) {
            List<Book> books = foundUser.getBooks();
            if (books != null) {
                books.forEach(book ->
                        userBookService.returnBook(phoneNumber, book.getIsbn()));
            }
            userRepository.deleteById(foundUser.getId());
        } else {
            throw new NotFoundException("User for phoneNumber " + phoneNumber + " not found");
        }
    }

}