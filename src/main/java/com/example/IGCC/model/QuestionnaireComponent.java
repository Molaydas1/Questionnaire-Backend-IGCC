package com.example.IGCC.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QuestionnaireComponent {
    private String questionId;
    private String question;
    private String report;
    private String score;
    private String questionType;
    private Boolean required;
    private Map<String, String> parameters;
    @Field("email")
    private String email;
    @Field("response")
    private Boolean response;
}
