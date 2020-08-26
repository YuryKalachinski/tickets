package com.kalachinski.tickets.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<Object> handlerNotFoundException (NotFoundException ex, WebRequest request) {
        log.warn("Not found exception:");
        log.warn("WebRequest: " + request);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<Object> handlerBadRequestException(BadRequestException ex, WebRequest request) {
        log.warn("Bad request exception:");
        log.warn("WebRequest: " + request);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<Object> handlerConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        log.warn("Constraint violation exception:" + ex);
        log.warn("WebRequest: " + request);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid (MethodArgumentNotValidException ex,
                                                                   HttpHeaders headers,
                                                                   HttpStatus status,
                                                                   WebRequest request) {
        log.warn("Bad request exception:");
        log.warn("HttpHeaders: " + headers);
        log.warn("WebRequest: " + request);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
