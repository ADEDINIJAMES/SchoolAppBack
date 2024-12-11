package com.tumtech.schoolApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserNotPermitted  extends  RuntimeException{
    public UserNotPermitted(String message) {
        super(message);
    }
}
