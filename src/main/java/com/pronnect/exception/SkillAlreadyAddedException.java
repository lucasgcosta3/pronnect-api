package com.pronnect.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class SkillAlreadyAddedException extends ResponseStatusException {
    public SkillAlreadyAddedException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
