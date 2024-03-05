package com.example.IGCC.controller;

import com.example.IGCC.model.User;
import com.example.IGCC.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/User")
@Slf4j
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        try {
            user.setResponseKey(false);
            user.setUserAnswerResponse(new ArrayList<>());
            userRepository.save(user);
            log.info("create user {}", user);
            return ResponseEntity.ok("create user successfully.");
        } catch (Exception e) {
            log.info("Execption occurs {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to user create.");
        }
    }
}
