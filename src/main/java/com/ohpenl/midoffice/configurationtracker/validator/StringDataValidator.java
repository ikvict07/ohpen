package com.ohpenl.midoffice.configurationtracker.validator;

import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationDataType;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
public class StringDataValidator extends DataValidator {
    @Override
    public ConfigurationDataType dataType() {
        return ConfigurationDataType.STRING;
    }

    @Override
    public boolean isValid(String data) {
        return data != null && !data.trim().isEmpty();
    }
}
