package com.example.IGCC.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("No")
    private String questionId;
    @JsonProperty("Question")
    protected String question;
    @JsonProperty("Score")
    protected String score;
    @JsonProperty("QuestionType")
    protected String questionType;
    @JsonProperty("Required")
    protected boolean required;
    @JsonProperty("Report")
    protected String report;
    @JsonProperty("IfNo")
    protected String ifNo;
    @JsonProperty("IfYes")
    protected String ifYes;
    @Field("response")
    private Boolean response;

}
