package com.ohpenl.midoffice.configurationtracker.service;

import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationType;
import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationChangeEntity;
import com.ohpenl.midoffice.configurationtracker.mapper.ConfigurationChangeMapper;
import com.ohpenl.midoffice.configurationtracker.repository.ConfigurationChangeRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChangeHistoryService {

    private final ConfigurationChangeRepository configurationChangeRepository;

    public @Nullable ConfigurationChange getLatestByType(ConfigurationType configType) {
        var latest = configurationChangeRepository.findLatestByConfigType(configType.name());
        return latest.map(ConfigurationChangeMapper::fromEntity).orElse(null);
    }

    public @Nullable ConfigurationChangeEntity getLatestEntityByType(ConfigurationType configType) {
        var latest = configurationChangeRepository.findLatestByConfigType(configType.name());
        return latest.orElse(null);
    }
}
