package com.ohpenl.midoffice.configurationtracker.controller;

import com.ohpenl.midoffice.configurationtracker.api.model.ConfigurationTypeResponse;
import com.ohpenl.midoffice.configurationtracker.api.model.CreateConfigurationTypeRequest;
import com.ohpenl.midoffice.configurationtracker.api.model.DataType;
import com.ohpenl.midoffice.configurationtracker.client.NotificationClient;
import com.ohpenl.midoffice.configurationtracker.infra.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ConfigurationTypesControllerIT extends BaseIntegrationTest {

    @MockitoBean
    private NotificationClient notificationClient;

    @Test
    void shouldCreateConfigurationType() throws Exception {
        var request = new CreateConfigurationTypeRequest().name("feature-flag").dataType(DataType.BOOLEAN);

        mockMvc.perform(post("/configuration-types")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("feature-flag")))
                .andExpect(jsonPath("$.dataType", is("BOOLEAN")))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    void shouldReturnConflict_whenDuplicateName() throws Exception {
        var request = new CreateConfigurationTypeRequest().name("timeout").dataType(DataType.NUMBER);

        performPost("/configuration-types", request, ConfigurationTypeResponse.class);

        mockMvc.perform(post("/configuration-types")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldGetConfigurationTypeById() throws Exception {
        var request = new CreateConfigurationTypeRequest().name("max-retries").dataType(DataType.NUMBER);
        var created = performPost("/configuration-types", request, ConfigurationTypeResponse.class);

        mockMvc.perform(get("/configuration-types/{id}", created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("max-retries")))
                .andExpect(jsonPath("$.dataType", is("NUMBER")));
    }

    @Test
    void shouldListConfigurationTypes() throws Exception {
        performPost("/configuration-types", new CreateConfigurationTypeRequest().name("type-a").dataType(DataType.STRING), ConfigurationTypeResponse.class);
        performPost("/configuration-types", new CreateConfigurationTypeRequest().name("type-b").dataType(DataType.BOOLEAN), ConfigurationTypeResponse.class);

        mockMvc.perform(get("/configuration-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void shouldReturn4xx_whenConfigTypeNotFound() throws Exception {
        mockMvc.perform(get("/configuration-types/{id}", 99999))
                .andExpect(status().is4xxClientError());
    }
}
