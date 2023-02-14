package com.github.eduriol.training.plan.app;

import com.github.eduriol.training.plan.app.dao.IPlanDao;
import com.github.eduriol.training.plan.app.models.domain.Plan;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.iterableWithSize;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PlanTests extends AbstractTests {

    @Autowired
    private IPlanDao planDao;

    @Override
    @BeforeAll
    public void setUp() {
        super.setUp();
    }

    @BeforeEach
    public void resetDb() {
        planDao.deleteAll();
    }

    @Test
    void createPlan() throws Exception {
        String uri = "/api/plans";
        Plan plan = new Plan();
        plan.setName("Become developer");
        String body = super.mapToJson(plan);

        mvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").isNumber(),
                        jsonPath("$.createdAt").value(matchesPattern(zonedDateTimeRegex)),
                        jsonPath("$.name").value(plan.getName())
                );
    }

    @Test
    void createPlanWithoutName() throws Exception {
        String uri = "/api/plans";
        Plan plan = new Plan();
        String body = super.mapToJson(plan);

        mvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.errors").value(iterableWithSize(1))
                );
    }

}
