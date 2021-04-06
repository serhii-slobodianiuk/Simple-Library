package com.slobodianiuk.library;

import com.slobodianiuk.library.controller.BookController;
import com.slobodianiuk.library.controller.UserBookController;
import com.slobodianiuk.library.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class SimpleLibraryApplicationTests {

    @Autowired
    UserController userController;

    @Autowired
    BookController bookController;

    @Autowired
    UserBookController userBookController;

    @Test
    void contextLoads() {
        assertThat(userController).isNotNull();
        assertThat(bookController).isNotNull();
        assertThat(userBookController).isNotNull();
    }
}