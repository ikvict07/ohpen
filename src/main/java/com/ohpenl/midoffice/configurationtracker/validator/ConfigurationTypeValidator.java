package com.ohpenl.midoffice.configurationtracker.validator;

import com.ohpenl.midoffice.configurationtracker.problem.exception.AlreadyExistsException;
import com.ohpenl.midoffice.configurationtracker.repository.ConfigurationTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public final class ConfigurationTypeValidator {

    private final ConfigurationTypeRepository configurationTypeRepository;

    public void validateUniqueName(String name) {
        if (configurationTypeRepository.existsByName(name)) {
            throw new AlreadyExistsException("Configuration type with name " + name + " already exists");
        }
    }
}
