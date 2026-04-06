package com.ohpenl.midoffice.configurationtracker.problem.processor;

import jakarta.persistence.OptimisticLockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;

public class OptimisticLockExceptionProcessor implements ExceptionProcessor<OptimisticLockException> {
    @Override
    public ProblemDetail convertToDetail(OptimisticLockException exception) {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        detail.setTitle("OptimisticLockException");
        detail.setDetail("Concurrent modification detected - please retry with the latest version");
        detail.setType(URI.create("application:error:optimistic-lock"));
        return detail;
    }
}
