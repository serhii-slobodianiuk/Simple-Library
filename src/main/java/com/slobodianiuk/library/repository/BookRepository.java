package com.slobodianiuk.library.repository;

import com.slobodianiuk.library.model.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    Book findByIsbn(String isbn);

    @Query("select b from Book b where b.user is NULL")
    List<Book> findAllAvailable();
}