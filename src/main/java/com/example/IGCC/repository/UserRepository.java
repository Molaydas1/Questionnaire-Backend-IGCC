package com.example.IGCC.repository;

import com.example.IGCC.model.Questionnaire;
import com.example.IGCC.model.User;
import com.example.IGCC.model.UserAnswerResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    @Query(value = "{email:'?0'}")
    User findByEmail(String email);
}
