package com.ohpenl.midoffice.configurationtracker.mapper;

import com.ohpenl.midoffice.configurationtracker.api.model.ConfigurationChangePage;
import com.ohpenl.midoffice.configurationtracker.api.model.ConfigurationChangeResponse;
import com.ohpenl.midoffice.configurationtracker.domain.AddConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationType;
import com.ohpenl.midoffice.configurationtracker.domain.RemoveConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.domain.UpdateConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationChangeEntity;
import org.springframework.data.domain.Page;

import java.time.ZoneOffset;
import java.util.Objects;

import com.ohpenl.midoffice.configurationtracker.api.model.Action;

public final class ConfigurationChangeMapper {
    private ConfigurationChangeMapper() {
    }

    public static ConfigurationChangePage toApiPage(Page<ConfigurationChangeEntity> configurationChanges) {
        return new ConfigurationChangePage(
                configurationChanges
                        .stream()
                        .map(entity -> toApi(fromEntity(entity), entity.getVersion()))
                        .toList(),
                configurationChanges.getTotalElements(),
                configurationChanges.getTotalPages(),
                configurationChanges.getNumber(),
                configurationChanges.getSize()
        );
    }

    public static ConfigurationChange fromEntity(ConfigurationChangeEntity entity) {
        ConfigurationType configType = DataTypeMapper.toDomain(entity.getConfigType());
        ConfigurationChange change = switch (entity.getAction()) {
            case ADD -> new AddConfigurationChange(entity.getNewValue(), configType);
            case UPDATE -> new UpdateConfigurationChange(entity.getPreviousValue(), entity.getNewValue(), configType);
            case REMOVE -> new RemoveConfigurationChange(entity.getPreviousValue(), configType);
        };
        change.setId(entity.getId());
        change.setTimestamp(entity.getTimestamp());
        return change;
    }

    public static ConfigurationChangeResponse toApi(ConfigurationChange configurationChange, Long version) {
        return switch (configurationChange) {
            case AddConfigurationChange addConfigurationChange -> new ConfigurationChangeResponse(
                    configurationChange.getId(),
                    version,
                    DataTypeMapper.toApi(addConfigurationChange.getConfigType()),
                    null,
                    Objects.requireNonNull(addConfigurationChange.getNewValue()),
                    configurationChange.getTimestamp().atOffset(ZoneOffset.UTC)
            ).action(Action.ADD);
            case RemoveConfigurationChange removeConfigurationChange -> new ConfigurationChangeResponse(
                    configurationChange.getId(),
                    version,
                    DataTypeMapper.toApi(removeConfigurationChange.getConfigType()),
                    Objects.requireNonNull(removeConfigurationChange.getPreviousValue()),
                    null,
                    configurationChange.getTimestamp().atOffset(ZoneOffset.UTC)
            ).action(Action.REMOVE);
            case UpdateConfigurationChange updateConfigurationChange -> new ConfigurationChangeResponse(
                    configurationChange.getId(),
                    version,
                    DataTypeMapper.toApi(updateConfigurationChange.getConfigType()),
                    Objects.requireNonNull(updateConfigurationChange.getNewValue()),
                    Objects.requireNonNull(updateConfigurationChange.getPreviousValue()),
                    configurationChange.getTimestamp().atOffset(ZoneOffset.UTC)
            ).action(Action.UPDATE);
        };
    }

}
