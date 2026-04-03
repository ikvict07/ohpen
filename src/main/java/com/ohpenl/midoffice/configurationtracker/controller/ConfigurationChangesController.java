package com.ohpenl.midoffice.configurationtracker.controller;

import com.ohpenl.midoffice.configurationtracker.api.ConfigurationChangesApi;
import com.ohpenl.midoffice.configurationtracker.api.model.ConfigurationChangeResponse;
import com.ohpenl.midoffice.configurationtracker.api.model.CreateConfigurationChangeRequest;
import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/configuration-changes")
public class ConfigurationChangesController implements ConfigurationChangesApi {
    @Override
    public ResponseEntity<ConfigurationChangeResponse> createConfigurationChange(CreateConfigurationChangeRequest createConfigurationChangeRequest) {
        return null;
    }

    @Override
    public ResponseEntity<ConfigurationChangeResponse> getConfigurationChange(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<List<ConfigurationChangeResponse>> listConfigurationChanges(@Nullable String configTypeName) {
        return null;
    }
}
