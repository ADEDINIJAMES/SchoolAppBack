package com.tumtech.schoolApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class SessionExpiredException extends Exception {
    public SessionExpiredException(  String message) {
        super(message);
    }
}
