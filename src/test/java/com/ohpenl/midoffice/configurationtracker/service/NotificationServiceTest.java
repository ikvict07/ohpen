package com.ohpenl.midoffice.configurationtracker.service;

import com.ohpenl.midoffice.configurationtracker.client.NotificationClient;
import com.ohpenl.midoffice.configurationtracker.client.NotificationRequest;
import com.ohpenl.midoffice.configurationtracker.domain.AddConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationDataType;
import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationType;
import com.ohpenl.midoffice.configurationtracker.domain.RemoveConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.domain.UpdateConfigurationChange;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationClient notificationClient;

    @InjectMocks
    private NotificationService notificationService;

    private final ConfigurationType configType = new ConfigurationType(ConfigurationDataType.STRING, "feature", 1L, 0L);

    @Test
    void notify_shouldSendAddNotification() {
        var change = new AddConfigurationChange("val", configType);
        change.setId(5L);

        notificationService.notify(change);

        var captor = ArgumentCaptor.forClass(NotificationRequest.class);
        then(notificationClient).should().sendNotification(captor.capture());
        assertThat(captor.getValue().action()).isEqualTo("ADD");
        assertThat(captor.getValue().id()).isEqualTo(5L);
        assertThat(captor.getValue().type()).isEqualTo("feature");
    }

    @Test
    void notify_shouldSendUpdateNotification() {
        var change = new UpdateConfigurationChange("old", "new", configType);
        change.setId(6L);

        notificationService.notify(change);

        var captor = ArgumentCaptor.forClass(NotificationRequest.class);
        then(notificationClient).should().sendNotification(captor.capture());
        assertThat(captor.getValue().action()).isEqualTo("UPDATE");
    }

    @Test
    void notify_shouldSendRemoveNotification() {
        var change = new RemoveConfigurationChange("old", configType);
        change.setId(7L);

        notificationService.notify(change);

        var captor = ArgumentCaptor.forClass(NotificationRequest.class);
        then(notificationClient).should().sendNotification(captor.capture());
        assertThat(captor.getValue().action()).isEqualTo("REMOVE");
    }
}
