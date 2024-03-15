package com.example.IGCC.service;

import com.example.IGCC.entity.Answer;
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
import java.util.UUID;

@Service
@Slf4j
public class UserService {
    @Autowired
    private QuestionnaireRepository questionnaireRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JavaMailSender emailSender;
    public void createUser(UserRequest userRequest){
        User newUser=userRepository.findByEmail(userRequest.getEmail());
        if(newUser!=null){
            log.info("already user exists{}", newUser.getEmail());
        }else{
            User user=new User();
            user.setId(UUID.randomUUID().toString());
            user.setEmail(userRequest.getEmail());
            user.setCountry(userRequest.getCountry());
            user.setSector(userRequest.getSector());
            user.setUserAnswer(new ArrayList<>());
            log.info("create user {}", user.getEmail());
            userRepository.save(user);
        }
    }
//    public void userAndQuestionnaireSaveAndSubmit(List<QuestionnaireResponse> questionnaires, String email,Boolean status){
//        User user=userRepository.findByEmail(email);
//        if(user.getUserAnswer().isEmpty()){
//            List<UserAnswer> userAnswerResponses=new ArrayList<>();
//            for(QuestionnaireResponse questionnaire:questionnaires){
//                for(QuestionnaireComponentResponse component:questionnaire.getComponents()){
//                    userAnswerResponses.add(new UserAnswer(component.getQuestionId(),new Date(),
//                            true,component.getResponse()));
//                }
//            }
//            user.setUserAnswerResponse(userAnswerResponses);
//            user.setStatus(status);
//            userRepository.save(user);
//        }else{
//            List<UserAnswer> userAnswerResponses=user.getUserAnswerResponse();
//            Boolean count;
//            for(QuestionnaireResponse questionnaire:questionnaires){
//                for(QuestionnaireComponentResponse component:questionnaire.getComponents()){
//                    count=true;
//                    for (UserAnswer userAnswer:userAnswerResponses){
//                        if(userAnswer.getQuestionId().equals(component.getQuestionId())){
//                                userAnswer.setResponse(component.getResponse());
//                                userAnswer.setSubmitDate(new Date());
//                                count=false;
//                                break;
//                        }
//                    }if(count){
//                        userAnswerResponses.add(new UserAnswer(component.getQuestionId(),new Date(),
//                                true,component.getResponse()));
//                    }
//                }
//            }
//            user.setUserAnswerResponse(userAnswerResponses);
//            user.setStatus(status);
//            userRepository.save(user);
//        }
//
//        log.info("save the all Questionnaire for user");
//    }
//
//
    public void generateOtp(String email) {
        int otpValue = (int) ((Math.random() * (999999 - 100000)) + 100000);
        String otp = String.valueOf(otpValue);

        User user=userRepository.findByEmail(email);
        user.setOtp(otp);
        user.setOtpSandTime(new Date());
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
            Date otpTime = user.getOtpSandTime();
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
//    public ResponseEntity<?> showAllUser() {
//        return new ResponseEntity<>(new ApiResponse<>(true,"List of User found ",userRepository.findAllUser()), HttpStatus.OK);
//    }


}
