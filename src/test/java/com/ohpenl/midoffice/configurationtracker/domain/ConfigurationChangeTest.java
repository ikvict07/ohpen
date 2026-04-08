package com.ohpenl.midoffice.configurationtracker.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigurationChangeTest {

    private final ConfigurationType configType = new ConfigurationType(ConfigurationDataType.STRING, "feature", 1L, 0L);

    @Test
    void addChange_update_shouldReturnUpdateWithCorrectValues() {
        var add = new AddConfigurationChange("initial", configType);

        var update = add.update("updated");

        assertThat(update).isInstanceOf(UpdateConfigurationChange.class);
        assertThat(update.getPreviousValue()).isEqualTo("initial");
        assertThat(update.getNewValue()).isEqualTo("updated");
    }

    @Test
    void addChange_remove_shouldReturnRemove() {
        var add = new AddConfigurationChange("val", configType);

        var remove = add.remove();

        assertThat(remove).isInstanceOf(RemoveConfigurationChange.class);
    }

    @Test
    void updateChange_update_shouldChainUpdates() {
        var update = new UpdateConfigurationChange("old", "current", configType);

        var next = update.update("newest");

        assertThat(next.getPreviousValue()).isEqualTo("current");
        assertThat(next.getNewValue()).isEqualTo("newest");
    }

    @Test
    void updateChange_remove_shouldReturnRemoveWithCurrentValue() {
        var update = new UpdateConfigurationChange("old", "current", configType);

        var remove = update.remove();

        assertThat(remove).isInstanceOf(RemoveConfigurationChange.class);
        assertThat(remove.getPreviousValue()).isEqualTo("current");
    }

    @Test
    void removeChange_recreate_shouldReturnAdd() {
        var remove = new RemoveConfigurationChange("old", configType);

        var add = remove.recreate("new", configType);

        assertThat(add).isInstanceOf(AddConfigurationChange.class);
        assertThat(add.getNewValue()).isEqualTo("new");
    }
}
