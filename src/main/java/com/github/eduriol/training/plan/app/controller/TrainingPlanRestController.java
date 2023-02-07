package com.github.eduriol.training.plan.app.controller;

import com.github.eduriol.training.plan.app.exception.NoContentException;
import com.github.eduriol.training.plan.app.exception.NotFoundException;
import com.github.eduriol.training.plan.app.models.HealthStatus;
import com.github.eduriol.training.plan.app.models.domain.Topic;
import com.github.eduriol.training.plan.app.service.ITopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TrainingPlanRestController {

    @Autowired
    private ITopicService topicService;

    @GetMapping("/health")
    public HealthStatus health() {
        return new HealthStatus();
    }

    @PostMapping("/topics")
    @ResponseStatus(HttpStatus.CREATED)
    public Topic createTopic(@RequestBody Topic topic) {
        return topicService.save(topic);
    }

    @GetMapping("/topics")
    public List<Topic> getAllTopics() {
        List<Topic> topicsList = topicService.findAll();

        if (topicsList.isEmpty()) {
            throw new NoContentException("The list of topics is empty.");
        }

        return topicsList;
    }

    @GetMapping("/topics/{id}")
    public Topic getTopic(@PathVariable Long id) {
        Topic topic = topicService.findById(id);

        if (topic == null) {
            throw new NotFoundException("The topic with id = ".concat(id.toString()).concat(" does not exist."));
        }

        return topic;

    }

    @PutMapping("/topics/{id}")
    public Topic updateTopic(@PathVariable Long id, @RequestBody Topic updatedTopic) {
        Topic topic = topicService.findById(id);

        if (topic == null) {
            throw new NotFoundException("The topic with id = ".concat(id.toString()).concat(" does not exist."));
        }

        topic.setName(updatedTopic.getName());
        return topicService.save(topic);

    }

}
