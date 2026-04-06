package com.ohpenl.midoffice.configurationtracker.service;

import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationTypeEntity;
import com.ohpenl.midoffice.configurationtracker.repository.ConfigurationTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConfigurationTypeService {


    private final ConfigurationTypeRepository configurationTypeRepository;


    public ConfigurationTypeEntity getById(Long id) {
        return configurationTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Configuration type with id " + id + " not found"));
    }

    public ConfigurationTypeEntity createConfigurationType(String name, com.ohpenl.midoffice.configurationtracker.domain.ConfigurationDataType dataType) {
        if (configurationTypeRepository.existsByName(name)) {
            throw new IllegalArgumentException("Configuration type with name " + name + " already exists");
        }
        ConfigurationTypeEntity entity = new ConfigurationTypeEntity();
        entity.setName(name);
        entity.setDataType(dataType);
        return configurationTypeRepository.save(entity);
    }

    public ConfigurationTypeEntity getConfigurationType(Long id) {
        return getById(id);
    }

    public List<ConfigurationTypeEntity> listConfigurationTypes() {
        return configurationTypeRepository.findAll();
    }
}
