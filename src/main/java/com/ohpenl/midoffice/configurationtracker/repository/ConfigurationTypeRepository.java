package com.ohpenl.midoffice.configurationtracker.repository;

import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationTypeRepository extends JpaRepository<ConfigurationType, Long> {

    boolean existsByName(String name);
}
