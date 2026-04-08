package com.ohpenl.midoffice.configurationtracker.facade;

import com.ohpenl.midoffice.configurationtracker.api.model.Action;
import com.ohpenl.midoffice.configurationtracker.domain.AddConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationDataType;
import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationType;
import com.ohpenl.midoffice.configurationtracker.domain.RemoveConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.domain.UpdateConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationChangeAction;
import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationChangeEntity;
import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationTypeEntity;
import com.ohpenl.midoffice.configurationtracker.service.ConfigurationChangeService;
import com.ohpenl.midoffice.configurationtracker.service.ConfigurationTypeService;
import com.ohpenl.midoffice.configurationtracker.validator.ConfigurationChangeValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class ConfigurationChangeFacadeTest {

    @Mock
    private ConfigurationChangeService configurationChangeService;

    @Mock
    private ConfigurationTypeService configurationTypeService;

    @Mock
    private ConfigurationChangeValidator configurationChangeValidator;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private ConfigurationChangeFacade facade;

    private final ConfigurationTypeEntity typeEntity = createTypeEntity();

    private static ConfigurationTypeEntity createTypeEntity() {
        var e = new ConfigurationTypeEntity("feature", ConfigurationDataType.STRING);
        e.setId(1L);
        return e;
    }

    @Test
    void applyConfigurationChange_shouldAdd() {
        var configType = new ConfigurationType(ConfigurationDataType.STRING, "feature", 1L, 0L);
        var domainChange = new AddConfigurationChange("val", configType);
        var savedEntity = new ConfigurationChangeEntity(typeEntity, null, "val", ConfigurationChangeAction.ADD);
        savedEntity.setId(10L);
        savedEntity.setVersion(0L);

        given(configurationTypeService.getById(1L)).willReturn(typeEntity);
        given(configurationChangeService.addConfiguration(eq("val"), any())).willReturn(domainChange);
        given(configurationChangeService.save(domainChange, 0L)).willReturn(savedEntity);

        var result = facade.applyConfigurationChange(Action.ADD, "val", 1L, 0L);

        then(configurationChangeValidator).should().validateDataTypes(eq("val"), any());
        then(eventPublisher).should().publishEvent(domainChange);
        assertThat(result).isNotNull();
    }

    @Test
    void applyConfigurationChange_shouldUpdate() {
        var configType = new ConfigurationType(ConfigurationDataType.STRING, "feature", 1L, 0L);
        var domainChange = new UpdateConfigurationChange("old", "new", configType);
        var savedEntity = new ConfigurationChangeEntity(typeEntity, "old", "new", ConfigurationChangeAction.UPDATE);
        savedEntity.setId(11L);
        savedEntity.setVersion(1L);

        given(configurationTypeService.getById(1L)).willReturn(typeEntity);
        given(configurationChangeService.changeConfiguration(eq("new"), any())).willReturn(domainChange);
        given(configurationChangeService.save(domainChange, 1L)).willReturn(savedEntity);

        var result = facade.applyConfigurationChange(Action.UPDATE, "new", 1L, 1L);

        then(configurationChangeValidator).should().validateDataTypes(eq("new"), any());
        assertThat(result).isNotNull();
    }

    @Test
    void applyConfigurationChange_shouldRemoveWithoutValidation() {
        var configType = new ConfigurationType(ConfigurationDataType.STRING, "feature", 1L, 0L);
        var domainChange = new RemoveConfigurationChange("old", configType);
        var savedEntity = new ConfigurationChangeEntity(typeEntity, "old", null, ConfigurationChangeAction.REMOVE);
        savedEntity.setId(12L);
        savedEntity.setVersion(2L);

        given(configurationTypeService.getById(1L)).willReturn(typeEntity);
        given(configurationChangeService.removeConfiguration(any())).willReturn(domainChange);
        given(configurationChangeService.save(domainChange, 2L)).willReturn(savedEntity);

        var result = facade.applyConfigurationChange(Action.REMOVE, null, 1L, 2L);

        then(configurationChangeValidator).should(never()).validateDataTypes(any(), any());
        assertThat(result).isNotNull();
    }
}
