package com.slobodianiuk.library.repository;

import com.slobodianiuk.library.model.User;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByPhoneNumber(String phoneNumber);
}
