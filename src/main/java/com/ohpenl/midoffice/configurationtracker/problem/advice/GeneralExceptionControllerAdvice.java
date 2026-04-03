package com.ohpenl.midoffice.configurationtracker.problem.advice;

import com.ohpenl.midoffice.configurationtracker.problem.registry.ProcessorsRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GeneralExceptionControllerAdvice {
    private final ProcessorsRegistry registry;

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception exception) {
        log.error("An exception occurred", exception);
        log.error("Caused by", exception.getCause());
        return registry.getProcessor(exception.getClass()).convertToDetail(exception);
    }
}
