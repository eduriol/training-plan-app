package com.github.eduriol.training.plan.app.controller;

import com.github.eduriol.training.plan.app.exception.NotFoundException;
import com.github.eduriol.training.plan.app.exception.BadRequestException;
import com.github.eduriol.training.plan.app.models.HealthStatus;
import com.github.eduriol.training.plan.app.models.domain.Topic;
import com.github.eduriol.training.plan.app.service.ITopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

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
    public Topic createTopic(@Valid @RequestBody Topic topic, BindingResult result) {

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "'" + err.getField() + "' " + err.getDefaultMessage())
                    .toList();
            throw new BadRequestException(errors);
        }

        return topicService.save(topic);
    }

    @GetMapping("/topics")
    public List<Topic> getAllTopics() {
        return topicService.findAll();
    }

    @GetMapping("/topics/{id}")
    public Topic getTopic(@PathVariable Long id) {
        Topic topic = topicService.findById(id);

        if (topic == null) {
            throw new NotFoundException(List.of("The topic with id = " + id.toString() + " does not exist."));
        }

        return topic;

    }

    @PutMapping("/topics/{id}")
    public Topic updateTopic(@PathVariable Long id, @Valid @RequestBody Topic updatedTopic, BindingResult result) {
        Topic topic = topicService.findById(id);

        if (topic == null) {
            throw new NotFoundException(List.of("The topic with id = " + id.toString() + " does not exist."));
        }

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "'" + err.getField() + "' " + err.getDefaultMessage())
                    .toList();
            throw new BadRequestException(errors);
        }

        topic.setName(updatedTopic.getName());
        return topicService.save(topic);

    }

}
