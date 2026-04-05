package com.ohpenl.midoffice.configurationtracker.service;

import com.ohpenl.midoffice.configurationtracker.domain.AddConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationType;
import com.ohpenl.midoffice.configurationtracker.domain.RemoveConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.domain.UpdateConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.problem.exception.IllegalTransitionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfigurationChangeService {

    private final ChangeHistoryService changeHistoryService;

    public <T> AddConfigurationChange<T> addConfiguration(T newValue, ConfigurationType configType) {
        ConfigurationChange<T> latest = changeHistoryService.getLatestByType(configType);

        return switch (latest) {
            case null -> new AddConfigurationChange<>(newValue, configType);
            case AddConfigurationChange<T> ignored ->
                    throw new IllegalTransitionException("There is already a configuration for this type");
            case RemoveConfigurationChange<T> removeConfigurationChange ->
                    removeConfigurationChange.recreate(newValue, configType);
            case UpdateConfigurationChange<T> ignored ->
                    throw new IllegalTransitionException("There is already a configuration for this type");
        };
    }

    public <T> RemoveConfigurationChange<T> removeConfiguration(ConfigurationType configType) {
        ConfigurationChange<T> latest = changeHistoryService.getLatestByType(configType);
        return switch (latest) {
            case null -> throw new IllegalTransitionException("There is no configuration for this type");
            case AddConfigurationChange<T> addConfigurationChange -> addConfigurationChange.remove();
            case RemoveConfigurationChange<T> ignored ->
                    throw new IllegalTransitionException("This type already removed");
            case UpdateConfigurationChange<T> updateConfigurationChange -> updateConfigurationChange.remove();
        };
    }

    public <T> UpdateConfigurationChange<T> changeConfiguration(T newValue, ConfigurationType configType) {
        ConfigurationChange<T> latest = changeHistoryService.getLatestByType(configType);

        return switch (latest) {
            case null -> throw new IllegalTransitionException("There is no configuration for this type");
            case AddConfigurationChange<T> addConfigurationChange -> addConfigurationChange.update(newValue);
            case RemoveConfigurationChange<T> ignored ->
                    throw new IllegalTransitionException("Cannot change removed configuration");
            case UpdateConfigurationChange<T> updateConfigurationChange -> updateConfigurationChange.update(newValue);
        };
    }
}
