package com.aspire.api.exception;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ValidationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.core.JsonParseException;
import com.mongodb.DuplicateKeyException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
        
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<Map<String,String>> handleNumberFormatException(NumberFormatException ex){
        Map<String,String> errors = new HashMap<>();
        errors.put("message" , "Enter a valid number");
        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Map<String,String>> handleDuplicateKeyExeption(DuplicateKeyException ex) {
        Map<String,String> errors = new HashMap<>();
        errors.put("message", "Key Already Exists");
        return new ResponseEntity<>(errors,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<Map<String,String>> validateJsonParseException(JsonParseException ex) {
        Map<String,String> errors = new HashMap<>();
        errors.put("message", "Json Parse Error");
        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String,String>> handleValidationException(ValidationException ex) {
        Map<String,String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,String>> handleExceptions(Exception ex){
        Map<String,String> errors = new HashMap<>();
        errors.put("message", ex.getMessage() );
        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
    }



}


