package com.ohpenl.midoffice.configurationtracker.problem.exception;

import org.springframework.http.HttpStatus;

public class AlreadyExistsException extends ServiceException {
    public AlreadyExistsException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
