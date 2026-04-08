package com.ohpenl.midoffice.configurationtracker.service.listener;

import com.ohpenl.midoffice.configurationtracker.domain.AddConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationDataType;
import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationType;
import com.ohpenl.midoffice.configurationtracker.domain.RemoveConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.domain.UpdateConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ConfigurationChangeListenerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private ConfigurationChangeListener listener;

    private final ConfigurationType configType = new ConfigurationType(ConfigurationDataType.STRING, "feature", 1L, 0L);

    @Test
    void onConfigurationChange_shouldNotifyOnAdd() {
        var event = new AddConfigurationChange("val", configType);
        event.setId(1L);

        listener.onConfigurationChange(event);

        then(notificationService).should().notify(event);
    }

    @Test
    void onConfigurationChange_shouldNotifyOnUpdate() {
        var event = new UpdateConfigurationChange("old", "new", configType);
        event.setId(2L);

        listener.onConfigurationChange(event);

        then(notificationService).should().notify(event);
    }

    @Test
    void onConfigurationChange_shouldNotifyOnRemove() {
        var event = new RemoveConfigurationChange("old", configType);
        event.setId(3L);

        listener.onConfigurationChange(event);

        then(notificationService).should().notify(event);
    }
}
