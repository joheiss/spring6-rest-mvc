package com.jovisco.spring6restmvc.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "data not found")
public class NotFoundException extends RuntimeException {
    
    public NotFoundException() {}

    public NotFoundException(String message) {
        super(message);
    }
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public NotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writeableStacktrace) {
        super(message, cause, enableSuppression, writeableStacktrace);
    }

}
