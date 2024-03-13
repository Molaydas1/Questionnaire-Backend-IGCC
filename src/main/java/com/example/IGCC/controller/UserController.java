package com.example.IGCC.controller;

import com.example.IGCC.exception.NoRecordsFoundExcption;
import com.example.IGCC.model.Questionnaire;
import com.example.IGCC.model.User;
import com.example.IGCC.repository.QuestionnaireRepository;
import com.example.IGCC.repository.UserRepository;
import com.example.IGCC.service.OtpService;
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
    private OtpService otpService;

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        try {
            userService.createUser(user);
            return ResponseEntity.ok().body("create user");
        }catch (NoRecordsFoundExcption e){
            return ResponseEntity.ok().body("already user exists ");
        }
    }
    @PostMapping("/save")
    public ResponseEntity<String> allQuestionnaireSaveUser(
            @RequestBody List<Questionnaire> questionnaires,@RequestParam("email") String email) {
        userService.userAndQuestionnaireSave(questionnaires,email);
        log.info("save the all Questionnaire for user");
        return ResponseEntity.ok("save the all Questionnaire for user");
    }
    @PostMapping("/generateOtp")
    public ResponseEntity<String> generateOtp(@RequestParam String email) {
        try{
            otpService.generateOtp(email);
            log.info("OTP has been sent to {}",email);
            return ResponseEntity.ok("OTP has been sent to " + email);
        }catch (Exception e){
            return ResponseEntity.ok("OTP has been not sent to " + email);
        }
    }
    @PostMapping("/verifyOtp")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        boolean isValid = otpService.verifyOtp(email, otp);
        if (isValid) {
            return ResponseEntity.ok("OTP is valid");
        } else {
            return ResponseEntity.ok("Invalid OTP");
        }
    }
}
