package com.ohpenl.midoffice.configurationtracker.problem.exception;

import org.springframework.http.HttpStatus;

public class IllegalTransitionException extends ServiceException {
    public IllegalTransitionException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
