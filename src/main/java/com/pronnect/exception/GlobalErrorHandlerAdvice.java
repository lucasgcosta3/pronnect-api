package com.pronnect.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalErrorHandlerAdvice {

    private ResponseEntity<DefaultErrorMessage> buildError(
            HttpStatus status,
            String message,
            HttpServletRequest request
    ) {

        DefaultErrorMessage error = new DefaultErrorMessage(
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<DefaultErrorMessage> handleNotFoundException(
            NotFoundException e,
            HttpServletRequest request
    ) {
        return buildError(HttpStatus.NOT_FOUND, e.getReason(), request);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<DefaultErrorMessage> handleEmailAlreadyExistsException(
            EmailAlreadyExistsException e,
            HttpServletRequest request
    ) {
        return buildError(HttpStatus.BAD_REQUEST, e.getReason(), request);
    }

    @ExceptionHandler(ProfileAlreadyExistsException.class)
    public ResponseEntity<DefaultErrorMessage> handleProfileAlreadyExistsException(
            ProfileAlreadyExistsException e,
            HttpServletRequest request
    ) {
        return buildError(HttpStatus.BAD_REQUEST, e.getReason(), request);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<DefaultErrorMessage> handleInvalidCredentialsException(
            InvalidCredentialsException e,
            HttpServletRequest request
    ) {
        return buildError(HttpStatus.UNAUTHORIZED, e.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DefaultErrorMessage> handleGenericException(
            Exception ex,
            HttpServletRequest request
    ) {
        return buildError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unexpected internal error",
                request
        );
    }
}

