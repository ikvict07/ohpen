package com.ohpenl.midoffice.configurationtracker.service;

import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationType;
import com.ohpenl.midoffice.configurationtracker.problem.exception.NotFoundException;
import com.ohpenl.midoffice.configurationtracker.repository.ConfigurationChangeRepository;
import com.ohpenl.midoffice.configurationtracker.validator.ConfigurationChangeValidator;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConfigurationChangesService {

    private final ConfigurationChangeRepository configurationChangeRepository;
    private final ConfigurationTypeService configurationTypeService;
    private final ConfigurationChangeValidator configurationChangeValidator;

    @Transactional
    public ConfigurationChange createConfigurationChange(Long configTypeId, String previousValue, String newValue) {
        ConfigurationType configType = configurationTypeService.getConfigurationType(configTypeId);

        ConfigurationChange newChange = new ConfigurationChange(configType, previousValue, newValue);
        ConfigurationChange lastChange = configurationChangeRepository.findLatestByConfigType(configType.getName())
                .orElse(null);

        configurationChangeValidator.validatePreviousValue(lastChange, newChange);
        configurationChangeValidator.validateDataTypes(newChange);


        return configurationChangeRepository.save(newChange);
    }

    @Transactional
    public void deleteConfigurationChange(Long id) {
        ConfigurationChange change = configurationChangeRepository.findById(id).orElseThrow(() -> new NotFoundException("Configuration change not found with id: " + id));
        ConfigurationType configType = change.getConfigType();
        ConfigurationChange latestChange = configurationChangeRepository.findLatestByConfigType(configType.getName())
                .orElseThrow(() -> new IllegalStateException("No previous configuration change found for configuration type: " + configType.getName()));
        if (latestChange.getId().equals(change.getId())) {
            configurationChangeRepository.delete(latestChange);
        }
    }

    public ConfigurationChange getConfigurationChange(Long id) {
        return configurationChangeRepository.findById(id).orElseThrow(() -> new NotFoundException("Configuration change not found with id: " + id));
    }

    public Page<ConfigurationChange> listConfigurationChanges(Pageable page, @Nullable String configTypeName) {
        if (configTypeName == null) {
            return configurationChangeRepository.findAll(page);
        }
        return configurationChangeRepository.findAllByConfigType_Name(configTypeName, page);
    }

}
