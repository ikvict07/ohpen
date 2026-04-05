package com.ohpenl.midoffice.configurationtracker.domain;

import org.jspecify.annotations.Nullable;

public final class RemoveConfigurationChange <T> extends ConfigurationChange<T> {
    public RemoveConfigurationChange(@Nullable T previousValue, ConfigurationType configType) {
        super(previousValue, null, configType);
    }

    public <NEW_T> AddConfigurationChange<NEW_T> recreate(NEW_T newValue, ConfigurationType configType) {
        return new AddConfigurationChange<>(
                newValue,
                configType
        );
    }
}
