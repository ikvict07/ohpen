package com.ohpenl.midoffice.configurationtracker.controller;

import com.ohpenl.midoffice.configurationtracker.api.ConfigurationChangesApi;
import com.ohpenl.midoffice.configurationtracker.api.model.ApplyConfigurationChangeRequest;
import com.ohpenl.midoffice.configurationtracker.api.model.ConfigurationChangePage;
import com.ohpenl.midoffice.configurationtracker.api.model.ConfigurationChangeResponse;
import com.ohpenl.midoffice.configurationtracker.facade.ConfigurationChangeFacade;
import com.ohpenl.midoffice.configurationtracker.mapper.ConfigurationChangeMapper;
import com.ohpenl.midoffice.configurationtracker.repository.ConfigurationChangeRepository;
import com.ohpenl.midoffice.configurationtracker.problem.exception.NotFoundException;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Validated
public class ConfigurationChangesController implements ConfigurationChangesApi {

    private final ConfigurationChangeFacade configurationChangeFacade;
    private final ConfigurationChangeRepository configurationChangeRepository;

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
        var found = configurationChangeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Configuration change not found"));
        return ResponseEntity.ok(ConfigurationChangeMapper.toApi(ConfigurationChangeMapper.fromEntity(found), found.getVersion()));
    }

    @Timed("configuration_changes.list")
    @Counted("configuration_changes.list.count")
    @Override
    public ResponseEntity<ConfigurationChangePage> listConfigurationChanges(String configTypeName, Integer page, Integer size, String sort) {
        var pageable = PageRequest.of(page, size);
        var resultPage = configTypeName != null 
                ? configurationChangeRepository.findAllByConfigType_Name(configTypeName, pageable)
                : configurationChangeRepository.findAll(pageable);
        return ResponseEntity.ok(ConfigurationChangeMapper.toApiPage(resultPage));
    }
}
