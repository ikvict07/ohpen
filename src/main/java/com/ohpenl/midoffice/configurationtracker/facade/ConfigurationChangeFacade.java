package com.ohpenl.midoffice.configurationtracker.facade;

import com.ohpenl.midoffice.configurationtracker.api.model.Action;
import com.ohpenl.midoffice.configurationtracker.api.model.ConfigurationChangeResponse;
import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationType;
import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationChangeEntity;
import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationTypeEntity;
import com.ohpenl.midoffice.configurationtracker.mapper.ConfigurationChangeMapper;
import com.ohpenl.midoffice.configurationtracker.mapper.DataTypeMapper;
import com.ohpenl.midoffice.configurationtracker.service.ConfigurationChangeService;
import com.ohpenl.midoffice.configurationtracker.service.ConfigurationTypeService;
import com.ohpenl.midoffice.configurationtracker.validator.ConfigurationChangeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConfigurationChangeFacade {

    private final ConfigurationChangeService configurationChangeService;
    private final ConfigurationTypeService configurationTypeService;
    private final ConfigurationChangeValidator configurationChangeValidator;

    @Transactional
    public ConfigurationChangeResponse applyConfigurationChange(Action action, String newValue, Long configTypeId, Long version) {
        ConfigurationTypeEntity typeEntity = configurationTypeService.getById(configTypeId);
        ConfigurationType configurationType = DataTypeMapper.toDomain(typeEntity);

        if (action != Action.REMOVE) {
            configurationChangeValidator.validateDataTypes(newValue, configurationType);
        }

        ConfigurationChange domain = switch (action) {
            case ADD -> configurationChangeService.addConfiguration(newValue, configurationType);
            case UPDATE -> configurationChangeService.changeConfiguration(newValue, configurationType);
            case REMOVE -> configurationChangeService.removeConfiguration(configurationType);
        };

        ConfigurationChangeEntity entity = configurationChangeService.save(domain, version);

        domain.setId(entity.getId());

        return ConfigurationChangeMapper.toApi(domain, entity.getVersion());
    }
}
