package com.qi.hospital.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class GlobalException extends ResponseStatusException {

    public GlobalException(final HttpStatus status, final String message) {
        super(status, message);
    }
}
