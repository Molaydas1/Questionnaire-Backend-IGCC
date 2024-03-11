package com.example.IGCC.service;

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
import java.util.UUID;

@Service
@Slf4j
public class UserService {
    @Autowired
    private QuestionnaireRepository questionnaireRepository;
    @Autowired
    private UserRepository userRepository;
    public List<Questionnaire> createUserService(User user){
        User newUser=userRepository.findByEmail(user.getEmail());
        List<Questionnaire> questionnaires = questionnaireRepository.findAllQuestionnaire();
        if(newUser!=null){
            log.info("already user exists{}", newUser);
            user=newUser;
            boolean count;
            for(Questionnaire questionnaire:questionnaires){
                for(QuestionnaireComponent component:questionnaire.getComponents()){
                    count=false;
                    for (UserAnswerResponse userAnswerResponse:user.getUserAnswerResponse()){
                        if(userAnswerResponse.getQuestionId().equals(component.getQuestionId())){
                            component.setResponse(userAnswerResponse.getResponse());
                            count=true;
                        }else if (count==true){
                            break;
                        }
                    }if(count==false){
                        List<Boolean> list=new ArrayList<>();
                        for(String s:component.getQuestionOpposite()){
                            list.add(false);
                        }
                        component.setResponse(list);
                    }
                }
            }
            return questionnaires;
        }
        for(Questionnaire questionnaire:questionnaires){
            for(QuestionnaireComponent component:questionnaire.getComponents()){
                List<Boolean> list=new ArrayList<>();
                for(String s:component.getQuestionOpposite()){
                    list.add(false);
                }
                component.setResponse(list);
            }
        }
        log.info("create user {}", user);
        user.setUserAnswerResponse(new ArrayList<>());
        userRepository.save(user);
        return questionnaires;
    }

    public User userAndQuestionnaireSave(List<Questionnaire> questionnaires){
        User user=userRepository.findByEmail("sbdsbc4");
        List<UserAnswerResponse> userAnswerResponses=new ArrayList<>();
        if(user.getUserAnswerResponse().isEmpty()){
            for(Questionnaire questionnaire:questionnaires){
                for(QuestionnaireComponent component:questionnaire.getComponents()){
                    List<Boolean> list=new ArrayList<>();
                    for(String s:component.getQuestionOpposite()){
                        list.add(false);
                    }
                    userAnswerResponses.add(new UserAnswerResponse(component.getQuestionId(),list));
                }
            }
        }else{
            boolean count;
            int i=0;
            userAnswerResponses=user.getUserAnswerResponse();
            for(Questionnaire questionnaire:questionnaires){
                for(QuestionnaireComponent component:questionnaire.getComponents()){
                    count=false;
                    for(UserAnswerResponse userAnswerResponse:userAnswerResponses){
                        if(userAnswerResponse.getQuestionId().equals(component.getQuestionId())){
                            count=true;
                            userAnswerResponses.set(i,new UserAnswerResponse(userAnswerResponse.getQuestionId(),component.getResponse()));
                        }else if (count==true){
                            break;
                        }
                    }if(count==false){
                        userAnswerResponses.add(new UserAnswerResponse(component.getQuestionId(),component.getResponse()));
                    }
                    i++;
                }
            }
        }
        user.setUserAnswerResponse(userAnswerResponses);
        return user;
    }
}
