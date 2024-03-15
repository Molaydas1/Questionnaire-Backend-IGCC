package com.example.IGCC.entity;
;
import lombok.*;
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
    private Date submitDate;
    private Boolean flag;
    private String otp;
    private Date otpSandTime;
    private List<UserAnswer> userAnswer;
}
