package com.example.IGCC.repository;

import com.example.IGCC.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<User, String> {
    @Query(value = "{email:'?0'}")
    User findByEmail(String email);
}
