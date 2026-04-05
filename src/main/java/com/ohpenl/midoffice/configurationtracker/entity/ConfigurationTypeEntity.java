package com.ohpenl.midoffice.configurationtracker.entity;

import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationDataType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "configuration_type")
@NoArgsConstructor
public class ConfigurationTypeEntity {
    @Id
    private Long id = 0L;

    @Column(unique = true, nullable = false)
    private String name = "";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConfigurationDataType dataType = ConfigurationDataType.STRING;

    public ConfigurationTypeEntity(String name, ConfigurationDataType dataType) {
        this.name = name;
        this.dataType = dataType;
    }
}
