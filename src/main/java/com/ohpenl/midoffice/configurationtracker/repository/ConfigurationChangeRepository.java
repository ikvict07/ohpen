package com.ohpenl.midoffice.configurationtracker.repository;

import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationChangeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ConfigurationChangeRepository extends JpaRepository<ConfigurationChangeEntity, Long> {

    Page<ConfigurationChangeEntity> findAllByConfigType_Name(String configTypeName, Pageable pageable);

    Page<ConfigurationChangeEntity> findAllByTimestampBetween(LocalDateTime from, LocalDateTime to, Pageable pageable);

    Page<ConfigurationChangeEntity> findAllByConfigType_NameAndTimestampBetween(String configTypeName, LocalDateTime from, LocalDateTime to, Pageable pageable);

    @Query("SELECT c FROM ConfigurationChangeEntity c WHERE c.configType.name = :configTypeName ORDER BY c.timestamp DESC LIMIT 1")
    Optional<ConfigurationChangeEntity> findLatestByConfigType(@Param("configTypeName") String configTypeName);
}
