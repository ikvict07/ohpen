package com.ohpenl.midoffice.configurationtracker.controller;

import com.ohpenl.midoffice.configurationtracker.api.ConfigurationChangesApi;
import com.ohpenl.midoffice.configurationtracker.api.model.ApplyConfigurationChangeRequest;
import com.ohpenl.midoffice.configurationtracker.api.model.ConfigurationChangePage;
import com.ohpenl.midoffice.configurationtracker.api.model.ConfigurationChangeResponse;
import com.ohpenl.midoffice.configurationtracker.facade.ConfigurationChangeFacade;
import com.ohpenl.midoffice.configurationtracker.service.ChangeHistoryService;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Validated
public class ConfigurationChangesController implements ConfigurationChangesApi {

    private final ConfigurationChangeFacade configurationChangeFacade;
    private final ChangeHistoryService changeHistoryService;

    @Timed("configuration_changes.apply")
    @Counted("configuration_changes.apply.count")
    @Override
    public ResponseEntity<ConfigurationChangeResponse> applyConfigurationChange(ApplyConfigurationChangeRequest request) {
        ConfigurationChangeResponse result = configurationChangeFacade.applyConfigurationChange(
                request.getAction(),
                request.getNewValue(),
                request.getConfigTypeId(),
                request.getVersion()
        );
        return ResponseEntity.ok(result);
    }

    @Timed("configuration_changes.get")
    @Counted("configuration_changes.get.count")
    @Override
    public ResponseEntity<ConfigurationChangeResponse> getConfigurationChange(Long id) {
        return ResponseEntity.ok(changeHistoryService.getById(id));
    }

    @Timed("configuration_changes.list")
    @Counted("configuration_changes.list.count")
    @Override
    public ResponseEntity<ConfigurationChangePage> listConfigurationChanges(String configTypeName, Integer page, Integer size, OffsetDateTime from, OffsetDateTime to, String sort) {
        return ResponseEntity.ok(changeHistoryService.list(configTypeName, from, to, page, size));
    }
}
