package com.ohpenl.midoffice.configurationtracker.domain;

import org.jspecify.annotations.Nullable;

public final class UpdateConfigurationChange extends ConfigurationChange {

    public UpdateConfigurationChange(@Nullable String previousValue, @Nullable String newValue, ConfigurationType configType) {
        super(previousValue, newValue, configType);
    }

    public UpdateConfigurationChange(Long id, @Nullable String previousValue, @Nullable String newValue, ConfigurationType configType) {
        super(id, previousValue, newValue, configType);
    }

    public UpdateConfigurationChange(String newValue, ConfigurationType configType) {
        super(null, newValue, configType);
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
