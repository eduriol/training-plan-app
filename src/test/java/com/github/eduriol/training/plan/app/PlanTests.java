package com.github.eduriol.training.plan.app;

import com.github.eduriol.training.plan.app.models.domain.Plan;
import com.github.eduriol.training.plan.app.models.domain.Topic;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.time.temporal.ChronoUnit;
import java.util.List;

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
    void createPlanWithTopicsAndLetPlanEmpty() throws Exception {
        String uri = "/api/plans";
        Plan plan = new Plan();
        plan.setName("Backend developer");
        Topic testTopic = new Topic();
        testTopic.setName("Java");
        Topic secondTestTopic = new Topic();
        secondTestTopic.setName("Docker");
        plan.setTopics(List.of(testTopic, secondTestTopic));
        String body = super.mapToJson(plan);

        MvcResult result = mvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").isNumber(),
                        jsonPath("$.createdAt").value(matchesPattern(zonedDateTimeRegex)),
                        jsonPath("$.name").value(plan.getName()),
                        // Even if a topics array is provided, the Plan creation endpoint will ignore topics:
                        jsonPath("$.topics").value(iterableWithSize(0))
                )
                .andReturn();

    }

    @Test
    void createPlanWithUnexpectedIdsInTopics() throws Exception {
        String uri = "/api/plans";
        Plan plan = new Plan();
        plan.setName("Backend developer");
        Topic testTopic = new Topic();
        testTopic.setId(1L);
        testTopic.setName("Java");
        Topic secondTestTopic = new Topic();
        secondTestTopic.setName("Docker");
        secondTestTopic.setId(2L);
        plan.setTopics(List.of(testTopic, secondTestTopic));
        String body = super.mapToJson(plan);

        MvcResult result = mvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").isNumber(),
                        jsonPath("$.createdAt").value(matchesPattern(zonedDateTimeRegex)),
                        jsonPath("$.name").value(plan.getName()),
                        // Even if a topics array is provided, the Plan creation endpoint will ignore topics:
                        jsonPath("$.topics").value(iterableWithSize(0))
                )
                .andReturn();

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

    @Test
    public void getPlanWithoutTopics() throws Exception {
        Plan testPlan = createTestPlan("Backend developer");
        String uri = "/api/plans/" + testPlan.getId();

        mvc.perform(get(uri))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").value(testPlan.getId()),
                        jsonPath("$.createdAt").value(matchesPattern(zonedDateTimeRegex)),
                        jsonPath("$.name").value("Backend developer"),
                        jsonPath("$.topics").value(iterableWithSize(0))
                );
    }

    @Test
    public void getPlanWithTopics() throws Exception {
        Plan testPlan = createTestPlan("Backend developer");
        createTestTopic("Java", testPlan);
        createTestTopic("Docker", testPlan);
        String uri = "/api/plans/" + testPlan.getId();

        mvc.perform(get(uri))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").value(testPlan.getId()),
                        jsonPath("$.createdAt").value(matchesPattern(zonedDateTimeRegex)),
                        jsonPath("$.name").value("Backend developer"),
                        jsonPath("$.topics").value(iterableWithSize(2))
                );
    }

    @Test
    public void getUnknownPlan() throws Exception {
        String uri = "/api/plans/0";
        mvc.perform(get(uri))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.errors").value(iterableWithSize(1))
                );
    }

    @Test
    public void getPlansList() throws Exception {
        createTestPlan("Backend developer");
        createTestPlan("Frontend developer");
        String uri = "/api/plans";

        mvc.perform(get(uri))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$").value(iterableWithSize(2))
                );
    }

    @Test
    public void getEmptyPlansList() throws Exception {
        String uri = "/api/plans";

        mvc.perform(get(uri))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$").value(iterableWithSize(0))
                );
    }

    @Test
    public void updatePlanWithTopicsToChangeNameOnly() throws Exception {
        Plan testPlan = createTestPlan("Backend developer");
        String uri = "/api/plans/" + testPlan.getId();
        createTestTopic("Java", testPlan);
        createTestTopic("Docker", testPlan);

        Plan updatedPlan = new Plan();
        updatedPlan.setName("Backend senior developer");
        String body = super.mapToJson(updatedPlan);

        mvc.perform(put(uri)
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(testPlan.getId()),
                        jsonPath("$.createdAt", new ZonedDateTimeMatcher(testPlan.getCreatedAt(), 1, ChronoUnit.MILLIS)),
                        jsonPath("$.name").value("Backend senior developer"),
                        jsonPath("$.topics").value(iterableWithSize(2))
                );
    }

    @Test
    public void updatePlan() throws Exception {
        Plan testPlan = createTestPlan("Backend developer");
        String uri = "/api/plans/" + testPlan.getId();

        Plan updatedPlan = new Plan();
        updatedPlan.setName("Backend senior developer");
        String body = super.mapToJson(updatedPlan);

        mvc.perform(put(uri)
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(testPlan.getId()),
                        jsonPath("$.createdAt", new ZonedDateTimeMatcher(testPlan.getCreatedAt(), 1, ChronoUnit.MILLIS)),
                        jsonPath("$.name").value("Backend senior developer"),
                        jsonPath("$.topics").value(iterableWithSize(0))
                );
    }

    @Test
    public void updatePlanWithUnexpectedFields() throws Exception {
        Plan testPlan = createTestPlan("Backend developer");
        String uri = "/api/plans/" + testPlan.getId();

        Plan updatedPlan = new Plan();
        updatedPlan.setName(testPlan.getName());
        Topic testTopic = new Topic();
        testTopic.setId(1L);
        testTopic.setName("Python");
        Topic secondTestTopic = new Topic();
        secondTestTopic.setName("Docker");
        secondTestTopic.setId(2L);
        updatedPlan.setTopics(List.of(testTopic, secondTestTopic));
        String body = super.mapToJson(updatedPlan);

        mvc.perform(put(uri)
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(testPlan.getId()),
                        jsonPath("$.createdAt", new ZonedDateTimeMatcher(testPlan.getCreatedAt(), 1, ChronoUnit.MILLIS)),
                        jsonPath("$.name").value(testPlan.getName()),
                        jsonPath("$.topics").value(iterableWithSize(0))
                );
    }

    @Test
    public void updateUnknownPlan() throws Exception {
        String uri = "/api/plans/0";
        Plan updatedPlan = new Plan();
        updatedPlan.setName("Backend senior developer");
        String body = super.mapToJson(updatedPlan);

        mvc.perform(put(uri)
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.errors").value(iterableWithSize(1))
                );
    }

    @Test
    public void updatePlanWithoutName() throws Exception {
        Plan testPlan = createTestPlan("Backend developer");
        String uri = "/api/plans/" + testPlan.getId();
        Plan updatedPlan = new Plan();
        String body = super.mapToJson(updatedPlan);

        mvc.perform(put(uri)
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.errors").value(iterableWithSize(1))
                );
    }

}
