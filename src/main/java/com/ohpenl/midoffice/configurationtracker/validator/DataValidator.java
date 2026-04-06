package com.ohpenl.midoffice.configurationtracker.validator;

import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationDataType;
import jakarta.validation.constraints.NotNull;

public abstract class DataValidator {

    public abstract ConfigurationDataType dataType();

    public abstract boolean isValid(@NotNull String data);
}
