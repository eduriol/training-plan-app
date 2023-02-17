package com.github.eduriol.training.plan.app.controller;

import com.github.eduriol.training.plan.app.exception.NotFoundException;
import com.github.eduriol.training.plan.app.exception.BadRequestException;
import com.github.eduriol.training.plan.app.models.domain.Plan;
import com.github.eduriol.training.plan.app.models.domain.Topic;
import com.github.eduriol.training.plan.app.service.IPlanService;
import com.github.eduriol.training.plan.app.service.ITopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/plans/{plan_id}/topics")
public class TopicRestController {

    private static final Logger logger = LoggerFactory.getLogger(TopicRestController.class);

    @Autowired
    private ITopicService topicService;

    @Autowired
    private IPlanService planService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Topic createTopic(@PathVariable Long plan_id, @Valid @RequestBody Topic topic, BindingResult result) {

        logger.info("Received request to create topic: {} in plan with id = {}", topic, plan_id);

        Plan plan = planService.findById(plan_id);

        if (plan == null) {
            throw new NotFoundException(List.of("The plan with id = " + plan_id.toString() + " does not exist."));
        }

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "'" + err.getField() + "' " + err.getDefaultMessage())
                    .toList();
            throw new BadRequestException(errors);
        }

        topic.setPlan(plan);
        Topic createdTopic = topicService.save(topic);
        logger.info("Response: {}", createdTopic);

        return createdTopic;
    }

    @GetMapping
    public List<Topic> getTopicsFromPlan(@PathVariable Long plan_id) {
        logger.info("Received request to list all topics in plan with id = {}", plan_id);

        Plan plan = planService.findById(plan_id);

        if (plan == null) {
            throw new NotFoundException(List.of("The plan with id = " + plan_id.toString() + " does not exist."));
        }

        List<Topic> topics = topicService.findByPlan(plan);
        logger.info("Response: {}", topics);
        return topics;
    }

    @GetMapping("/{id}")
    public Topic getTopic(@PathVariable Long id, @PathVariable Long plan_id) {

        logger.info("Received request to get topic with id = {} for plan with id = {}", id, plan_id);

        Topic topic = topicService.findByIdAndPlanId(id, plan_id);

        if (topic == null) {
            throw new NotFoundException(List.of("The topic with id = " + id.toString() + " and plan id = " + plan_id.toString() + " does not exist."));
        }

        logger.info("Response: {}", topic);

        return topic;
    }

    @PutMapping("/{id}")
    public Topic updateTopic(@PathVariable Long id, @PathVariable Long plan_id, @Valid @RequestBody Topic newTopic, BindingResult result) {

        logger.info("Received request to update topic with id = {} and plan id = {} with topic: {}", id, plan_id, newTopic);

        Topic topic = topicService.findByIdAndPlanId(id, plan_id);

        if (topic == null) {
            throw new NotFoundException(List.of("The topic with id = " + id.toString() + " and plan id = " + plan_id.toString() + " does not exist."));
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
    public void deleteTopic(@PathVariable Long id, @PathVariable Long plan_id) {

        logger.info("Received request to delete topic with id = {} in plan with id = {}", id, plan_id);

        Topic topic = topicService.findByIdAndPlanId(id, plan_id);

        if (topic == null) {
            throw new NotFoundException(List.of("The topic with id = " + id.toString() + " and plan id = " + plan_id.toString() + " does not exist."));
        }

        topicService.delete(id);

        logger.info("Topic with id = {} in plan with id = {} successfully deleted", id, plan_id);

    }

}
