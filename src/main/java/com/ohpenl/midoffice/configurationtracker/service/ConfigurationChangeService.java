package com.ohpenl.midoffice.configurationtracker.service;

import com.ohpenl.midoffice.configurationtracker.domain.AddConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationType;
import com.ohpenl.midoffice.configurationtracker.domain.RemoveConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.domain.UpdateConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationChangeAction;
import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationChangeEntity;
import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationTypeEntity;
import com.ohpenl.midoffice.configurationtracker.problem.exception.IllegalTransitionException;
import com.ohpenl.midoffice.configurationtracker.repository.ConfigurationChangeRepository;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ConfigurationChangeService {

    private final ChangeHistoryService changeHistoryService;
    private final ConfigurationTypeService configurationTypeService;
    private final ConfigurationChangeRepository configurationChangeRepository;

    public AddConfigurationChange addConfiguration(String newValue, ConfigurationType configType) {
        ConfigurationChange latest = changeHistoryService.getLatestByType(configType);

        return switch (latest) {
            case null -> new AddConfigurationChange(newValue, configType);
            case AddConfigurationChange ignored ->
                    throw new IllegalTransitionException("There is already a configuration for this type");
            case RemoveConfigurationChange removeConfigurationChange ->
                    removeConfigurationChange.recreate(newValue, configType);
            case UpdateConfigurationChange ignored ->
                    throw new IllegalTransitionException("There is already a configuration for this type");
        };
    }

    public ConfigurationChangeEntity save(ConfigurationChange change, Long version) {
        ConfigurationChangeAction action = map(change);
        ConfigurationTypeEntity type = configurationTypeService.getById(change.getConfigType().id());
        ConfigurationChangeEntity latestEntity = changeHistoryService.getLatestEntityByType(change.getConfigType());
        
        long nextVersion = 0L;
        if (latestEntity != null) {
            Long latestVersion = latestEntity.getVersion();
            if (!Objects.equals(latestVersion, version)) {
                throw new OptimisticLockException("Version mismatch");
            }
            nextVersion = latestVersion + 1;
        } else {
            if (version != null && version != 0) {
                throw new OptimisticLockException("Version mismatch");
            }
        }
        
        ConfigurationChangeEntity entity = new ConfigurationChangeEntity(
                type,
                change.getPreviousValue(),
                change.getNewValue(),
                action
        );
        entity.setVersion(nextVersion);
        return configurationChangeRepository.save(entity);
    }

    private ConfigurationChangeAction map(ConfigurationChange change) {
        return switch (change) {
            case AddConfigurationChange ignored -> ConfigurationChangeAction.ADD;
            case RemoveConfigurationChange ignored -> ConfigurationChangeAction.REMOVE;
            case UpdateConfigurationChange ignored -> ConfigurationChangeAction.UPDATE;
        };
    }

    public RemoveConfigurationChange removeConfiguration(ConfigurationType configType) {
        ConfigurationChange latest = changeHistoryService.getLatestByType(configType);
        return switch (latest) {
            case null -> throw new IllegalTransitionException("There is no configuration for this type");
            case AddConfigurationChange addConfigurationChange -> addConfigurationChange.remove();
            case RemoveConfigurationChange ignored -> throw new IllegalTransitionException("This type already removed");
            case UpdateConfigurationChange updateConfigurationChange -> updateConfigurationChange.remove();
        };
    }

    public UpdateConfigurationChange changeConfiguration(String newValue, ConfigurationType configType) {
        ConfigurationChange latest = changeHistoryService.getLatestByType(configType);

        return switch (latest) {
            case null -> throw new IllegalTransitionException("There is no configuration for this type");
            case AddConfigurationChange addConfigurationChange -> addConfigurationChange.update(newValue);
            case RemoveConfigurationChange ignored ->
                    throw new IllegalTransitionException("Cannot change removed configuration");
            case UpdateConfigurationChange updateConfigurationChange -> updateConfigurationChange.update(newValue);
        };
    }
}
