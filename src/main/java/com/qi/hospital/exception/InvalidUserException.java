package com.qi.hospital.exception;

import org.springframework.http.HttpStatus;

public class InvalidUserException extends GlobalException {
    public InvalidUserException(final String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
