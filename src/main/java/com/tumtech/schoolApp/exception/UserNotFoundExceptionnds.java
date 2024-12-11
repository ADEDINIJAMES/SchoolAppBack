package com.tumtech.schoolApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundExceptionnds extends RuntimeException {
    public UserNotFoundExceptionnds(String message) {
        super(message);
    }
}