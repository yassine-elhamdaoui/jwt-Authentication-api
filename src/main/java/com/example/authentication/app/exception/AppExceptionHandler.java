package com.example.authentication.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppExceptionHandler {
    
    public ResponseEntity<String> handleForbiddenException(){
        return new ResponseEntity<String>("jwt masal7ach", HttpStatus.FORBIDDEN);
    }
}
