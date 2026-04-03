package com.ohpenl.midoffice.configurationtracker.problem.processor;

import com.ohpenl.midoffice.configurationtracker.problem.util.NameFormatter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;

public class IllegalArgumentExceptionProcessor implements ExceptionProcessor<IllegalArgumentException> {
    @Override
    public ProblemDetail convertToDetail(IllegalArgumentException exception) {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        detail.setTitle("Invalid Argument");
        detail.setDetail(exception.getLocalizedMessage());
        detail.setType(URI.create("application:error" + NameFormatter.format(exception.getClass().getName())));
        return detail;
    }
}
