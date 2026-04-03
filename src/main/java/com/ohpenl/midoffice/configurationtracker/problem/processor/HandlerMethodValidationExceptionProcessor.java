package com.ohpenl.midoffice.configurationtracker.problem.processor;

import com.ohpenl.midoffice.configurationtracker.problem.util.NameFormatter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HandlerMethodValidationExceptionProcessor implements ExceptionProcessor<HandlerMethodValidationException> {
    @Override
    public ProblemDetail convertToDetail(HandlerMethodValidationException exception) {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        detail.setTitle("Validation Error");
        detail.setDetail(buildDetailMessage(exception));
        detail.setType(URI.create("application:error" + NameFormatter.format(exception.getClass().getName())));
        detail.setProperty("errors", extractValidationErrors(exception));
        return detail;
    }

    private String buildDetailMessage(HandlerMethodValidationException exception) {
        List<String> errors = exception.getParameterValidationResults().stream()
                .flatMap(result -> result.getResolvableErrors().stream()
                        .map(error -> {
                            String location = result.getMethodParameter().getParameterName() != null
                                    ? result.getMethodParameter().getParameterName()
                                    : "response";
                            String message = error.getDefaultMessage() != null
                                    ? error.getDefaultMessage()
                                    : Arrays.toString(error.getCodes());
                            return location + ": " + message;
                        }))
                .collect(Collectors.toList());
        return errors.isEmpty()
                ? exception.getLocalizedMessage()
                : "Validation failed for: " + String.join(", ", errors);
    }

    private List<Map<String, Object>> extractValidationErrors(HandlerMethodValidationException exception) {
        boolean isReturnValue = exception.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR;
        return exception.getParameterValidationResults().stream()
                .flatMap(result -> result.getResolvableErrors().stream()
                        .map(error -> {
                            String message = error.getDefaultMessage() != null
                                    ? error.getDefaultMessage()
                                    : Arrays.toString(error.getCodes());
                            if (isReturnValue) {
                                return Map.of(
                                        "type", "returnValue",
                                        "message", message,
                                        "rejectedValue", result.getArgument() != null ? result.getArgument() : "null",
                                        "code", Arrays.toString(error.getCodes())
                                );
                            } else {
                                String paramName = result.getMethodParameter().getParameterName() != null
                                        ? result.getMethodParameter().getParameterName()
                                        : "unknown";
                                return Map.of(
                                        "type", "parameter",
                                        "field", paramName,
                                        "message", message,
                                        "rejectedValue", result.getArgument() != null ? result.getArgument() : "null",
                                        "code", Arrays.toString(error.getCodes())
                                );
                            }
                        }))
                .collect(Collectors.toList());
    }
}
