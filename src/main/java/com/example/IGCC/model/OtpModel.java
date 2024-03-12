package com.example.IGCC.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "otp")
public class OtpModel {
    @Id
    private String email;
    private String otp;
    private Date timestamp;
}
