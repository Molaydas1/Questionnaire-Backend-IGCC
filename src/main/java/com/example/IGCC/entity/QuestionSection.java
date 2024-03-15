package com.example.IGCC.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class QuestionSection {
    private String sectionId;
    private String section;
    private List<Question> questions;
}
