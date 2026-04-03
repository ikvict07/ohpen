package com.ohpenl.midoffice.configurationtracker.problem.processor;

import com.ohpenl.midoffice.configurationtracker.problem.exception.ServiceException;
import com.ohpenl.midoffice.configurationtracker.problem.util.NameFormatter;
import org.springframework.http.ProblemDetail;

import java.net.URI;

public class ServiceExceptionProcessor implements ExceptionProcessor<ServiceException> {
    @Override
    public ProblemDetail convertToDetail(ServiceException exception) {
        ProblemDetail detail = ProblemDetail.forStatus(exception.getStatus());
        detail.setTitle(exception.getClass().getSimpleName());
        detail.setDetail(exception.getMessage());
        detail.setType(URI.create("application:error" + NameFormatter.format(exception.getClass().getName())));
        return detail;
    }
}
