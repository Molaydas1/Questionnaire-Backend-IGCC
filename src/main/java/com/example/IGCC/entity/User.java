package com.example.IGCC.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("User")
public class User{

    @Id
    private String id;
    private String email;
    private String country;
    private String sector;
    private Boolean status;
    private String otp;
    private Date timestamp;
    private List<UserAnswer> userAnswerResponse;
}
