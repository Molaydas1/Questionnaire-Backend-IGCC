package com.example.IGCC.controller;

import com.example.IGCC.model.*;
import com.example.IGCC.service.AdminService;
import com.example.IGCC.service.JwtService;
import com.example.IGCC.service.QuestionnaireService;
import com.example.IGCC.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Admin")
@Slf4j
@CrossOrigin(origins = "*")
public class AdminController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private QuestionnaireService questionnaireService;
    @PostMapping("/create")
    public ResponseEntity<String> createAdmin() {
        adminService.createAdmin();
        return ResponseEntity.ok().body("create Admin");
    }
//    @PostMapping("/login")
//    public ResponseEntity<String> createUser(@RequestBody Admin admin) {
//        if (adminService.adminLoginValidation(admin)){
//            return ResponseEntity.ok("login successfully");
//        }
//        return ResponseEntity.ok().body("invalid credentials");
//    }
    @PostMapping("/getAllUser")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<List<UserResponse>> getAllUser() {
        return ResponseEntity.ok().body(userService.showAllUser());
    }
    @PostMapping("/viewUser")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<?> viewByUser(@RequestParam("email")String email) {
        return questionnaireService.getQuestionnaire(email);
    }


    @PostMapping("/generateToken")
//    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.
                authenticate(new UsernamePasswordAuthenticationToken
                        (authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated())
        {
            // return jwtService.generateToken(authRequest.getUsername());
            return new ResponseEntity<>(new ApiResponse<>(true,"Login Successful",jwtService.generateToken(authRequest.getUsername())), HttpStatus.OK);
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }
}
