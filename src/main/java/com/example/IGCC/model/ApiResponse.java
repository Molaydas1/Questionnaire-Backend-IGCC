package com.example.IGCC.model;

import lombok.*;

@Data
@AllArgsConstructor
public class ApiResponse<T>
{
    private boolean success;
    private String message;
    private T payload;
}