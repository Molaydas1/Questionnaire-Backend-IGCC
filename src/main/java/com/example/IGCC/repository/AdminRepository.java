package com.example.IGCC.repository;

import com.example.IGCC.model.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AdminRepository extends MongoRepository<Admin, String> {
//    Admin findByEmail(String email);
    Optional<Admin> findByEmail(String email);
}
