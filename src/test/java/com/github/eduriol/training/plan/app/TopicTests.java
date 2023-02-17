package com.github.eduriol.training.plan.app;

import com.github.eduriol.training.plan.app.dao.IPlanDao;
import com.github.eduriol.training.plan.app.dao.ITopicDao;
import com.github.eduriol.training.plan.app.models.domain.Plan;
import com.github.eduriol.training.plan.app.models.domain.Topic;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.temporal.ChronoUnit;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TopicTests extends AbstractTests {

    @Autowired
    private ITopicDao topicDao;

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
    void createTopic() throws Exception {
        Plan testPlan = createTestPlan("Backend developer");
        String uri = "/api/plans/" + testPlan.getId() + "/topics";
        Topic topic = new Topic();
        topic.setName("Java");
        String body = super.mapToJson(topic);

        mvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").isNumber(),
                        jsonPath("$.createdAt").value(matchesPattern(zonedDateTimeRegex)),
                        jsonPath("$.name").value(topic.getName())
                );
    }

    @Test
    void createTopicWithoutName() throws Exception {
        Plan testPlan = createTestPlan("Backend developer");
        String uri = "/api/plans/" + testPlan.getId() + "/topics";
        Topic topic = new Topic();
        String body = super.mapToJson(topic);

        mvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.errors").value(iterableWithSize(1))
                );
    }

    @Test
    void createTopicForUnknownPlan() throws Exception {
        String uri = "/api/plans/0/topics";
        Topic topic = new Topic();
        String body = super.mapToJson(topic);

        mvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.errors").value(iterableWithSize(1))
                );
    }

    @Test
    public void getTopicsListFromMyPlanOnly() throws Exception {
        Plan testPlan = createTestPlan("Backend developer");
        String uri = "/api/plans/" + testPlan.getId() + "/topics";
        createTestTopic("Java", testPlan);
        createTestTopic("Kubernetes", testPlan);

        Plan secondTestPlan = createTestPlan("Frontend developer");
        createTestTopic("React", secondTestPlan);

        mvc.perform(get(uri))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$").value(iterableWithSize(2))
                );
    }

    @Test
    public void getEmptyTopicsList() throws Exception {
        Plan testPlan = createTestPlan("Backend developer");
        String uri = "/api/plans/" + testPlan.getId() + "/topics";
        mvc.perform(get(uri))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$").value(iterableWithSize(0))
                );
    }

    @Test
    public void getTopic() throws Exception {
        Plan testPlan = createTestPlan("Backend developer");
        Topic testTopic = createTestTopic("Java", testPlan);
        String uri = "/api/plans/" + testPlan.getId() + "/topics/" + testTopic.getId();

        mvc.perform(get(uri))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").value(testTopic.getId()),
                        jsonPath("$.createdAt").value(matchesPattern(zonedDateTimeRegex)),
                        jsonPath("$.name").value("Java")
                );
    }

    @Test
    public void getUnknownTopic() throws Exception {
        Plan testPlan = createTestPlan("Backend developer");
        String uri = "/api/plans/" + testPlan.getId() + "/topics/0";
        mvc.perform(get(uri))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.errors").value(iterableWithSize(1))
                );
    }

    @Test
    public void getTopicFromAnotherPlan() throws Exception {
        Plan testPlan = createTestPlan("Backend developer");
        Topic testTopic = createTestTopic("Java", testPlan);
        Plan secondTestPlan = createTestPlan("Frontend developer");
        String uri = "/api/plans/" + secondTestPlan.getId() + "/topics/" + testTopic.getId();
        mvc.perform(get(uri))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.errors").value(iterableWithSize(1))
                );
    }

    @Test
    public void updateTopic() throws Exception {
        Plan testPlan = createTestPlan("Backend developer");
        Topic testTopic = createTestTopic("Java", testPlan);
        String uri = "/api/plans/" + testPlan.getId() + "/topics/" + testTopic.getId();
        Topic updatedTopic = new Topic();
        updatedTopic.setName("Kubernetes");
        String body = super.mapToJson(updatedTopic);

        mvc.perform(put(uri)
                .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(testTopic.getId()),
                        jsonPath("$.createdAt", new ZonedDateTimeMatcher(testTopic.getCreatedAt(), 1, ChronoUnit.MILLIS)),
                        jsonPath("$.name").value("Kubernetes")
                );
    }

    @Test
    public void updateUnknownTopic() throws Exception {
        Plan testPlan = createTestPlan("Backend developer");
        String uri = "/api/plans/" + testPlan.getId() + "/topics/0";
        Topic updatedTopic = new Topic();
        updatedTopic.setName("Kubernetes");
        String body = super.mapToJson(updatedTopic);

        mvc.perform(put(uri)
                .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.errors").value(iterableWithSize(1))
                );
    }

    @Test
    public void updateTopicFromAnotherPlan() throws Exception {
        Plan testPlan = createTestPlan("Backend developer");
        Topic testTopic = createTestTopic("Java", testPlan);
        Plan secondTestPlan = createTestPlan("Frontend developer");
        String uri = "/api/plans/" + secondTestPlan.getId() + "/topics/" + testTopic.getId();
        Topic updatedTopic = new Topic();
        updatedTopic.setName("Kubernetes");
        String body = super.mapToJson(updatedTopic);

        mvc.perform(put(uri)
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.errors").value(iterableWithSize(1))
                );
    }

    @Test
    public void updateTopicWithoutName() throws Exception {
        Plan testPlan = createTestPlan("Backend developer");
        Topic testTopic = createTestTopic("Java", testPlan);
        String uri = "/api/plans/" + testPlan.getId() + "/topics/" + testTopic.getId();
        Topic updatedTopic = new Topic();
        String body = super.mapToJson(updatedTopic);

        mvc.perform(put(uri)
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.errors").value(iterableWithSize(1))
                );
    }

    @Test
    public void deleteTopic() throws Exception {
        Plan testPlan = createTestPlan("Backend developer");
        Topic testTopic = createTestTopic("Java", testPlan);
        String uri = "/api/plans/" + testPlan.getId() + "/topics/" + testTopic.getId();

        mvc.perform(delete(uri)).andExpect(status().isNoContent());

        mvc.perform(get(uri)).andExpect(status().isNotFound());
    }

    @Test
    public void deleteUnknownTopic() throws Exception {
        Plan testPlan = createTestPlan("Backend developer");
        String uri = "/api/plans/" + testPlan.getId() + "/topics/0";

        mvc.perform(delete(uri))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.errors").value(iterableWithSize(1))
                );
    }

    @Test
    public void deleteTopicFromAnotherPlan() throws Exception {
        Plan testPlan = createTestPlan("Backend developer");
        Topic testTopic = createTestTopic("Java", testPlan);
        Plan secondTestPlan = createTestPlan("Frontend developer");
        String uri = "/api/plans/" + secondTestPlan.getId() + "/topics/" + testTopic.getId();

        mvc.perform(delete(uri))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.errors").value(iterableWithSize(1))
                );
    }

    private Topic createTestTopic(String topicName, Plan plan) {
        Topic topic = new Topic();
        topic.setName(topicName);
        topic.setPlan(plan);
        return topicDao.save(topic);
    }

    private Plan createTestPlan(String planName) {
        Plan plan = new Plan();
        plan.setName(planName);
        return planDao.save(plan);
    }

}
