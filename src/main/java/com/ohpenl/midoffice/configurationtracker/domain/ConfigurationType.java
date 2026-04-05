package com.ohpenl.midoffice.configurationtracker.domain;

public record ConfigurationType(ConfigurationDataType dataType, String name) {
    public <T> ConfigurationType(String name, Class<T> dataType) {
        this(ConfigurationDataType.fromClass(dataType), name);
    }
}
