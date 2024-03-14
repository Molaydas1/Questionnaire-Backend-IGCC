package com.example.IGCC.controller;

import com.example.IGCC.model.ApiResponse;
import com.example.IGCC.model.QuestionnaireResponse;
import com.example.IGCC.model.User;
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
    public ResponseEntity<String> allQuestionnaireSaveUser(
            @RequestBody List<QuestionnaireResponse> questionnaires, @RequestParam("email") String email) {
        userService.userAndQuestionnaireSaveAndSubmit(questionnaires,email,false);
        return ResponseEntity.ok("save the all Questionnaire for user");
    }
    @PostMapping("/submit")
    public ResponseEntity<String> allQuestionnaireSubmitUser(
            @RequestBody List<QuestionnaireResponse> questionnaires, @RequestParam("email") String email) {
        userService.userAndQuestionnaireSaveAndSubmit(questionnaires,email,true);
        return ResponseEntity.ok("save the all Questionnaire for user");
    }
    @PostMapping("/generateOtp")
    public ResponseEntity<String> generateOtp(@RequestParam String email) {
            userService.generateOtp(email);
            return ResponseEntity.ok("OTP has been sent to " + email);
    }
    @PostMapping("/verifyOtp")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        if (userService.verifyOtp(email, otp)) {
            return ResponseEntity.ok("OTP is valid");
        }
        return ResponseEntity.ok("Invalid OTP");
    }
}
