package com.example.IGCC.service;

import com.example.IGCC.exception.MyFileNotFoundException;
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
import java.util.UUID;

@Service
@Slf4j
public class UserService {
    @Autowired
    private QuestionnaireRepository questionnaireRepository;
    @Autowired
    private UserRepository userRepository;
    public void createUserService(User user){
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
}
