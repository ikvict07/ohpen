package com.ohpenl.midoffice.configurationtracker.controller;

import com.ohpenl.midoffice.configurationtracker.api.ConfigurationTypesApi;
import com.ohpenl.midoffice.configurationtracker.api.model.ConfigurationTypeResponse;
import com.ohpenl.midoffice.configurationtracker.api.model.CreateConfigurationTypeRequest;
import com.ohpenl.midoffice.configurationtracker.mapper.DataTypeMapper;
import com.ohpenl.midoffice.configurationtracker.service.ConfigurationTypeService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/configuration-types")
@RequiredArgsConstructor
public class ConfigurationTypesController implements ConfigurationTypesApi {

    private final ConfigurationTypeService configurationTypeService;

    @Override
    public ResponseEntity<ConfigurationTypeResponse> createConfigurationType(@NotNull CreateConfigurationTypeRequest createConfigurationTypeRequest) {
        var domainDataType = DataTypeMapper.toDomain(createConfigurationTypeRequest.getDataType());
        var result = configurationTypeService.createConfigurationType(createConfigurationTypeRequest.getName(), domainDataType);
        return ResponseEntity.ok()
                .body(DataTypeMapper.toApi(result));
    }

    @Override
    public ResponseEntity<ConfigurationTypeResponse> getConfigurationType(@NotNull Long id) {
        var result = configurationTypeService.getConfigurationType(id);
        return ResponseEntity.ok()
                .body(DataTypeMapper.toApi(result));
    }

    @Override
    public ResponseEntity<List<ConfigurationTypeResponse>> listConfigurationTypes() {
        return ResponseEntity.ok()
                .body(configurationTypeService.listConfigurationTypes().stream()
                        .map(DataTypeMapper::toApi)
                        .toList());
    }
}
