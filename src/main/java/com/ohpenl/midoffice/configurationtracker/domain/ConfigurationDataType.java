package com.ohpenl.midoffice.configurationtracker.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ConfigurationDataType {

    STRING(String.class),
    NUMBER(Number.class),
    BOOLEAN(Boolean.class);

    public final Class<?> dataType;

    public static ConfigurationDataType fromClass(Class<?> clazz) {
        for (ConfigurationDataType type : values()) {
            if (type.dataType.isAssignableFrom(clazz)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unsupported data type: " + clazz.getName());
    }
}
