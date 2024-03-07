package com.example.IGCC.controller;

import com.example.IGCC.exception.NoRecordsFoundExcption;
import com.example.IGCC.model.Questionnaire;
import com.example.IGCC.model.User;
import com.example.IGCC.repository.QuestionnaireRepository;
import com.example.IGCC.repository.UserRepository;
import com.example.IGCC.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/User")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionnaireRepository questionnaireRepository;

    @PostMapping("/create")
    public ResponseEntity<List<Questionnaire>> createUser(@RequestBody User user) {
        List<Questionnaire> questionnaires = userService.createUserService(user);
        if (questionnaires.isEmpty()) {
            log.info("no records found");
            throw new NoRecordsFoundExcption("no records found");
        }
        log.info("all question sand");
        return ResponseEntity.ok().body(questionnaires);
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveAllQuestionnaire(@RequestBody List<Questionnaire> questionnaires) {
        User user=userService.userAndQuestionnaireSave(questionnaires);

        userRepository.save(user);
        log.info("save the all Questionnaire for user");
        return ResponseEntity.ok("save the all Questionnaire for user");
    }
}
