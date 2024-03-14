package com.example.IGCC.repository;

import com.example.IGCC.model.CountryOrSectorRisks;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;


public interface CountryOrSectorRisksRepository extends MongoRepository<CountryOrSectorRisks, String> {
    List<CountryOrSectorRisks> findAllByCountry(String country);
}