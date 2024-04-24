package com.jovisco.spring6restmvc.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class CustomErrorController {

    @ExceptionHandler
    ResponseEntity<Object> handleJpaViolations(TransactionSystemException exception) {
        var responseEntity = ResponseEntity.badRequest();
        if (exception.getCause().getCause() instanceof ConstraintViolationException) {
            ConstraintViolationException cve = (ConstraintViolationException) exception.getCause().getCause();
            var errors = cve.getConstraintViolations()
                .stream()
                .map(violation -> {
                    Map<String, String> errorMap = new HashMap<>();
                    errorMap.put(violation.getPropertyPath().toString(), violation.getMessage());
                    return errorMap;
                })
                .collect(Collectors.toList());
            return responseEntity.body(errors);
        }
        return responseEntity.build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Object> handleBindError(MethodArgumentNotValidException exception) {
        var errors = exception.getBindingResult().getFieldErrors()
            .stream()
            .map(error -> {
                Map<String, String> errorMap = new HashMap<>();
                errorMap.put(error.getField(), error.getDefaultMessage());
                return errorMap;
            })
            .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errors);
    }
}
