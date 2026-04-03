package com.ohpenl.midoffice.configurationtracker.mapper;

import com.ohpenl.midoffice.configurationtracker.api.model.ConfigurationChangePage;
import com.ohpenl.midoffice.configurationtracker.api.model.ConfigurationChangeResponse;
import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationChange;
import org.springframework.data.domain.Page;

import java.time.ZoneOffset;

public final class ConfigurationChangeMapper {
    private ConfigurationChangeMapper() {
    }

    public static ConfigurationChangePage toApiPage(Page<ConfigurationChange> configurationChanges) {
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

    public static ConfigurationChangeResponse toApi(ConfigurationChange configurationChange) {
        return new ConfigurationChangeResponse(
                configurationChange.getId(),
                DataTypeMapper.toApi(configurationChange.getConfigType()),
                configurationChange.getPreviousValue(),
                configurationChange.getNewValue(),
                configurationChange.getTimestamp().atOffset(ZoneOffset.UTC)
        );
    }
}
