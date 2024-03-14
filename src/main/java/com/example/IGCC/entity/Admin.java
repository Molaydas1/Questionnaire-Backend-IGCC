package com.example.IGCC.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("Admin")
public class Admin {
    @Id
    private String id;
    private String email;
    private String password;
    private String roles;

}
