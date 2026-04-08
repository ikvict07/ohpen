package com.ohpenl.midoffice.configurationtracker.service;

import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationDataType;
import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationTypeEntity;
import com.ohpenl.midoffice.configurationtracker.repository.ConfigurationTypeRepository;
import com.ohpenl.midoffice.configurationtracker.validator.ConfigurationTypeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConfigurationTypeService {


    private final ConfigurationTypeRepository configurationTypeRepository;
    private final ConfigurationTypeValidator configurationTypeValidator;

    public ConfigurationTypeEntity getById(Long id) {
        return configurationTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Configuration type with id " + id + " not found"));
    }

    public ConfigurationTypeEntity createConfigurationType(String name, ConfigurationDataType dataType) {
        configurationTypeValidator.validateUniqueName(name);
        ConfigurationTypeEntity entity = new ConfigurationTypeEntity(name, dataType);
        return configurationTypeRepository.save(entity);
    }

    public ConfigurationTypeEntity getConfigurationType(Long id) {
        return getById(id);
    }

    public List<ConfigurationTypeEntity> listConfigurationTypes() {
        return configurationTypeRepository.findAll();
    }
}
