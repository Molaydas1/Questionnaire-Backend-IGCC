package com.example.IGCC.service;

import com.example.IGCC.model.OtpModel;
import com.example.IGCC.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    public String generateOtp(String email) {
        // Generate a random 6-digit OTP
        int otpValue = (int) ((Math.random() * (999999 - 100000)) + 100000);
        String otp = String.valueOf(otpValue);

        // Save OTP to the database
        OtpModel otpModel = new OtpModel();
        otpModel.setEmail(email);
        otpModel.setOtp(otp);
        otpModel.setTimestamp(new Date());
        otpRepository.save(otpModel);

        return otp;
    }

    public boolean verifyOtp(String email, String otp) {
        // Find the OTP record in the database
        OtpModel otpModel = otpRepository.findById(email).orElse(null);

        // Check if OTP exists and is valid
        if (otpModel != null && otpModel.getOtp().equals(otp)) {
            // Check if OTP is within 2 minutes
            Date currentTime = new Date();
            Date otpTime = otpModel.getTimestamp();
            long diffInMillis = currentTime.getTime() - otpTime.getTime();
            long diffInSeconds = diffInMillis / 1000;

            if (diffInSeconds <= 120) {
                // OTP is valid and within 2 minutes
                return true;
            }
        }
        return false;
    }
}

