package com.example.IGCC.repository;

import com.example.IGCC.model.OtpModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OtpRepository extends MongoRepository<OtpModel, String> {
}