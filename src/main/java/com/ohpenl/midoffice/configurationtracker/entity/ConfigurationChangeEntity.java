package com.ohpenl.midoffice.configurationtracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "configuration_changes")
@NoArgsConstructor
public class ConfigurationChangeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;

    private Long version;

    @ManyToOne(fetch = FetchType.EAGER)
    private ConfigurationTypeEntity configType;

    private String previousValue = null;
    private String newValue = null;

    @CreationTimestamp
    private LocalDateTime timestamp = LocalDateTime.now();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ConfigurationChangeAction action;

    public ConfigurationChangeEntity(ConfigurationTypeEntity configType, String previousValue, String newValue, ConfigurationChangeAction action) {
        this.configType = configType;
        this.previousValue = previousValue;
        this.newValue = newValue;
        this.action = action;
    }
}
