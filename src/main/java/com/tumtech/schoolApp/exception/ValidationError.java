package com.tumtech.schoolApp.exception;

public record ValidationError(
        String field,
        String message
) {
}