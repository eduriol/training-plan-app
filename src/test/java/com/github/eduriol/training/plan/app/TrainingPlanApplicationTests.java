package com.github.eduriol.training.plan.app;

import com.github.eduriol.training.plan.app.dao.ITopicDao;
import com.github.eduriol.training.plan.app.models.HealthStatus;
import com.github.eduriol.training.plan.app.models.domain.Topic;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TrainingPlanApplicationTests extends AbstractTests {

    @Autowired
    private ITopicDao topicDao;

    @Override
    @BeforeAll
    public void setUp() {
        super.setUp();
    }

    @AfterEach
    public void resetDb() {
        topicDao.deleteAll();
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

    @Test
    void createTopic() throws Exception {
        String uri = "/api/topics";
        Topic topic = new Topic();
        topic.setName("Java");
        String body = super.mapToJson(topic);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);
        String content = mvcResult.getResponse().getContentAsString();
        Topic topicResponse = super.mapFromJson(content, Topic.class);
        assertInstanceOf(Long.class, topicResponse.getId());
        assertInstanceOf(Date.class, topicResponse.getCreatedAt());
        assertEquals(topic.getName(), topicResponse.getName());
    }

    @Test
    public void getTopicsList() throws Exception {
        String uri = "/api/topics";
        createTestTopic("Java");
        createTestTopic("Kubernetes");

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Topic[] topicList = super.mapFromJson(content, Topic[].class);
        assertEquals(2, topicList.length);
    }

    @Test
    public void getEmptyTopicsList() throws Exception {
        String uri = "/api/topics";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(204, status);
    }

    @Test
    public void getTopic() throws Exception {
        Topic topic = createTestTopic("Java");
        String uri = "/api/topics/".concat(topic.getId().toString());

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Topic topicResponse = super.mapFromJson(content, Topic.class);
        assertEquals(topic.getId(), topicResponse.getId());
        assertInstanceOf(Date.class, topicResponse.getCreatedAt());
        assertEquals("Java", topicResponse.getName());
    }

    @Test
    public void getUnknownTopic() throws Exception {
        String uri = "/api/topics/0";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    public void updateTopic() throws Exception {
        Topic topic = createTestTopic("Java");
        String uri = "/api/topics/".concat(topic.getId().toString());
        Topic updatedTopic = new Topic();
        updatedTopic.setName("Kubernetes");
        String body = super.mapToJson(updatedTopic);

        MvcResult mvcPutResult = mvc.perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int putResponseStatus = mvcPutResult.getResponse().getStatus();
        assertEquals(200, putResponseStatus);
        String putContent = mvcPutResult.getResponse().getContentAsString();
        Topic putTopicResponse = super.mapFromJson(putContent, Topic.class);
        assertEquals(topic.getId(), putTopicResponse.getId());
        assertEquals(topic.getCreatedAt(), putTopicResponse.getCreatedAt());
        assertEquals("Kubernetes", putTopicResponse.getName());

        MvcResult mvcGetResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int getResponseStatus = mvcGetResult.getResponse().getStatus();
        assertEquals(200, getResponseStatus);
        String getContent = mvcGetResult.getResponse().getContentAsString();
        Topic getTopicResponse = super.mapFromJson(getContent, Topic.class);
        assertEquals(topic.getId(), getTopicResponse.getId());
        assertEquals(topic.getCreatedAt(), getTopicResponse.getCreatedAt());
        assertEquals("Kubernetes", getTopicResponse.getName());

    }

    @Test
    public void updateUnknownTopic() throws Exception {
        String uri = "/api/topics/0";
        Topic updatedTopic = new Topic();
        updatedTopic.setName("Kubernetes");
        String body = super.mapToJson(updatedTopic);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }

    private Topic createTestTopic(String topicName) {
        Topic topic = new Topic();
        topic.setName(topicName);
        return topicDao.save(topic);
    }

}
