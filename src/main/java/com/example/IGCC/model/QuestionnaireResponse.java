package com.example.IGCC.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionnaireResponse {
    private String sectionId;
    private String section;
    private List<QuestionnaireComponentResponse> components;
}
