package com.example.IGCC.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Answer {
    private String questionId;
    private Boolean response;
}
