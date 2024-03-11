package com.example.IGCC.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document("User")
public class User {
    @Id
    private String email;
    private String country;
    private String sector;
    private List<UserAnswerResponse> userAnswerResponse;
}
