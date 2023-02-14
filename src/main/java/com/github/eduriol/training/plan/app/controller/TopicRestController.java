package com.github.eduriol.training.plan.app.controller;

import com.github.eduriol.training.plan.app.exception.NotFoundException;
import com.github.eduriol.training.plan.app.exception.BadRequestException;
import com.github.eduriol.training.plan.app.models.domain.Topic;
import com.github.eduriol.training.plan.app.service.ITopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/topics")
public class TopicRestController {

    private static final Logger logger = LoggerFactory.getLogger(TopicRestController.class);

    @Autowired
    private ITopicService topicService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Topic createTopic(@Valid @RequestBody Topic topic, BindingResult result) {

        logger.info("Received request to create topic: {}", topic);

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "'" + err.getField() + "' " + err.getDefaultMessage())
                    .toList();
            throw new BadRequestException(errors);
        }

        Topic createdTopic = topicService.save(topic);
        logger.info("Response: {}", createdTopic);

        return createdTopic;
    }

    @GetMapping
    public List<Topic> getAllTopics() {
        logger.info("Received request to list all topics");
        List<Topic> topics = topicService.findAll();
        logger.info("Response: {}", topics);
        return topics;
    }

    @GetMapping("/{id}")
    public Topic getTopic(@PathVariable Long id) {

        logger.info("Received request to get topic with id = {}", id);

        Topic topic = topicService.findById(id);

        if (topic == null) {
            throw new NotFoundException(List.of("The topic with id = " + id.toString() + " does not exist."));
        }

        logger.info("Response: {}", topic);

        return topic;

    }

    @PutMapping("/{id}")
    public Topic updateTopic(@PathVariable Long id, @Valid @RequestBody Topic newTopic, BindingResult result) {

        logger.info("Received request to update topic with id = {} with topic: {}", id, newTopic);

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

        topic.setName(newTopic.getName());
        Topic updatedTopic = topicService.save(topic);
        logger.info("Response: {}", updatedTopic);

        return updatedTopic;

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTopic(@PathVariable Long id) {

        logger.info("Received request to delete topic with id = {}", id);

        try {
            topicService.delete(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new NotFoundException(List.of("The topic with id = " + id.toString() + " does not exist."));
        }

        logger.info("Topic with id = {} successfully deleted", id);

    }

}
