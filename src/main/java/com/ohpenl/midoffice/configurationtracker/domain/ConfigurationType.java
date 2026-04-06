package com.ohpenl.midoffice.configurationtracker.domain;

public record ConfigurationType(ConfigurationDataType dataType, String name, Long id, Long version) {
    public <T> ConfigurationType(String name, Class<T> dataType) {
        this(ConfigurationDataType.fromClass(dataType), name, 0L, 0L);
    }
}
