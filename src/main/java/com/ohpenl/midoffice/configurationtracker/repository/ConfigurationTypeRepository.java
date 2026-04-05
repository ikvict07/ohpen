package com.ohpenl.midoffice.configurationtracker.repository;

import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationTypeRepository extends JpaRepository<ConfigurationTypeEntity, Long> {

    boolean existsByName(String name);
}
