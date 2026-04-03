package com.ohpenl.midoffice.configurationtracker.validator;

import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.problem.exception.BadRequestException;
import com.ohpenl.midoffice.configurationtracker.problem.exception.ConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public final class ConfigurationChangeValidator {

    private final List<DataValidator> validators;

    public void validateDataTypes(ConfigurationChange configurationChange) {
        var validator = validators
                .stream()
                .filter((e) -> e.dataType() == configurationChange.getConfigType().getDataType())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No validator found for configuration type: " + configurationChange.getConfigType().getName()));

        if (!validator.isValid(configurationChange.getNewValue())) {
            throw new BadRequestException("Invalid value " + configurationChange.getNewValue() + " for configuration type: " + configurationChange.getConfigType().getName());
        }
    }

    public void validatePreviousValue(ConfigurationChange oldChange, ConfigurationChange newChange) {
        if (oldChange == null && newChange.getPreviousValue() == null) {
            return;
        }

        String actualPrevious = oldChange == null ? null : oldChange.getNewValue();
        String claimedPrevious = newChange.getPreviousValue();

        if (!Objects.equals(actualPrevious, claimedPrevious)) {
            throw new ConflictException(
                    "Previous value mismatch: expected '%s' but current is '%s'"
                            .formatted(claimedPrevious, actualPrevious)
            );
        }
    }
}
