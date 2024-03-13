package com.example.IGCC.service;

import com.example.IGCC.exception.NoRecordsFoundExcption;
import com.example.IGCC.model.Questionnaire;
import com.example.IGCC.model.QuestionnaireComponent;
import com.example.IGCC.model.User;
import com.example.IGCC.model.UserAnswerResponse;
import com.example.IGCC.repository.QuestionnaireRepository;
import com.example.IGCC.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserService {
    @Autowired
    private QuestionnaireRepository questionnaireRepository;
    @Autowired
    private UserRepository userRepository;
    public void createUser(User user){
        User newUser=userRepository.findByEmail(user.getEmail());
        if(newUser!=null){
            log.info("already user exists{}", newUser);
            throw new NoRecordsFoundExcption("already user exists ");
        }else{
            log.info("create user {}", user);
            user.setUserAnswerResponse(new ArrayList<>());
            userRepository.save(user);
        }
    }
    public void userAndQuestionnaireSave(List<Questionnaire> questionnaires,String email){
        User user=userRepository.findByEmail(email);
        List<UserAnswerResponse> userAnswerResponses=new ArrayList<>();
        for(Questionnaire questionnaire:questionnaires){
            for(QuestionnaireComponent component:questionnaire.getComponents()){
                userAnswerResponses.add(new UserAnswerResponse(questionnaire.getId()+
                        "."+component.getQuestionId(),
                        component.getResponse()));
            }
        }
        user.setUserAnswerResponse(userAnswerResponses);
        userRepository.save(user);
    }
}
