package com.ohpenl.midoffice.configurationtracker.mapper;

import com.ohpenl.midoffice.configurationtracker.api.model.ConfigurationTypeResponse;
import com.ohpenl.midoffice.configurationtracker.api.model.DataType;
import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationType;
import com.ohpenl.midoffice.configurationtracker.enums.ConfigurationDataType;

public final class DataTypeMapper {

    private DataTypeMapper() {
    }

    public static DataType toApi(ConfigurationDataType dataType) {
        return switch (dataType) {
            case STRING -> DataType.STRING;
            case NUMBER -> DataType.NUMBER;
            case BOOLEAN -> DataType.BOOLEAN;
        };
    }

    public static ConfigurationDataType toDomain(DataType dataType) {
        return switch (dataType) {
            case STRING -> ConfigurationDataType.STRING;
            case NUMBER -> ConfigurationDataType.NUMBER;
            case BOOLEAN -> ConfigurationDataType.BOOLEAN;
        };
    }

    public static ConfigurationTypeResponse toApi(ConfigurationType configurationType) {
        return new ConfigurationTypeResponse()
                .id(configurationType.getId())
                .name(configurationType.getName())
                .dataType(toApi(configurationType.getDataType()));
    }
}
