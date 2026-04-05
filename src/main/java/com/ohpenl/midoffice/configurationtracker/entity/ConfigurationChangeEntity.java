package com.ohpenl.midoffice.configurationtracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class ConfigurationChangeEntity {
    @Id
    private Long id = 0L;

    @ManyToOne(fetch = FetchType.EAGER)
    private ConfigurationTypeEntity configType;

    private String previousValue = null;
    @Column(nullable = false)
    private String newValue = "";

    @CreationTimestamp
    private LocalDateTime timestamp = LocalDateTime.now();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ConfigurationChangeAction action;
}
