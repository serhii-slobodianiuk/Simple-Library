package com.slobodianiuk.library.controller;

import com.slobodianiuk.library.dto.UserDto;
import com.slobodianiuk.library.model.User;
import com.slobodianiuk.library.repository.BookRepository;
import com.slobodianiuk.library.repository.UserRepository;
import com.slobodianiuk.library.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController()
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public UserController(UserService userService, UserRepository userRepository, BookRepository bookRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @GetMapping("/user")
    public ResponseEntity<Object> getAllUsers() {
        try {
            List<User> users = userService.getAll();
            return ResponseEntity.ok(users.stream().map(UserDto::fromModel).collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/user")
    public ResponseEntity<Object> addUser(@RequestBody UserDto dto) {
        try {
            User user = userService.add(UserDto.toModel(dto));
            return ResponseEntity.ok(UserDto.fromModel(user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/user")
    public ResponseEntity<Object> updateUser(@RequestBody UserDto dto) {
        try {
            User user = userService.update(UserDto.toModel(dto));
            return ResponseEntity.ok(UserDto.fromModel(user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/user")
    public ResponseEntity<Object> deleteUser(@RequestParam("phoneNumber") String phoneNumber) {
        try {
            userService.delete(phoneNumber);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
