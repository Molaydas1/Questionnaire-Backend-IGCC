package com.example.IGCC.service;

import com.example.IGCC.config.SecurityConfig;
import com.example.IGCC.entity.Admin;
import com.example.IGCC.model.*;
import com.example.IGCC.repository.AdminRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Slf4j
public class AdminService implements UserDetailsService {
    @Autowired
    private AdminRepository adminRepository;
    @Value("${admin.email}")
    private String email;
    @Value("${admin.password}")
    private String password;
    @Autowired
    private SecurityConfig securityConfig;
    public ResponseEntity<?> createAdmin(){
        Admin admin;
//        if(admin!=null){
//            log.info("already Admin exists{}", admin.getEmail());
//        }else{
            admin=new Admin();
            admin.setEmail(email);
            admin.setPassword( securityConfig.passwordEncoder().encode(password));
            admin.setRoles("Admin");
            log.info("create Admin Successfully {}", admin.getEmail());
//        }
        adminRepository.save(admin);
        return new ResponseEntity<>(new ApiResponse<>(true,"Create Admin ",null), HttpStatus.OK);
    }
    public ResponseEntity<?> adminLoginValidation(Admin admin){
        Admin newAdmin=adminRepository.findByEmail(admin.getEmail()).get();
        if(newAdmin != null && newAdmin.getPassword().equals(admin.getPassword())){
            log.info("Admin Login Successfully");
            return new ResponseEntity<>(new ApiResponse<>(true,"Admin Login Successfully",null), HttpStatus.OK);
        }
        log.info("Invalid Credentials");
        return new ResponseEntity<>(new ApiResponse<>(false,"Invalid Credentials",null), HttpStatus.INTERNAL_SERVER_ERROR);

    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Admin> userDetail = adminRepository.findByEmail(username);
        return userDetail.map(AdminInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }
}
