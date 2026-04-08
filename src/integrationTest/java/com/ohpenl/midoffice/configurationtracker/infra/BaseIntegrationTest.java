package com.ohpenl.midoffice.configurationtracker.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohpenl.midoffice.configurationtracker.ConfigurationTrackerApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = ConfigurationTrackerApplication.class
)
@AutoConfigureMockMvc
@ActiveProfiles("integrationTest")
@Sql(statements = {
        "DELETE FROM configuration_changes",
        "DELETE FROM configuration_type"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public abstract class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected <T> T performPost(String url, Object request, Class<T> responseType) throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn();
        return objectMapper.readValue(result.getResponse().getContentAsString(), responseType);
    }
}
