package com.ohpenl.midoffice.configurationtracker.problem.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends ServiceException{
    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
