package com.jovisco.spring6restmvc.controllers;

/*
 * The exception controller with @ControllerAdvice is not needed if you use @ResponseStatus in the exception class

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<HttpStatus> handleNotFoundException() {
        return ResponseEntity.notFound().build();
    }

}
 */
