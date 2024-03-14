package com.example.IGCC.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionnaireComponentResponse {
    private String questionId;
    private String question;
    private String questionType;
    private Boolean required;
    private Boolean response;
}
