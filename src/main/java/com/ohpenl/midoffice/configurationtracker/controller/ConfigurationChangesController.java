package com.ohpenl.midoffice.configurationtracker.controller;

import com.ohpenl.midoffice.configurationtracker.api.ConfigurationChangesApi;
import com.ohpenl.midoffice.configurationtracker.api.model.ConfigurationChangePage;
import com.ohpenl.midoffice.configurationtracker.api.model.ConfigurationChangeResponse;
import com.ohpenl.midoffice.configurationtracker.api.model.CreateConfigurationChangeRequest;
import com.ohpenl.midoffice.configurationtracker.mapper.ConfigurationChangeMapper;
import com.ohpenl.midoffice.configurationtracker.service.ConfigurationChangesService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/configuration-changes")
@RequiredArgsConstructor
@Validated
public class ConfigurationChangesController implements ConfigurationChangesApi {

    private final ConfigurationChangesService configurationChangesService;

    @Override
    public ResponseEntity<ConfigurationChangeResponse> createConfigurationChange(@NotNull CreateConfigurationChangeRequest createConfigurationChangeRequest) {
        var result = configurationChangesService.createConfigurationChange(
                createConfigurationChangeRequest.getConfigTypeId(),
                createConfigurationChangeRequest.getPreviousValue(),
                createConfigurationChangeRequest.getNewValue()
        );
        return ResponseEntity.ok(ConfigurationChangeMapper.toApi(result));
    }

    @Override
    public ResponseEntity<ConfigurationChangeResponse> getConfigurationChange(@NotNull Long id) {
        var found = configurationChangesService.getConfigurationChange(id);
        return ResponseEntity.ok(ConfigurationChangeMapper.toApi(found));
    }

    @Override
    public ResponseEntity<ConfigurationChangePage> listConfigurationChanges(String configTypeName, Integer page, Integer size, String sort) {
        var resultPage = configurationChangesService.listConfigurationChanges(PageRequest.of(page, size), configTypeName);
        return ResponseEntity.ok(ConfigurationChangeMapper.toApiPage(resultPage));
    }
}
