package com.ohpenl.midoffice.configurationtracker.problem.processor;

import com.ohpenl.midoffice.configurationtracker.problem.util.NameFormatter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MethodArgumentNotValidExceptionProcessor implements ExceptionProcessor<MethodArgumentNotValidException> {
    @Override
    public ProblemDetail convertToDetail(MethodArgumentNotValidException exception) {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        detail.setTitle("Invalid Method Argument");
        detail.setDetail(buildDetailMessage(exception));
        detail.setType(URI.create("application:error" + NameFormatter.format(exception.getClass().getName())));
        detail.setProperty("errors", buildErrors(exception));
        return detail;
    }

    private String buildDetailMessage(MethodArgumentNotValidException exception) {
        List<String> errors = exception.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    String field = error instanceof FieldError fe ? fe.getField() : "unknown";
                    return field + ": " + (error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid value");
                })
                .collect(Collectors.toList());
        return errors.isEmpty()
                ? exception.getLocalizedMessage()
                : "Validation failed for: " + String.join(", ", errors);
    }

    private List<Map<String, Object>> buildErrors(MethodArgumentNotValidException exception) {
        return exception.getBindingResult().getFieldErrors().stream()
                .map(error -> Map.of(
                        "field", error.getField(),
                        "message", error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid value",
                        "rejectedValue", error.getRejectedValue() != null ? error.getRejectedValue() : "null",
                        "code", error.getCode() != null ? error.getCode() : ""
                ))
                .collect(Collectors.toList());
    }
}
