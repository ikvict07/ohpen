package com.ohpenl.midoffice.configurationtracker.entity;

import jakarta.persistence.Column;
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
    private Long id = 0L;

    @ManyToOne
    private ConfigurationType configType;

    @Column(nullable = false)
    private String previousValue = "";
    @Column(nullable = false)
    private String newValue = "";

    @CreationTimestamp
    private LocalDateTime timestamp = LocalDateTime.now();
}
