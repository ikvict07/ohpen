package com.ohpenl.midoffice.configurationtracker.mapper;

import com.ohpenl.midoffice.configurationtracker.api.model.Action;
import com.ohpenl.midoffice.configurationtracker.domain.AddConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationDataType;
import com.ohpenl.midoffice.configurationtracker.domain.RemoveConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.domain.UpdateConfigurationChange;
import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationChangeAction;
import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationChangeEntity;
import com.ohpenl.midoffice.configurationtracker.entity.ConfigurationTypeEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigurationChangeMapperTest {

    private final ConfigurationTypeEntity typeEntity = createTypeEntity();

    private static ConfigurationTypeEntity createTypeEntity() {
        var e = new ConfigurationTypeEntity("feature", ConfigurationDataType.STRING);
        e.setId(1L);
        return e;
    }

    @Test
    void fromEntity_shouldMapAddAction() {
        var entity = new ConfigurationChangeEntity(typeEntity, null, "val", ConfigurationChangeAction.ADD);
        entity.setId(1L);

        var result = ConfigurationChangeMapper.fromEntity(entity);

        assertThat(result).isInstanceOf(AddConfigurationChange.class);
        assertThat(result.getNewValue()).isEqualTo("val");
        assertThat(result.getPreviousValue()).isNull();
    }

    @Test
    void fromEntity_shouldMapUpdateAction() {
        var entity = new ConfigurationChangeEntity(typeEntity, "old", "new", ConfigurationChangeAction.UPDATE);
        entity.setId(2L);

        var result = ConfigurationChangeMapper.fromEntity(entity);

        assertThat(result).isInstanceOf(UpdateConfigurationChange.class);
        assertThat(result.getPreviousValue()).isEqualTo("old");
        assertThat(result.getNewValue()).isEqualTo("new");
    }

    @Test
    void fromEntity_shouldMapRemoveAction() {
        var entity = new ConfigurationChangeEntity(typeEntity, "old", null, ConfigurationChangeAction.REMOVE);
        entity.setId(3L);

        var result = ConfigurationChangeMapper.fromEntity(entity);

        assertThat(result).isInstanceOf(RemoveConfigurationChange.class);
        assertThat(result.getPreviousValue()).isEqualTo("old");
    }

    @Test
    void toApi_shouldMapAddChange() {
        var configType = DataTypeMapper.toDomain(typeEntity);
        var change = new AddConfigurationChange("val", configType);
        change.setId(1L);

        var result = ConfigurationChangeMapper.toApi(change, 0L);

        assertThat(result.getAction()).isEqualTo(Action.ADD);
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void toApi_shouldMapUpdateChange() {
        var configType = DataTypeMapper.toDomain(typeEntity);
        var change = new UpdateConfigurationChange("old", "new", configType);
        change.setId(2L);

        var result = ConfigurationChangeMapper.toApi(change, 1L);

        assertThat(result.getAction()).isEqualTo(Action.UPDATE);
        assertThat(result.getVersion()).isEqualTo(1L);
    }

    @Test
    void toApi_shouldMapRemoveChange() {
        var configType = DataTypeMapper.toDomain(typeEntity);
        var change = new RemoveConfigurationChange("old", configType);
        change.setId(3L);

        var result = ConfigurationChangeMapper.toApi(change, 2L);

        assertThat(result.getAction()).isEqualTo(Action.REMOVE);
    }
}
