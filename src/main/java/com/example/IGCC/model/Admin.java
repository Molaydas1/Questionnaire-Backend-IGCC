package com.example.IGCC.model;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("Admin")
public class Admin {
    @Id
    private String id;
    private String email;
    private String password;
    private String roles;

}
