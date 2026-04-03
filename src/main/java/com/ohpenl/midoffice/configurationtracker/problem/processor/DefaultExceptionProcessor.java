package com.ohpenl.midoffice.configurationtracker.problem.processor;

import com.ohpenl.midoffice.configurationtracker.problem.util.NameFormatter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;

public class DefaultExceptionProcessor implements ExceptionProcessor<Throwable> {
    @Override
    public ProblemDetail convertToDetail(Throwable exception) {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        detail.setTitle("Internal Server Error");
        detail.setDetail(exception.getLocalizedMessage());
        detail.setType(URI.create("application:error" + NameFormatter.format(exception.getClass().getName())));
        return detail;
    }
}
