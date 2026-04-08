package com.ohpenl.midoffice.configurationtracker.mapper;

import com.ohpenl.midoffice.configurationtracker.api.model.DataType;
import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationDataType;
import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationTypeEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DataTypeMapperTest {

    @Test
    void toApi_shouldMapAllDataTypes() {
        assertThat(DataTypeMapper.toApi(ConfigurationDataType.STRING)).isEqualTo(DataType.STRING);
        assertThat(DataTypeMapper.toApi(ConfigurationDataType.NUMBER)).isEqualTo(DataType.NUMBER);
        assertThat(DataTypeMapper.toApi(ConfigurationDataType.BOOLEAN)).isEqualTo(DataType.BOOLEAN);
    }

    @Test
    void toDomain_shouldMapAllDataTypes() {
        assertThat(DataTypeMapper.toDomain(DataType.STRING)).isEqualTo(ConfigurationDataType.STRING);
        assertThat(DataTypeMapper.toDomain(DataType.NUMBER)).isEqualTo(ConfigurationDataType.NUMBER);
        assertThat(DataTypeMapper.toDomain(DataType.BOOLEAN)).isEqualTo(ConfigurationDataType.BOOLEAN);
    }

    @Test
    void toApi_fromEntity_shouldMapCorrectly() {
        var entity = new ConfigurationTypeEntity("timeout", ConfigurationDataType.NUMBER);
        entity.setId(5L);

        var result = DataTypeMapper.toApi(entity);

        assertThat(result.getId()).isEqualTo(5L);
        assertThat(result.getName()).isEqualTo("timeout");
        assertThat(result.getDataType()).isEqualTo(DataType.NUMBER);
    }

    @Test
    void toDomain_fromEntity_shouldMapCorrectly() {
        var entity = new ConfigurationTypeEntity("flag", ConfigurationDataType.BOOLEAN);
        entity.setId(3L);

        var result = DataTypeMapper.toDomain(entity);

        assertThat(result.id()).isEqualTo(3L);
        assertThat(result.name()).isEqualTo("flag");
        assertThat(result.dataType()).isEqualTo(ConfigurationDataType.BOOLEAN);
    }
}
