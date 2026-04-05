package com.ohpenl.midoffice.configurationtracker.event;

import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationTypeEntity;

public abstract sealed class ConfigurationChangeEvent permits ConfigurationChangeCreatedEvent, ConfigurationChangeDeletedEvent, ConfigurationChangeUpdatedEvent {
    public abstract Long getId();

    public abstract ConfigurationTypeEntity getConfigType();

}
