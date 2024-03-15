package com.example.IGCC.controller;

import com.example.IGCC.entity.Admin;
import com.example.IGCC.model.*;
import com.example.IGCC.service.AdminService;
import com.example.IGCC.service.JwtService;
import com.example.IGCC.service.QuestionnaireService;
import com.example.IGCC.service.UserService;
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
    public ResponseEntity<?> createAdmin() {
        return adminService.createAdmin();
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginAdmin(@RequestBody Admin admin) {
        return adminService.adminLoginValidation(admin);
    }
//    @PostMapping("/getAllUser")
//    @PreAuthorize("hasAuthority('Admin')")
//    public ResponseEntity<?> getAllUser() {
//        return userService.showAllUser();
//    }
//    @PostMapping("/viewUser")
//    @PreAuthorize("hasAuthority('Admin')")
//    public ResponseEntity<?> viewByUser(@RequestParam("email")String email) {
//        return questionnaireService.getQuestionnaire(email);
//    }
    @PostMapping("/generateToken")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.
                authenticate(new UsernamePasswordAuthenticationToken
                        (authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()){
            return new ResponseEntity<>(new ApiResponse<>(true,"Login Successful",jwtService.generateToken(authRequest.getUsername())), HttpStatus.OK);
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }
}
