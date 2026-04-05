package com.ohpenl.midoffice.configurationtracker.domain;

import org.jspecify.annotations.Nullable;

public final class AddConfigurationChange<T> extends ConfigurationChange<T> {

    public AddConfigurationChange(@Nullable T newValue, ConfigurationType configType) {
        super(null, newValue, configType);
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
