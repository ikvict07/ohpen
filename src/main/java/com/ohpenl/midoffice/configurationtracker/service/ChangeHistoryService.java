package com.ohpenl.midoffice.configurationtracker.service;

import com.ohpenl.midoffice.configurationtracker.api.model.ConfigurationChangePage;
import com.ohpenl.midoffice.configurationtracker.api.model.ConfigurationChangeResponse;
import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationType;
import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationChangeEntity;
import com.ohpenl.midoffice.configurationtracker.mapper.ConfigurationChangeMapper;
import com.ohpenl.midoffice.configurationtracker.problem.exception.NotFoundException;
import com.ohpenl.midoffice.configurationtracker.repository.ConfigurationChangeRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

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

    public ConfigurationChangeResponse getById(Long id) {
        var entity = configurationChangeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Configuration change not found"));
        return ConfigurationChangeMapper.toApi(ConfigurationChangeMapper.fromEntity(entity), entity.getVersion());
    }

    public ConfigurationChangePage list(String configTypeName, OffsetDateTime from, OffsetDateTime to, Integer page, Integer size) {
        var pageable = PageRequest.of(page, size);
        boolean hasType = configTypeName != null;
        boolean hasTimeRange = from != null && to != null;
        LocalDateTime fromLocal = hasTimeRange ? from.toLocalDateTime() : null;
        LocalDateTime toLocal = hasTimeRange ? to.toLocalDateTime() : null;
        final Page<ConfigurationChangeEntity> resultPage;
        if (hasType && hasTimeRange) {
            resultPage = configurationChangeRepository.findAllByConfigType_NameAndTimestampBetween(configTypeName, fromLocal, toLocal, pageable);
        } else if (hasTimeRange) {
            resultPage = configurationChangeRepository.findAllByTimestampBetween(fromLocal, toLocal, pageable);
        } else if (hasType) {
            resultPage = configurationChangeRepository.findAllByConfigType_Name(configTypeName, pageable);
        } else {
            resultPage = configurationChangeRepository.findAll(pageable);
        }
        return ConfigurationChangeMapper.toApiPage(resultPage);
    }
}
