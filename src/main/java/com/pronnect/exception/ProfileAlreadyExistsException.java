package com.pronnect.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ProfileAlreadyExistsException extends ResponseStatusException {
    public ProfileAlreadyExistsException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
