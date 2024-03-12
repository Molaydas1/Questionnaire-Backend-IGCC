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
        userService.createUserService(user);
        return ResponseEntity.ok().body("create user");
    }

//    @PostMapping("/save")
//    public ResponseEntity<String> saveAllQuestionnaire(@RequestBody List<Questionnaire> questionnaires,@RequestParam("email") String email) {
//        User user=userService.userAndQuestionnaireSave(questionnaires,email);
//
//        userRepository.save(user);
//        log.info("save the all Questionnaire for user");
//        return ResponseEntity.ok("save the all Questionnaire for user");
//    }
    @PostMapping("/generateOtp")
    public String generateOtp(@RequestParam String email) {
        String otp = otpService.generateOtp(email);
        return "OTP has been sent to " + email;
    }
    @PostMapping("/verifyOtp")
    public String verifyOtp(@RequestParam String email, @RequestParam String otp) {
        boolean isValid = otpService.verifyOtp(email, otp);
        if (isValid) {
            return "OTP is valid";
        } else {
            return "Invalid OTP";
        }
    }
}
