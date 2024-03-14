package com.example.IGCC.controller;

import com.example.IGCC.entity.User;
import com.example.IGCC.model.ApiResponse;
import com.example.IGCC.model.QuestionnaireResponse;
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
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        userService.createUser(user);
        return new ResponseEntity<>(new ApiResponse<>(true,"user create successfully",null), HttpStatus.OK);
    }
    @PostMapping("/save")
    public ResponseEntity<?> allQuestionnaireSaveUser(
            @RequestBody List<QuestionnaireResponse> questionnaires, @RequestParam("email") String email) {
        userService.userAndQuestionnaireSaveAndSubmit(questionnaires,email,false);
        return new ResponseEntity<>(new ApiResponse<>(true,"save the all response successfully",null), HttpStatus.OK);
    }
    @PostMapping("/submit")
    public ResponseEntity<?> allQuestionnaireSubmitUser(
            @RequestBody List<QuestionnaireResponse> questionnaires, @RequestParam("email") String email) {
        userService.userAndQuestionnaireSaveAndSubmit(questionnaires,email,true);
        return new ResponseEntity<>(new ApiResponse<>(true,"submit the all response successfully",null), HttpStatus.OK);

    }
    @PostMapping("/generateOtp")
    public ResponseEntity<?> generateOtp(@RequestParam String email) {
        userService.generateOtp(email);
        return new ResponseEntity<>(new ApiResponse<>(true,"OTP sent successfully",null), HttpStatus.OK);
    }
    @PostMapping("/verifyOtp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        return userService.verifyOtp(email, otp);
    }
}
