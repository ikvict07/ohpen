package com.ohpenl.midoffice.configurationtracker.service;

import com.ohpenl.midoffice.configurationtracker.client.NotificationClient;
import com.ohpenl.midoffice.configurationtracker.client.NotificationRequest;
import com.ohpenl.midoffice.configurationtracker.domain.AddConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.domain.RemoveConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.domain.UpdateConfigurationChange;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationClient notificationClient;

    @Retry(name = "notification", fallbackMethod = "fallback")
    @CircuitBreaker(name = "notification")
    public void notify(ConfigurationChange change) {
        log.info("Sending notification for change: {}", change.getId());

        String action = switch (change) {
            case AddConfigurationChange ignored -> "ADD";
            case UpdateConfigurationChange ignored -> "UPDATE";
            case RemoveConfigurationChange ignored -> "REMOVE";
        };

        NotificationRequest request = new NotificationRequest(
                change.getId(),
                change.getConfigType().name(),
                action
        );

        notificationClient.sendNotification(request);
        log.info("Successfully sent notification for change: {}", change.getId());
    }

    @SuppressWarnings("unused")
    public void fallback(ConfigurationChange change, Throwable t) {
        log.error("Failed to send notification for change: {}. Fallback triggered. Reason: {}", change.getId(), t.getMessage());
    }
}
