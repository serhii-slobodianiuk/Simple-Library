package com.slobodianiuk.library.controller;

import com.slobodianiuk.library.dto.UserBookDto;
import com.slobodianiuk.library.exception.BusinessServiceException;
import com.slobodianiuk.library.service.UserBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserBookController {

    @Autowired
    private UserBookService userBookService;

    @PostMapping("/userBook")
    public ResponseEntity<Object> takeBook(@RequestBody UserBookDto dto) {
        try {
            userBookService.takeBook(dto.getPhoneNumber(), dto.getIsbn());
            return ResponseEntity.ok().build();
        } catch (BusinessServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/userBook")
    public ResponseEntity<Object> returnBook(@RequestParam("phone") String phoneNumber,
                                             @RequestParam("isbn") String isbn) {
        try {
            userBookService.returnBook(phoneNumber, isbn);
            return ResponseEntity.ok().build();
        } catch (BusinessServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
