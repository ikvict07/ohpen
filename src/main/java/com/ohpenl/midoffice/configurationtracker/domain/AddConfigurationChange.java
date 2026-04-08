package com.ohpenl.midoffice.configurationtracker.domain;

import org.jspecify.annotations.Nullable;

public final class AddConfigurationChange extends ConfigurationChange {

    public AddConfigurationChange(@Nullable String newValue, ConfigurationType configType) {
        super(null, newValue, configType);
    }

    public AddConfigurationChange(Long id, @Nullable String newValue, ConfigurationType configType) {
        super(id, null, newValue, configType);
    }

    public UpdateConfigurationChange update(String newValue) {
        return new UpdateConfigurationChange(
                this.getNewValue(),
                newValue,
                this.getConfigType()
        );
    }

    public RemoveConfigurationChange remove() {
        return new RemoveConfigurationChange(
                this.getNewValue(),
                this.getConfigType()
        );
    }
}
