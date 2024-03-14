package com.example.IGCC.service;

import com.example.IGCC.config.SecurityConfig;
import com.example.IGCC.model.*;
import com.example.IGCC.repository.AdminRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    public void createAdmin(){
        Admin admin;
//        if(admin!=null){
//            log.info("already Admin exists{}", admin.getEmail());
//        }else{
            admin=new Admin();
            admin.setEmail(email);
            admin.setPassword( securityConfig.passwordEncoder().encode(password));
            admin.setRoles("Admin");
            log.info("create user {}", admin.getEmail());
//        }
        adminRepository.save(admin);
    }
//    public Boolean adminLoginValidation(Admin admin){
//        Admin newAdmin=adminRepository.findByEmail(admin.getEmail());
//        if(newAdmin != null && newAdmin.getPassword().equals(admin.getPassword())){
//            log.info("admin Login successfully");
//            return true;
//        }
//        log.info("invalid credentials");
//        return false;
//    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {

        Optional<Admin> userDetail = adminRepository.findByEmail(username);

        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }
}
