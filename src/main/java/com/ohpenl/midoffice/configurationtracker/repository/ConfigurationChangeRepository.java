package com.ohpenl.midoffice.configurationtracker.repository;

import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationChange;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.util.Optional;

public interface ConfigurationChangeRepository extends JpaRepository<ConfigurationChange, Long> {

    Page<ConfigurationChange> findAllByConfigType_Name(String configTypeName, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM ConfigurationChange c WHERE c.configType.name = :configTypeName ORDER BY c.timestamp DESC LIMIT 1")
    Optional<ConfigurationChange> findLatestByConfigType(@Param("configTypeName") String configTypeName);
}
