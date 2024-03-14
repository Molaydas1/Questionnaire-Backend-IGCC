package com.example.IGCC.exception;

import org.springframework.http.HttpStatus;
public class ErrorResponse {
    private String message;
    private HttpStatus status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }


    public ErrorResponse(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

}
