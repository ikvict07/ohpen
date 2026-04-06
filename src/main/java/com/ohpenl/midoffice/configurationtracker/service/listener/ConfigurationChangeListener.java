package com.ohpenl.midoffice.configurationtracker.service.listener;

import com.ohpenl.midoffice.configurationtracker.domain.AddConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.domain.RemoveConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.domain.UpdateConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConfigurationChangeListener {

    private final NotificationService notificationService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onConfigurationChange(ConfigurationChange event) {
        switch (event) {
            case AddConfigurationChange add -> log.info("Configuration ADDED: id={}, type={}, newValue={}",
                    add.getId(), add.getConfigType().name(), add.getNewValue());
            case UpdateConfigurationChange update -> log.info("Configuration UPDATED: id={}, type={}, oldValue={}, newValue={}",
                    update.getId(), update.getConfigType().name(), update.getPreviousValue(), update.getNewValue());
            case RemoveConfigurationChange remove -> log.info("Configuration REMOVED: id={}, type={}, oldValue={}",
                    remove.getId(), remove.getConfigType().name(), remove.getPreviousValue());
        }

        notificationService.notify(event);
    }
}
