package com.example.IGCC.service;

import com.example.IGCC.model.Questionnaire;
import com.example.IGCC.model.QuestionnaireComponent;
import com.example.IGCC.model.User;
import com.example.IGCC.model.UserAnswerResponse;
import com.example.IGCC.repository.QuestionnaireRepository;
import com.example.IGCC.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private QuestionnaireRepository questionnaireRepository;
    @Autowired
    private UserRepository userRepository;
    public List<Questionnaire> userAndQuestionnaire(String email){
        List<Questionnaire> questionnaires = questionnaireRepository.findAllQuestionnaire();
        User user=userRepository.findByEmail(email);
        if(user.getResponseKey()==false){
            return questionnaires;
        }else{
            for(Questionnaire questionnaire:questionnaires){
                for(QuestionnaireComponent component:questionnaire.getComponents()){
                    for (UserAnswerResponse userAnswerResponse:user.getUserAnswerResponse()){
                        if(userAnswerResponse.getQuestionId().equals(component.getQuestionId())){
                            component.setResponse(userAnswerResponse.getResponse());
                        }
                    }
                }
            }
            return questionnaires;
        }
    }

    public User userAndQuestionnaireSave(List<Questionnaire> questionnaires,String email){
        User user=userRepository.findByEmail(email);
        List<UserAnswerResponse> userAnswerResponses=new ArrayList<>();
        if(user.getResponseKey()==false){
            user.setResponseKey(true);
            for(Questionnaire questionnaire:questionnaires){
                for(QuestionnaireComponent component:questionnaire.getComponents()){
                    userAnswerResponses.add(new UserAnswerResponse(component.getQuestionId(),component.getResponse()));
                }
            }
        }else{
            for(Questionnaire questionnaire:questionnaires){
                for(QuestionnaireComponent component:questionnaire.getComponents()){
                    for (UserAnswerResponse userAnswerResponse:user.getUserAnswerResponse()){
                        if(userAnswerResponse.getQuestionId().equals(component.getQuestionId())){
                            userAnswerResponses.add(new UserAnswerResponse(component.getQuestionId(),component.getResponse()));
                        }
                    }
                }
            }
        }
        user.setUserAnswerResponse(userAnswerResponses);
        return user;
    }
}
