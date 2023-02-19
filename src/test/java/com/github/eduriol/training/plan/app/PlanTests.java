package com.github.eduriol.training.plan.app;

import com.github.eduriol.training.plan.app.models.domain.Plan;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PlanTests extends AbstractTests {

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
                        jsonPath("$.name").value(plan.getName()),
                        jsonPath("$.topics").value(iterableWithSize(0))
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

    @Test
    void deletePlanWithTopics() throws Exception {
        Plan testPlan = createTestPlan("Backend developer");
        createTestTopic("Java", testPlan);
        createTestTopic("Docker", testPlan);

        assertThat(topicDao.findAllByPlan(testPlan), Matchers.iterableWithSize(2));

        String uri = "/api/plans/" + testPlan.getId();

        mvc.perform(delete(uri)).andExpect(status().isNoContent());

        mvc.perform(get(uri)).andExpect(status().isNotFound());

        mvc.perform(get(uri + "/topics")).andExpect(status().isNotFound());

        assertThat(topicDao.findAllByPlan(testPlan), Matchers.emptyIterable());

    }

    @Test
    void deleteUnknownPlan() throws Exception {
        String uri = "/api/plans/0";

        mvc.perform(delete(uri))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.errors").value(iterableWithSize(1))
                );
    }

}
