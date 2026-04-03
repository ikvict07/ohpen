package com.ohpenl.midoffice.configurationtracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
public class ConfigurationChange {
    @Id
    private Long id = 0L;

    @ManyToOne(fetch = FetchType.EAGER)
    private ConfigurationType configType;

    private String previousValue = null;
    @Column(nullable = false)
    private String newValue = "";

    @CreationTimestamp
    private LocalDateTime timestamp = LocalDateTime.now();

    public ConfigurationChange(ConfigurationType configType, String previousValue, String newValue) {
        this.configType = configType;
        this.previousValue = previousValue;
        this.newValue = newValue;
    }
}
