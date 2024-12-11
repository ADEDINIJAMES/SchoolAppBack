package com.tumtech.schoolApp.exception;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class ExceptionHandlers {
    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleExpiredJwtException(ExpiredJwtException e, HttpServletRequest request) {
        return buildErrorResponse(request, e.getMessage());
    }

    @ExceptionHandler(UserNotPermitted.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleUserNotPermitted(ExpiredJwtException e, HttpServletRequest request) {
        return buildErrorResponse(request, e.getMessage());
    }

    private ResponseEntity<ApiError> buildErrorResponse(
            HttpServletRequest request, String message) {
        return buildErrorResponse(request, HttpStatus.BAD_REQUEST, message, null);
    }

    private ResponseEntity<ApiError> buildErrorResponse(
            HttpServletRequest request, HttpStatus status, String message, List<ValidationError> errors) {
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                message,
                status.value(),
                LocalDateTime.now(),
                errors
        );
        return new ResponseEntity<>(apiError, status);
    }

}




