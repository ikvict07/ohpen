package com.ohpenl.midoffice.configurationtracker.controller;

import com.ohpenl.midoffice.configurationtracker.api.ConfigurationChangesApi;
import com.ohpenl.midoffice.configurationtracker.api.model.ConfigurationChangePage;
import com.ohpenl.midoffice.configurationtracker.api.model.ConfigurationChangeResponse;
import com.ohpenl.midoffice.configurationtracker.api.model.CreateConfigurationChangeRequest;
import com.ohpenl.midoffice.configurationtracker.mapper.ConfigurationChangeMapper;
import com.ohpenl.midoffice.configurationtracker.service.ConfigurationChangesService;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
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

    @Timed("configuration_changes.create")
    @Counted("configuration_changes.create.count")
    @Override
    public ResponseEntity<ConfigurationChangeResponse> createConfigurationChange(CreateConfigurationChangeRequest createConfigurationChangeRequest) {
        var result = configurationChangesService.createConfigurationChange(
                createConfigurationChangeRequest.getConfigTypeId(),
                createConfigurationChangeRequest.getPreviousValue(),
                createConfigurationChangeRequest.getNewValue()
        );
        return ResponseEntity.ok(ConfigurationChangeMapper.toApi(result));
    }

    @Timed("configuration_changes.get")
    @Counted("configuration_changes.get.count")
    @Override
    public ResponseEntity<ConfigurationChangeResponse> getConfigurationChange(Long id) {
        var found = configurationChangesService.getConfigurationChange(id);
        return ResponseEntity.ok(ConfigurationChangeMapper.toApi(found));
    }

    @Timed("configuration_changes.list")
    @Counted("configuration_changes.list.count")
    @Override
    public ResponseEntity<ConfigurationChangePage> listConfigurationChanges(String configTypeName, Integer page, Integer size, String sort) {
        var resultPage = configurationChangesService.listConfigurationChanges(PageRequest.of(page, size), configTypeName);
        return ResponseEntity.ok(ConfigurationChangeMapper.toApiPage(resultPage));
    }
}
