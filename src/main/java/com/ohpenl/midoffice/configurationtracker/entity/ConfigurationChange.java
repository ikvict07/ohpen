package com.ohpenl.midoffice.configurationtracker.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "configuration_changes")
public class ConfigurationChange {
    @Id
    private Long id;

    @ManyToOne
    private ConfigurationType configType;

    private String previousValue;
    private String newValue;

    @CreationTimestamp
    private LocalDateTime timestamp;
}
