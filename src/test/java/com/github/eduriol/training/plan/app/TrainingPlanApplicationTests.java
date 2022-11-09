package com.github.eduriol.training.plan.app;

import com.github.eduriol.training.plan.app.models.HealthStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TrainingPlanApplicationTests extends AbstractTest {

    @Override
    @BeforeAll
    public void setUp() {
        super.setUp();
    }

    @Test
    void getAppHealthStatus() throws Exception {
        String uri = "/api/health";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        HealthStatus healthStatus = super.mapFromJson(content, HealthStatus.class);
        assertTrue(healthStatus.isHealthy());
    }

}
