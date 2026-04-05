package com.ohpenl.midoffice.configurationtracker.facade;

import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationType;
import com.ohpenl.midoffice.configurationtracker.domain.UpdateConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.service.ConfigurationChangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfigurationChangeFacade {

    private final ConfigurationChangeService configurationChangeService;

    public <T> UpdateConfigurationChange<T> changeConfiguration(T newValue, ConfigurationType configType) {
        return configurationChangeService.changeConfiguration(newValue, configType);
    }

    public void removeConfiguration(ConfigurationType configType) {
        configurationChangeService.removeConfiguration(configType);
    }

    public <T> void addConfiguration(T newValue, ConfigurationType configType) {
        configurationChangeService.addConfiguration(newValue, configType);
    }
}
