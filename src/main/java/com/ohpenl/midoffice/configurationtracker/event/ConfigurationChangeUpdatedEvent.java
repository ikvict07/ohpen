package com.ohpenl.midoffice.configurationtracker.event;

import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationChangeEntity;
import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationTypeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public final class ConfigurationChangeUpdatedEvent extends ConfigurationChangeEvent {

    private final Long id;

    private final String newValue;

    private final ConfigurationTypeEntity configType;

    public static ConfigurationChangeUpdatedEvent from(ConfigurationChangeEntity configurationChange) {
        return new ConfigurationChangeUpdatedEvent(configurationChange.getId(), configurationChange.getNewValue(), configurationChange.getConfigType());
    }
}
