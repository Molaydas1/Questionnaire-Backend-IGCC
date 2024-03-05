package com.example.IGCC.controller;

import com.example.IGCC.exception.NoRecordsFoundExcption;
import com.example.IGCC.model.Questionnaire;
import com.example.IGCC.model.User;
import com.example.IGCC.model.UserAnswerResponse;
import com.example.IGCC.repository.QuestionnaireRepository;
import com.example.IGCC.repository.UserRepository;
import com.example.IGCC.service.UserService;
import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    public ResponseEntity<String> createUser(@RequestBody User user) {
        try {
            user.setId(UUID.randomUUID().toString());
            user.setResponseKey(false);
            user.setUserAnswerResponse(new ArrayList<>());
            userRepository.save(user);
            log.info("create user {}", user);

            String completeUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/User/getAllQuestionnaire")
                    .toUriString();

            return ResponseEntity.ok("The complete URL is: " + completeUrl);
//            return ResponseEntity.ok("create user successfully.");
        } catch (Exception e) {
            log.info("Execption occurs {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to user create.");
        }
    }
//    @GetMapping("/sandEmail")
//    public ResponseEntity<String> getResource(@RequestParam("email") String email) {
//        String completeUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path("/User/getAllQuestionnaire")
//                .toUriString();
//
//        return ResponseEntity.ok("The complete URL is: " + completeUrl);
//    }
    @GetMapping("/get")
    public ResponseEntity<List<Questionnaire>> getAllQuestionnaire(@RequestParam("email") String email) {
        List<Questionnaire> questionnaires = userService.userAndQuestionnaire(email);
        if (questionnaires.isEmpty()) {
            log.info("no records found");
            throw new NoRecordsFoundExcption("no records found");
        }
        log.info("get all question for email:- {} ",email);
        return ResponseEntity.ok().body(questionnaires);
    }
    @PostMapping("/save")
    public ResponseEntity<String> saveAllQuestionnaire(@RequestBody List<Questionnaire> questionnaires,@RequestParam("email") String email) {
        User user=userService.userAndQuestionnaireSave(questionnaires,email);

        userRepository.save(user);
        return ResponseEntity.ok("save the all Questionnaire for user");
    }
}
