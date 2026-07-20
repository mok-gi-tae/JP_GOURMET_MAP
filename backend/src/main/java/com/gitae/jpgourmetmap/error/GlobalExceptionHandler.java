package com.gitae.jpgourmetmap.error;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException exception,
            HttpServletRequest request
    ) {
        return createResponse(HttpStatus.NOT_FOUND, exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler({
            ConstraintViolationException.class,
            MissingServletRequestParameterException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(
            Exception exception,
            HttpServletRequest request
    ) {
        return createResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), request.getRequestURI());
    }

    private ResponseEntity<ErrorResponse> createResponse(
            HttpStatus status,
            String message,
            String path
    ) {
        ErrorResponse response = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                LocalDateTime.now()
        );
        return ResponseEntity.status(status).body(response);
    }
}
