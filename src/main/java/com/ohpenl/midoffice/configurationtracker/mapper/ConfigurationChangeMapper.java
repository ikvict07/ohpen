package com.ohpenl.midoffice.configurationtracker.mapper;

import com.ohpenl.midoffice.configurationtracker.api.model.ConfigurationChangePage;
import com.ohpenl.midoffice.configurationtracker.api.model.ConfigurationChangeResponse;
import com.ohpenl.midoffice.configurationtracker.domain.AddConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.domain.RemoveConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.domain.UpdateConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationChangeEntity;
import org.springframework.data.domain.Page;

import java.time.ZoneOffset;

public final class ConfigurationChangeMapper {
    private ConfigurationChangeMapper() {
    }

    public static ConfigurationChangePage toApiPage(Page<ConfigurationChangeEntity> configurationChanges) {
        return new ConfigurationChangePage(
                configurationChanges
                        .stream()
                        .map(ConfigurationChangeMapper::toApi)
                        .toList(),
                configurationChanges.getTotalElements(),
                configurationChanges.getTotalPages(),
                configurationChanges.getNumber(),
                configurationChanges.getSize()
        );
    }

    public static ConfigurationChangeResponse toApi(ConfigurationChangeEntity configurationChange) {
        return new ConfigurationChangeResponse(
                configurationChange.getId(),
                DataTypeMapper.toApi(configurationChange.getConfigType()),
                configurationChange.getPreviousValue(),
                configurationChange.getNewValue(),
                configurationChange.getTimestamp().atOffset(ZoneOffset.UTC)
        );
    }

    public static <T> ConfigurationChange<T> toDomain(ConfigurationChangeEntity configurationChangeEntity) {
        return switch (configurationChangeEntity.getAction()) {
            case ADD -> {
                var type = DataTypeMapper.toDomain(configurationChangeEntity.getConfigType()).dataType().dataType;
                if (!type.isAssignableFrom(configurationChangeEntity.getNewValue().getClass())) {
                    throw new IllegalArgumentException("Invalid value type");
                }
                //noinspection unchecked
                yield new AddConfigurationChange<>(
                        (T) type.cast(configurationChangeEntity.getNewValue()),
                        DataTypeMapper.toDomain(configurationChangeEntity.getConfigType())
                );
            }
            case UPDATE -> {
                var type = DataTypeMapper.toDomain(configurationChangeEntity.getConfigType()).dataType().dataType;
                if (!type.isAssignableFrom(configurationChangeEntity.getNewValue().getClass())) {
                    throw new IllegalArgumentException("Invalid value type");
                }

                //noinspection unchecked
                yield new UpdateConfigurationChange<>(
                        (T) type.cast(configurationChangeEntity.getPreviousValue()),
                        (T) type.cast(configurationChangeEntity.getNewValue()),
                        DataTypeMapper.toDomain(configurationChangeEntity.getConfigType())
                );
            }
            case REMOVE -> {
                var type = DataTypeMapper.toDomain(configurationChangeEntity.getConfigType()).dataType().dataType;
                if (!type.isAssignableFrom(configurationChangeEntity.getPreviousValue().getClass())) {
                    throw new IllegalArgumentException("Invalid value type");
                }

                //noinspection unchecked
                yield new RemoveConfigurationChange<>(
                        (T) type.cast(configurationChangeEntity.getPreviousValue()),
                        DataTypeMapper.toDomain(configurationChangeEntity.getConfigType())
                );
            }
        };
    }
}
