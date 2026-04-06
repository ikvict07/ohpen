package com.ohpenl.midoffice.configurationtracker.domain;

import org.jspecify.annotations.Nullable;

public final class RemoveConfigurationChange extends ConfigurationChange {
    public RemoveConfigurationChange(Long id, @Nullable String previousValue, ConfigurationType configType) {
        super(id, previousValue, null, configType);
    }

    public RemoveConfigurationChange(@Nullable String previousValue, ConfigurationType configType) {
        super(previousValue, null, configType);
    }

    public AddConfigurationChange recreate(String newValue, ConfigurationType configType) {
        return new AddConfigurationChange(
                newValue,
                configType
        );
    }
}
