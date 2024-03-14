package com.example.IGCC.model;

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
@ToString
@Document("User")
public class User{

    @Id
    private String id;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String country;
    @NotBlank
    private String sector;
    private Boolean status;
    private String otp;
    private Date timestamp;
    private List<UserAnswer> userAnswerResponse;
}
