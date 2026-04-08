package com.ohpenl.midoffice.configurationtracker.service;

import com.ohpenl.midoffice.configurationtracker.domain.AddConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationDataType;
import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationType;
import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationChangeAction;
import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationChangeEntity;
import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationTypeEntity;
import com.ohpenl.midoffice.configurationtracker.problem.exception.NotFoundException;
import com.ohpenl.midoffice.configurationtracker.repository.ConfigurationChangeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ChangeHistoryServiceTest {

    @Mock
    private ConfigurationChangeRepository configurationChangeRepository;

    @InjectMocks
    private ChangeHistoryService changeHistoryService;

    private final ConfigurationType configType = new ConfigurationType(ConfigurationDataType.STRING, "feature", 1L, 0L);

    @Test
    void getLatestByType_shouldReturnChange_whenExists() {
        var typeEntity = new ConfigurationTypeEntity("feature", ConfigurationDataType.STRING);
        typeEntity.setId(1L);
        var entity = new ConfigurationChangeEntity(typeEntity, null, "val", ConfigurationChangeAction.ADD);
        entity.setId(1L);
        given(configurationChangeRepository.findLatestByConfigType("feature")).willReturn(Optional.of(entity));

        var result = changeHistoryService.getLatestByType(configType);

        assertNotNull(result);
        assertThat(result).isInstanceOf(AddConfigurationChange.class);
        assertThat(result.getNewValue()).isEqualTo("val");
    }

    @Test
    void getLatestByType_shouldReturnNull_whenNotExists() {
        given(configurationChangeRepository.findLatestByConfigType("feature")).willReturn(Optional.empty());

        var result = changeHistoryService.getLatestByType(configType);

        assertThat(result).isNull();
    }

    @Test
    void getLatestEntityByType_shouldReturnEntity_whenExists() {
        var typeEntity = new ConfigurationTypeEntity("feature", ConfigurationDataType.STRING);
        var entity = new ConfigurationChangeEntity(typeEntity, null, "val", ConfigurationChangeAction.ADD);
        given(configurationChangeRepository.findLatestByConfigType("feature")).willReturn(Optional.of(entity));

        var result = changeHistoryService.getLatestEntityByType(configType);

        assertThat(result).isEqualTo(entity);
    }

    @Test
    void getLatestEntityByType_shouldReturnNull_whenNotExists() {
        given(configurationChangeRepository.findLatestByConfigType("feature")).willReturn(Optional.empty());

        var result = changeHistoryService.getLatestEntityByType(configType);

        assertThat(result).isNull();
    }

    @Test
    void getById_shouldReturnResponse_whenExists() {
        var typeEntity = new ConfigurationTypeEntity("feature", ConfigurationDataType.STRING);
        typeEntity.setId(1L);
        var entity = new ConfigurationChangeEntity(typeEntity, null, "val", ConfigurationChangeAction.ADD);
        entity.setId(10L);
        entity.setVersion(0L);
        given(configurationChangeRepository.findById(10L)).willReturn(Optional.of(entity));

        var result = changeHistoryService.getById(10L);

        assertThat(result.getId()).isEqualTo(10L);
    }

    @Test
    void getById_shouldThrow_whenNotExists() {
        given(configurationChangeRepository.findById(10L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> changeHistoryService.getById(10L))
                .isInstanceOf(NotFoundException.class);
    }
}
