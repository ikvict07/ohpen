package com.ohpenl.midoffice.configurationtracker.service;

import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationType;
import com.ohpenl.midoffice.configurationtracker.enums.ConfigurationDataType;
import com.ohpenl.midoffice.configurationtracker.problem.exception.NotFoundException;
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

    public ConfigurationType createConfigurationType(String name, ConfigurationDataType dataType) {
        configurationTypeValidator.validateUniqueName(name);

        var configurationType = new ConfigurationType(name, dataType);
        return configurationTypeRepository.save(configurationType);
    }

    public ConfigurationType getConfigurationType(Long id) {
        return configurationTypeRepository.findById(id).orElseThrow(() -> new NotFoundException("Configuration type with id " + id + " not found"));
    }

    public List<ConfigurationType> listConfigurationTypes() {
        return configurationTypeRepository.findAll();
    }
}
