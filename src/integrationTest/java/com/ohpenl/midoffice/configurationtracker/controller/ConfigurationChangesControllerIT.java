package com.ohpenl.midoffice.configurationtracker.controller;

import com.ohpenl.midoffice.configurationtracker.api.model.Action;
import com.ohpenl.midoffice.configurationtracker.api.model.ApplyConfigurationChangeRequest;
import com.ohpenl.midoffice.configurationtracker.api.model.ConfigurationChangeResponse;
import com.ohpenl.midoffice.configurationtracker.api.model.ConfigurationTypeResponse;
import com.ohpenl.midoffice.configurationtracker.api.model.CreateConfigurationTypeRequest;
import com.ohpenl.midoffice.configurationtracker.api.model.DataType;
import com.ohpenl.midoffice.configurationtracker.client.NotificationClient;
import com.ohpenl.midoffice.configurationtracker.infra.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ConfigurationChangesControllerIT extends BaseIntegrationTest {

    @MockitoBean
    private NotificationClient notificationClient;

    private Long createConfigType(String name, DataType dataType) throws Exception {
        var request = new CreateConfigurationTypeRequest().name(name).dataType(dataType);
        return performPost("/configuration-types", request, ConfigurationTypeResponse.class).getId();
    }

    private ConfigurationChangeResponse applyChange(Action action, Long typeId, String newValue, Long version) throws Exception {
        var request = new ApplyConfigurationChangeRequest()
                .action(action)
                .configTypeId(typeId)
                .newValue(newValue)
                .version(version);
        return performPost("/configuration-changes", request, ConfigurationChangeResponse.class);
    }

    @Test
    void shouldAddConfiguration() throws Exception {
        Long typeId = createConfigType("feature", DataType.STRING);

        var request = new ApplyConfigurationChangeRequest()
                .action(Action.ADD).configTypeId(typeId).newValue("enabled").version(0L);

        mockMvc.perform(post("/configuration-changes")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.action", is("ADD")))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    void shouldUpdateConfiguration() throws Exception {
        Long typeId = createConfigType("timeout", DataType.NUMBER);
        applyChange(Action.ADD, typeId, "30", 0L);

        var request = new ApplyConfigurationChangeRequest()
                .action(Action.UPDATE).configTypeId(typeId).newValue("60").version(0L);

        mockMvc.perform(post("/configuration-changes")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.action", is("UPDATE")));
    }

    @Test
    void shouldRemoveConfiguration() throws Exception {
        Long typeId = createConfigType("flag", DataType.BOOLEAN);
        applyChange(Action.ADD, typeId, "true", 0L);

        var request = new ApplyConfigurationChangeRequest()
                .action(Action.REMOVE).configTypeId(typeId).version(0L);

        mockMvc.perform(post("/configuration-changes")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.action", is("REMOVE")));
    }

    @Test
    void shouldFailToAddDuplicate() throws Exception {
        Long typeId = createConfigType("unique", DataType.STRING);
        applyChange(Action.ADD, typeId, "val", 0L);

        var request = new ApplyConfigurationChangeRequest()
                .action(Action.ADD).configTypeId(typeId).newValue("val2").version(0L);

        mockMvc.perform(post("/configuration-changes")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailToRemoveNonExistent() throws Exception {
        Long typeId = createConfigType("empty", DataType.STRING);

        var request = new ApplyConfigurationChangeRequest()
                .action(Action.REMOVE).configTypeId(typeId).version(0L);

        mockMvc.perform(post("/configuration-changes")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetConfigurationChangeById() throws Exception {
        Long typeId = createConfigType("get-test", DataType.STRING);
        var added = applyChange(Action.ADD, typeId, "hello", 0L);

        mockMvc.perform(get("/configuration-changes/{id}", added.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(added.getId().intValue())))
                .andExpect(jsonPath("$.action", is("ADD")));
    }

    @Test
    void shouldListConfigurationChanges() throws Exception {
        Long typeId = createConfigType("list-test", DataType.STRING);
        applyChange(Action.ADD, typeId, "v1", 0L);
        applyChange(Action.UPDATE, typeId, "v2", 0L);

        mockMvc.perform(get("/configuration-changes")
                        .param("configTypeName", "list-test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements", is(2)));
    }

    @Test
    void shouldRejectInvalidDataType() throws Exception {
        Long typeId = createConfigType("num-type", DataType.NUMBER);

        var request = new ApplyConfigurationChangeRequest()
                .action(Action.ADD).configTypeId(typeId).newValue("not-a-number").version(0L);

        mockMvc.perform(post("/configuration-changes")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldSupportFullLifecycle() throws Exception {
        Long typeId = createConfigType("lifecycle", DataType.STRING);

        applyChange(Action.ADD, typeId, "initial", 0L);
        applyChange(Action.UPDATE, typeId, "updated", 0L);
        applyChange(Action.REMOVE, typeId, null, 1L);

        var request = new ApplyConfigurationChangeRequest()
                .action(Action.ADD).configTypeId(typeId).newValue("recreated").version(2L);

        mockMvc.perform(post("/configuration-changes")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.action", is("ADD")));
    }
}
