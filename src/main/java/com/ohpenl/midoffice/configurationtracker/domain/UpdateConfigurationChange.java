package com.ohpenl.midoffice.configurationtracker.domain;

import org.jspecify.annotations.Nullable;

public final class UpdateConfigurationChange<T> extends ConfigurationChange<T> {

    public UpdateConfigurationChange(@Nullable T previousValue, @Nullable T newValue, ConfigurationType configType) {
        super(previousValue, newValue, configType);
    }

    public UpdateConfigurationChange<T> update(T newValue) {
        return new UpdateConfigurationChange<>(
                this.getPreviousValue(),
                newValue,
                this.getConfigType()
        );
    }

    public RemoveConfigurationChange<T> remove() {
        return new RemoveConfigurationChange<>(
                this.getPreviousValue(),
                this.getNewValue(),
                this.getConfigType()
        );
    }
}
