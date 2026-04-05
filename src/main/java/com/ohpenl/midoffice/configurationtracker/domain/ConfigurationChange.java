package com.ohpenl.midoffice.configurationtracker.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.jspecify.annotations.Nullable;

@Data
public abstract sealed class ConfigurationChange<T> permits AddConfigurationChange, RemoveConfigurationChange, UpdateConfigurationChange {

    @NotNull
    private Long id = 0L;

    @Nullable
    private final T previousValue;

    @Nullable
    private final T newValue;

    @NotNull
    private final ConfigurationType configType;
}
