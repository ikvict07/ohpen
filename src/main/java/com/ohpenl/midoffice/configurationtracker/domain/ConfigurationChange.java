package com.ohpenl.midoffice.configurationtracker.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;

@Data
public abstract sealed class ConfigurationChange permits AddConfigurationChange, RemoveConfigurationChange, UpdateConfigurationChange {

    @NotNull
    private Long id = 0L;

    @Nullable
    private final String previousValue;

    @Nullable
    private final String newValue;

    @NotNull
    private final ConfigurationType configType;

    @NotNull
    private LocalDateTime timestamp = LocalDateTime.now();

    protected ConfigurationChange(Long id, @Nullable String previousValue, @Nullable String newValue, ConfigurationType configType) {
        if (id != null) {
            this.id = id;
        }
        this.previousValue = previousValue;
        this.newValue = newValue;
        this.configType = configType;
    }

    protected ConfigurationChange(@Nullable String previousValue, @Nullable String newValue, ConfigurationType configType) {
        this.previousValue = previousValue;
        this.newValue = newValue;
        this.configType = configType;
    }
}
