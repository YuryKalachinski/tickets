package com.kalachinski.tickets.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException{
    Logger log = LoggerFactory.getLogger(BadRequestException.class);

    public BadRequestException() {
        log.warn("Bad request exception.");
    }
}
