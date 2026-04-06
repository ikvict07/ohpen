package com.ohpenl.midoffice.configurationtracker.validator;

import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationDataType;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class BooleanDataValidator extends DataValidator {
    @Override
    public ConfigurationDataType dataType() {
        return ConfigurationDataType.BOOLEAN;
    }

    @Override
    public boolean isValid(String data) {
        return data.equalsIgnoreCase("true") || data.equalsIgnoreCase("false");
    }
}
