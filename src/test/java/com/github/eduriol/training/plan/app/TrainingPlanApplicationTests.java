package com.github.eduriol.training.plan.app;

import com.github.eduriol.training.plan.app.dao.ITopicDao;
import com.github.eduriol.training.plan.app.models.domain.Topic;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.temporal.ChronoUnit;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TrainingPlanApplicationTests extends AbstractTests {

    @Autowired
    private ITopicDao topicDao;

    @Override
    @BeforeAll
    public void setUp() {
        super.setUp();
    }

    @BeforeEach
    public void resetDb() {
        topicDao.deleteAll();
    }

    @Test
    void getAppHealthStatus() throws Exception {
        String uri = "/api/health";

        mvc.perform(get(uri))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.healthy").value(true)
                );
    }

    @Test
    void createTopic() throws Exception {
        String uri = "/api/topics";
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
        String uri = "/api/topics";
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
    public void getTopicsList() throws Exception {
        String uri = "/api/topics";
        createTestTopic("Java");
        createTestTopic("Kubernetes");

        mvc.perform(get(uri))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$").value(iterableWithSize(2))
                );
    }

    @Test
    public void getEmptyTopicsList() throws Exception {
        String uri = "/api/topics";
        mvc.perform(get(uri))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$").value(iterableWithSize(0))
                );
    }

    @Test
    public void getTopic() throws Exception {
        Topic topic = createTestTopic("Java");
        String uri = "/api/topics/" + topic.getId();

        mvc.perform(get(uri))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").value(topic.getId()),
                        jsonPath("$.createdAt").value(matchesPattern(zonedDateTimeRegex)),
                        jsonPath("$.name").value("Java")
                );
    }

    @Test
    public void getUnknownTopic() throws Exception {
        String uri = "/api/topics/0";
        mvc.perform(get(uri))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.errors").value(iterableWithSize(1))
                );
    }

    @Test
    public void updateTopic() throws Exception {
        Topic topic = createTestTopic("Java");
        String uri = "/api/topics/" + topic.getId();
        Topic updatedTopic = new Topic();
        updatedTopic.setName("Kubernetes");
        String body = super.mapToJson(updatedTopic);

        mvc.perform(put(uri)
                .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(topic.getId()),
                        jsonPath("$.createdAt", new ZonedDateTimeMatcher(topic.getCreatedAt(), 1, ChronoUnit.MILLIS)),
                        jsonPath("$.name").value("Kubernetes")
                );
    }

    @Test
    public void updateUnknownTopic() throws Exception {
        String uri = "/api/topics/0";
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
        Topic topic = createTestTopic("Java");
        String uri = "/api/topics/" + topic.getId();
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
        Topic topic = createTestTopic("Java");
        String uri = "/api/topics/" + topic.getId();

        mvc.perform(delete(uri)).andExpect(status().isNoContent());

        mvc.perform(get(uri)).andExpect(status().isNotFound());
    }

    @Test
    public void deleteUnknownTopic() throws Exception {
        String uri = "/api/topics/0";

        mvc.perform(delete(uri))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.errors").value(iterableWithSize(1))
                );
    }

    private Topic createTestTopic(String topicName) {
        Topic topic = new Topic();
        topic.setName(topicName);
        return topicDao.save(topic);
    }

}
