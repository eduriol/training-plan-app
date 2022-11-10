package com.github.eduriol.training.plan.app.controller;

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
        return topicService.findAll();
    }

}
