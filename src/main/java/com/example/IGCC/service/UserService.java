package com.example.IGCC.service;

import com.example.IGCC.entity.User;
import com.example.IGCC.entity.UserAnswer;
import com.example.IGCC.model.*;
import com.example.IGCC.repository.QuestionnaireRepository;
import com.example.IGCC.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class UserService {
    @Autowired
    private QuestionnaireRepository questionnaireRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JavaMailSender emailSender;
    public void createUser(User user){
        User newUser=userRepository.findByEmail(user.getEmail());
        if(newUser!=null){
            user=newUser;
            log.info("already user exists{}", newUser.getEmail());
        }else{
            log.info("create user {}", user.getEmail());
            user.setUserAnswerResponse(new ArrayList<>());
        }
        userRepository.save(user);
    }
    public void userAndQuestionnaireSaveAndSubmit(List<QuestionnaireResponse> questionnaires, String email,Boolean status){
        User user=userRepository.findByEmail(email);
        List<UserAnswer> userAnswerResponses=new ArrayList<>();
        for(QuestionnaireResponse questionnaire:questionnaires){
            for(QuestionnaireComponentResponse component:questionnaire.getComponents()){
                userAnswerResponses.add(new UserAnswer(component.getQuestionId(),
                        component.getResponse()));
            }
        }
        user.setUserAnswerResponse(userAnswerResponses);
        user.setStatus(status);
        userRepository.save(user);
        log.info("save the all Questionnaire for user");
    }


    public void generateOtp(String email) {
        int otpValue = (int) ((Math.random() * (999999 - 100000)) + 100000);
        String otp = String.valueOf(otpValue);

        User user=userRepository.findByEmail(email);
        user.setOtp(otp);
        user.setTimestamp(new Date());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Your OTP for verification");
        message.setText("Your OTP is: " + otp);
        emailSender.send(message);
        userRepository.save(user);
        log.info("OTP has been sent to {}",email);
    }
    public ResponseEntity<?> verifyOtp(String email, String otp) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getOtp().equals(otp)) {
            Date currentTime = new Date();
            Date otpTime = user.getTimestamp();
            long diffInMillis = currentTime.getTime() - otpTime.getTime();
            long diffInSeconds = diffInMillis / 1000;

            if (diffInSeconds <= 120) {
                log.info("valid otp");
                return new ResponseEntity<>(new ApiResponse<>(true,"OTP is valid",null), HttpStatus.OK);
            }
        }
        log.info("invalid otp");
        return new ResponseEntity<>(new ApiResponse<>(false,"Invalid OTP",null), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    public ResponseEntity<?> showAllUser() {
        return new ResponseEntity<>(new ApiResponse<>(true,"List of User found ",userRepository.findAllUser()), HttpStatus.OK);
    }
}
