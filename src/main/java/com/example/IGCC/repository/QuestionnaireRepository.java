package com.example.IGCC.repository;

import com.example.IGCC.entity.Questionnaire;
import com.example.IGCC.model.QuestionnaireResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface QuestionnaireRepository extends MongoRepository<Questionnaire, String> {
    @Query(value = "{flag:true}")
    List<QuestionnaireResponse> findAllFlagTrueQuestionnaire();
    @Query(value = "{}")
    List<QuestionnaireResponse> findAllQuestionnaire();


//    @Query("{$setField: {flag:false},}")
//    void updateAll();

}
