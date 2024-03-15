package com.example.IGCC.exception;

import com.example.IGCC.model.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalExceptions(Exception ex) {
        return new ResponseEntity<>(new ApiResponse<>(false,"An unexpected error occurred",null), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(MyFileNotFoundException.class)
    public ResponseEntity<?> handleFileNotFoundException(MyFileNotFoundException ex) {
        return new ResponseEntity<>(new ApiResponse<>(false,"File Not found Exception",null), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(NoRecordsFoundExcption.class)
    public ResponseEntity<?> handleNoRecordsFoundExcption(NoRecordsFoundExcption ex) {
        return new ResponseEntity<>(new ApiResponse<>(false,"Not found Exception",null), HttpStatus.NOT_FOUND);
    }

}
