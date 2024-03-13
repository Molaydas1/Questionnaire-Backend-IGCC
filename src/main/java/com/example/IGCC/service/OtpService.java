package com.example.IGCC.service;

import com.example.IGCC.model.OtpModel;
import com.example.IGCC.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;
    @Autowired
    private JavaMailSender emailSender;

    public void generateOtp(String email) {
        int otpValue = (int) ((Math.random() * (999999 - 100000)) + 100000);
        String otp = String.valueOf(otpValue);

        OtpModel otpModel = new OtpModel();
        otpModel.setEmail(email);
        otpModel.setOtp(otp);
        otpModel.setTimestamp(new Date());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP for verification");
        message.setText("Your OTP is: " + otp);
        emailSender.send(message);
        otpRepository.save(otpModel);
    }
    public boolean verifyOtp(String email, String otp) {
        OtpModel otpModel = otpRepository.findById(email).orElse(null);

        if (otpModel != null && otpModel.getOtp().equals(otp)) {
            Date currentTime = new Date();
            Date otpTime = otpModel.getTimestamp();
            long diffInMillis = currentTime.getTime() - otpTime.getTime();
            long diffInSeconds = diffInMillis / 1000;

            if (diffInSeconds <= 120) {
                return true;
            }
        }
        return false;
    }
}

