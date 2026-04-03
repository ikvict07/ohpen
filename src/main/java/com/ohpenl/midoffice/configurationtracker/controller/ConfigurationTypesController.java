package com.ohpenl.midoffice.configurationtracker.controller;

import com.ohpenl.midoffice.configurationtracker.api.ConfigurationTypesApi;
import com.ohpenl.midoffice.configurationtracker.api.model.ConfigurationTypeResponse;
import com.ohpenl.midoffice.configurationtracker.api.model.CreateConfigurationTypeRequest;
import com.ohpenl.midoffice.configurationtracker.mapper.DataTypeMapper;
import com.ohpenl.midoffice.configurationtracker.service.ConfigurationTypeService;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/configuration-types")
@RequiredArgsConstructor
@Validated
public class ConfigurationTypesController implements ConfigurationTypesApi {

    private final ConfigurationTypeService configurationTypeService;

    @Timed("configuration_types.create")
    @Counted("configuration_types.create.count")
    @Override
    public ResponseEntity<ConfigurationTypeResponse> createConfigurationType(CreateConfigurationTypeRequest createConfigurationTypeRequest) {
        var domainDataType = DataTypeMapper.toDomain(createConfigurationTypeRequest.getDataType());
        var result = configurationTypeService.createConfigurationType(createConfigurationTypeRequest.getName(), domainDataType);
        return ResponseEntity.ok()
                .body(DataTypeMapper.toApi(result));
    }

    @Timed("configuration_types.get")
    @Counted("configuration_types.get.count")
    @Override
    public ResponseEntity<ConfigurationTypeResponse> getConfigurationType(Long id) {
        var result = configurationTypeService.getConfigurationType(id);
        return ResponseEntity.ok()
                .body(DataTypeMapper.toApi(result));
    }

    @Timed("configuration_types.list")
    @Counted("configuration_types.list.count")
    @Override
    public ResponseEntity<List<ConfigurationTypeResponse>> listConfigurationTypes() {
        return ResponseEntity.ok()
                .body(configurationTypeService.listConfigurationTypes().stream()
                        .map(DataTypeMapper::toApi)
                        .toList());
    }
}
