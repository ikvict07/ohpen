package com.ohpenl.midoffice.configurationtracker.service;

import com.ohpenl.midoffice.configurationtracker.domain.AddConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationDataType;
import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationType;
import com.ohpenl.midoffice.configurationtracker.domain.RemoveConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.domain.UpdateConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationChangeAction;
import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationChangeEntity;
import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationTypeEntity;
import com.ohpenl.midoffice.configurationtracker.problem.exception.IllegalTransitionException;
import com.ohpenl.midoffice.configurationtracker.repository.ConfigurationChangeRepository;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ConfigurationChangeServiceTest {

    @Mock
    private ChangeHistoryService changeHistoryService;

    @Mock
    private ConfigurationTypeService configurationTypeService;

    @Mock
    private ConfigurationChangeRepository configurationChangeRepository;

    @InjectMocks
    private ConfigurationChangeService configurationChangeService;

    private final ConfigurationType configType = new ConfigurationType(ConfigurationDataType.STRING, "feature", 1L, 0L);

    // --- addConfiguration ---

    @Test
    void addConfiguration_shouldCreateAdd_whenNoLatest() {
        given(changeHistoryService.getLatestByType(configType)).willReturn(null);

        var result = configurationChangeService.addConfiguration("value", configType);

        assertThat(result).isInstanceOf(AddConfigurationChange.class);
        assertThat(result.getNewValue()).isEqualTo("value");
    }

    @Test
    void addConfiguration_shouldThrow_whenLatestIsAdd() {
        given(changeHistoryService.getLatestByType(configType))
                .willReturn(new AddConfigurationChange("old", configType));

        assertThatThrownBy(() -> configurationChangeService.addConfiguration("value", configType))
                .isInstanceOf(IllegalTransitionException.class);
    }

    @Test
    void addConfiguration_shouldThrow_whenLatestIsUpdate() {
        given(changeHistoryService.getLatestByType(configType))
                .willReturn(new UpdateConfigurationChange("old", "current", configType));

        assertThatThrownBy(() -> configurationChangeService.addConfiguration("value", configType))
                .isInstanceOf(IllegalTransitionException.class);
    }

    @Test
    void addConfiguration_shouldRecreate_whenLatestIsRemove() {
        given(changeHistoryService.getLatestByType(configType))
                .willReturn(new RemoveConfigurationChange("old", configType));

        var result = configurationChangeService.addConfiguration("new", configType);

        assertThat(result).isInstanceOf(AddConfigurationChange.class);
        assertThat(result.getNewValue()).isEqualTo("new");
    }

    // --- removeConfiguration ---

    @Test
    void removeConfiguration_shouldThrow_whenNoLatest() {
        given(changeHistoryService.getLatestByType(configType)).willReturn(null);

        assertThatThrownBy(() -> configurationChangeService.removeConfiguration(configType))
                .isInstanceOf(IllegalTransitionException.class);
    }

    @Test
    void removeConfiguration_shouldRemove_whenLatestIsAdd() {
        given(changeHistoryService.getLatestByType(configType))
                .willReturn(new AddConfigurationChange("val", configType));

        var result = configurationChangeService.removeConfiguration(configType);

        assertThat(result).isInstanceOf(RemoveConfigurationChange.class);
    }

    @Test
    void removeConfiguration_shouldThrow_whenLatestIsRemove() {
        given(changeHistoryService.getLatestByType(configType))
                .willReturn(new RemoveConfigurationChange("val", configType));

        assertThatThrownBy(() -> configurationChangeService.removeConfiguration(configType))
                .isInstanceOf(IllegalTransitionException.class);
    }

    @Test
    void removeConfiguration_shouldRemove_whenLatestIsUpdate() {
        given(changeHistoryService.getLatestByType(configType))
                .willReturn(new UpdateConfigurationChange("old", "current", configType));

        var result = configurationChangeService.removeConfiguration(configType);

        assertThat(result).isInstanceOf(RemoveConfigurationChange.class);
    }

    // --- changeConfiguration ---

    @Test
    void changeConfiguration_shouldThrow_whenNoLatest() {
        given(changeHistoryService.getLatestByType(configType)).willReturn(null);

        assertThatThrownBy(() -> configurationChangeService.changeConfiguration("val", configType))
                .isInstanceOf(IllegalTransitionException.class);
    }

    @Test
    void changeConfiguration_shouldUpdate_whenLatestIsAdd() {
        given(changeHistoryService.getLatestByType(configType))
                .willReturn(new AddConfigurationChange("old", configType));

        var result = configurationChangeService.changeConfiguration("new", configType);

        assertThat(result).isInstanceOf(UpdateConfigurationChange.class);
        assertThat(result.getNewValue()).isEqualTo("new");
    }

    @Test
    void changeConfiguration_shouldThrow_whenLatestIsRemove() {
        given(changeHistoryService.getLatestByType(configType))
                .willReturn(new RemoveConfigurationChange("old", configType));

        assertThatThrownBy(() -> configurationChangeService.changeConfiguration("val", configType))
                .isInstanceOf(IllegalTransitionException.class);
    }

    @Test
    void changeConfiguration_shouldUpdate_whenLatestIsUpdate() {
        given(changeHistoryService.getLatestByType(configType))
                .willReturn(new UpdateConfigurationChange("old", "current", configType));

        var result = configurationChangeService.changeConfiguration("new", configType);

        assertThat(result).isInstanceOf(UpdateConfigurationChange.class);
        assertThat(result.getNewValue()).isEqualTo("new");
        assertThat(result.getPreviousValue()).isEqualTo("current");
    }

    // --- save ---

    @Test
    void save_shouldSaveWithNextVersion_whenLatestEntityExists() {
        var change = new AddConfigurationChange("val", configType);
        var typeEntity = new ConfigurationTypeEntity("feature", ConfigurationDataType.STRING);
        typeEntity.setId(1L);

        var latestEntity = new ConfigurationChangeEntity();
        latestEntity.setVersion(2L);

        var savedEntity = new ConfigurationChangeEntity(typeEntity, null, "val", ConfigurationChangeAction.ADD);
        savedEntity.setId(1L);
        savedEntity.setVersion(3L);

        given(configurationTypeService.getById(1L)).willReturn(typeEntity);
        given(changeHistoryService.getLatestEntityByType(configType)).willReturn(latestEntity);
        given(configurationChangeRepository.save(any())).willReturn(savedEntity);

        var result = configurationChangeService.save(change, 2L);

        assertThat(result.getVersion()).isEqualTo(3L);
    }

    @Test
    void save_shouldThrowOptimisticLock_whenVersionMismatch() {
        var change = new AddConfigurationChange("val", configType);
        var typeEntity = new ConfigurationTypeEntity("feature", ConfigurationDataType.STRING);
        typeEntity.setId(1L);

        var latestEntity = new ConfigurationChangeEntity();
        latestEntity.setVersion(3L);

        given(configurationTypeService.getById(1L)).willReturn(typeEntity);
        given(changeHistoryService.getLatestEntityByType(configType)).willReturn(latestEntity);

        assertThatThrownBy(() -> configurationChangeService.save(change, 1L))
                .isInstanceOf(OptimisticLockException.class);
    }

    @Test
    void save_shouldSaveWithVersionZero_whenNoLatestEntity() {
        var change = new AddConfigurationChange("val", configType);
        var typeEntity = new ConfigurationTypeEntity("feature", ConfigurationDataType.STRING);
        typeEntity.setId(1L);

        var savedEntity = new ConfigurationChangeEntity(typeEntity, null, "val", ConfigurationChangeAction.ADD);
        savedEntity.setId(1L);
        savedEntity.setVersion(0L);

        given(configurationTypeService.getById(1L)).willReturn(typeEntity);
        given(changeHistoryService.getLatestEntityByType(configType)).willReturn(null);
        given(configurationChangeRepository.save(any())).willReturn(savedEntity);

        var result = configurationChangeService.save(change, null);

        assertThat(result.getVersion()).isZero();
    }

    @Test
    void save_shouldThrowOptimisticLock_whenNoLatestButVersionNonZero() {
        var change = new AddConfigurationChange("val", configType);
        var typeEntity = new ConfigurationTypeEntity("feature", ConfigurationDataType.STRING);
        typeEntity.setId(1L);

        given(configurationTypeService.getById(1L)).willReturn(typeEntity);
        given(changeHistoryService.getLatestEntityByType(configType)).willReturn(null);

        assertThatThrownBy(() -> configurationChangeService.save(change, 5L))
                .isInstanceOf(OptimisticLockException.class);
    }
}
