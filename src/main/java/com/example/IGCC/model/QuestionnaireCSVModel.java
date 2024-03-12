package com.example.IGCC.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionnaireCSVModel extends QuestionnaireComponent{
    @Id
    private String id;
    @JsonProperty("QuesctionSection")
    private String quesctionSection;
}
