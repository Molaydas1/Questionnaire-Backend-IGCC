package com.example.IGCC.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
public class Question {
    private String questionId;
    protected String question;
    protected String score;
    protected String questionType;
    protected Boolean required;
    protected String report;
    protected String ifNo;
    protected String ifYes;
}
