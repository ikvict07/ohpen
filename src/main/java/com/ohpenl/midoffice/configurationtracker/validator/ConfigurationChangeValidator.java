package com.ohpenl.midoffice.configurationtracker.validator;

import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationType;
import com.ohpenl.midoffice.configurationtracker.problem.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class ConfigurationChangeValidator {

    private final List<DataValidator> validators;

    public void validateDataTypes(String newValue, ConfigurationType configurationType) {
        if (newValue == null) return;
        var validator = validators
                .stream()
                .filter(e -> e.dataType().name().equals(configurationType.dataType().name()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No validator found for configuration type: " + configurationType.name()));

        if (!validator.isValid(newValue)) {
            throw new BadRequestException("Invalid value " + newValue + " for configuration type: " + configurationType.name() + " with data type: " + configurationType.dataType().name());
        }
    }
}
