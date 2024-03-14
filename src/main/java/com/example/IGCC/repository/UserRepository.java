package com.example.IGCC.repository;

import com.example.IGCC.entity.User;
import com.example.IGCC.model.UserResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
    @Query(value = "{}")
    List<UserResponse> findAllUser();
}
