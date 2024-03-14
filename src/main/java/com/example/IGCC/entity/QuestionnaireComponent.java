package com.example.IGCC.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionnaireComponent {
    @JsonProperty("No")
    private String questionId;
    @JsonProperty("QuesctionSection")
    @Field("QuesctionSection")
    private String quesctionSection;
    @JsonProperty("Question")
    protected String question;
    @JsonProperty("Score")
    protected String score;
    @JsonProperty("QuestionType")
    protected String questionType;
    @JsonProperty("Required")
    protected Boolean required;
    @JsonProperty("Report")
    protected String report;
    @JsonProperty("IfNo")
    protected String ifNo;
    @JsonProperty("IfYes")
    protected String ifYes;
}
