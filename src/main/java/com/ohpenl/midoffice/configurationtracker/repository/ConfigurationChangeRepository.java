package com.ohpenl.midoffice.configurationtracker.repository;

import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationChange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationChangeRepository extends JpaRepository<ConfigurationChange, Long> {

}
