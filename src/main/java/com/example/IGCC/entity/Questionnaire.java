package com.example.IGCC.entity;

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
@Document("Questionnaire")
public class Questionnaire {
    @Id
    private String id;
    private Date uplodeDate;
    private Boolean flag;
    private List<QuestionSection> questionSections;
}

