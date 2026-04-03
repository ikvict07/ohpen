package com.ohpenl.midoffice.configurationtracker.problem.processor;

import org.springframework.http.ProblemDetail;

public interface ExceptionProcessor<T extends Throwable> {
    ProblemDetail convertToDetail(T exception);
}
