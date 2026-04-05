package com.ohpenl.midoffice.configurationtracker.event;


import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationChangeEntity;
import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationTypeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public final class ConfigurationChangeDeletedEvent extends ConfigurationChangeEvent {

    private final Long id;

    private final ConfigurationTypeEntity configType;

    public static ConfigurationChangeDeletedEvent from(ConfigurationChangeEntity configurationChange) {
        return new ConfigurationChangeDeletedEvent(configurationChange.getId(), configurationChange.getConfigType());
    }
}
