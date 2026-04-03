package com.ohpenl.midoffice.configurationtracker.problem.processor;

import com.ohpenl.midoffice.configurationtracker.problem.util.NameFormatter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;

public class IllegalStateExceptionProcessor implements ExceptionProcessor<IllegalStateException> {
    @Override
    public ProblemDetail convertToDetail(IllegalStateException exception) {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        detail.setTitle("Application encountered an illegal state");
        detail.setDetail(exception.getLocalizedMessage());
        detail.setType(URI.create("application:error" + NameFormatter.format(exception.getClass().getName())));
        return detail;
    }
}
