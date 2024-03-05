package com.example.IGCC.repository;

import com.example.IGCC.model.Questionnaire;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface QuestionnaireRepository extends MongoRepository<Questionnaire, String> {
    @Query(value = "{}")
    List<Questionnaire> findAllQuestionnaire();
}
