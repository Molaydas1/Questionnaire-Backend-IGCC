package com.example.IGCC.repository;

import com.example.IGCC.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
