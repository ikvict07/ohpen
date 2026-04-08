package com.ohpenl.midoffice.configurationtracker.service;

import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationDataType;
import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationTypeEntity;
import com.ohpenl.midoffice.configurationtracker.repository.ConfigurationTypeRepository;
import com.ohpenl.midoffice.configurationtracker.validator.ConfigurationTypeValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ConfigurationTypeServiceTest {

    @Mock
    private ConfigurationTypeRepository configurationTypeRepository;

    @Mock
    private ConfigurationTypeValidator configurationTypeValidator;

    @InjectMocks
    private ConfigurationTypeService configurationTypeService;

    @Test
    void getById_shouldReturnEntity_whenExists() {
        var entity = new ConfigurationTypeEntity("feature-flag", ConfigurationDataType.BOOLEAN);
        entity.setId(1L);
        given(configurationTypeRepository.findById(1L)).willReturn(Optional.of(entity));

        var result = configurationTypeService.getById(1L);

        assertThat(result).isEqualTo(entity);
    }

    @Test
    void getById_shouldThrow_whenNotExists() {
        given(configurationTypeRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> configurationTypeService.getById(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void createConfigurationType_shouldValidateAndSave() {
        var entity = new ConfigurationTypeEntity("timeout", ConfigurationDataType.NUMBER);
        given(configurationTypeRepository.save(any(ConfigurationTypeEntity.class))).willReturn(entity);

        var result = configurationTypeService.createConfigurationType("timeout", ConfigurationDataType.NUMBER);

        then(configurationTypeValidator).should().validateUniqueName("timeout");
        assertThat(result.getName()).isEqualTo("timeout");
        assertThat(result.getDataType()).isEqualTo(ConfigurationDataType.NUMBER);
    }

    @Test
    void listConfigurationTypes_shouldReturnAll() {
        var entities = List.of(
                new ConfigurationTypeEntity("a", ConfigurationDataType.STRING),
                new ConfigurationTypeEntity("b", ConfigurationDataType.BOOLEAN)
        );
        given(configurationTypeRepository.findAll()).willReturn(entities);

        var result = configurationTypeService.listConfigurationTypes();

        assertThat(result).hasSize(2);
    }
}
