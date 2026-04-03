package com.ohpenl.midoffice.configurationtracker.validator;

import com.ohpenl.midoffice.configurationtracker.enums.ConfigurationDataType;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
public class NumberDataValidator extends DataValidator {
    @Override
    public ConfigurationDataType dataType() {
        return ConfigurationDataType.NUMBER;
    }

    @Override
    public boolean isValid(String data) {
        if (data.matches("-?\\d+(\\.\\d+)?")) {
            try {
                Double.parseDouble(data);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }
}
