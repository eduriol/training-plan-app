package com.github.eduriol.training.plan.app.controller;

import com.github.eduriol.training.plan.app.exception.BadRequestException;
import com.github.eduriol.training.plan.app.exception.NotFoundException;
import com.github.eduriol.training.plan.app.models.domain.Plan;
import com.github.eduriol.training.plan.app.service.IPlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/plans")
public class PlanRestController {

    private static final Logger logger = LoggerFactory.getLogger(PlanRestController.class);

    @Autowired
    private IPlanService planService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Plan createPlan(@Valid @RequestBody Plan plan, BindingResult result) {

        logger.info("Received request to create plan: {}", plan);

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "'" + err.getField() + "' " + err.getDefaultMessage())
                    .toList();
            throw new BadRequestException(errors);
        }

        plan.setTopics(new ArrayList<>());

        Plan createdPlan = planService.save(plan);
        logger.info("Response: {}", createdPlan);

        return createdPlan;
    }

    @GetMapping
    public List<Plan> getPlans() {
        logger.info("Received request to list all plans");

        List<Plan> plans = planService.findAll();

        logger.info("Response: {}", plans);

        return plans;
    }

    @GetMapping("/{id}")
    public Plan getPlan(@PathVariable Long id) {

        logger.info("Received request to get plan with id = {}", id);

        Plan plan = planService.findById(id);

        if (plan == null) {
            throw new NotFoundException(List.of("The plan with id = " + id.toString() + " does not exist."));
        }

        logger.info("Response: {}", plan);

        return plan;
    }

    @PutMapping("/{id}")
    public Plan updatePlan(@PathVariable Long id, @Valid @RequestBody Plan newPlan, BindingResult result) {

        logger.info("Received request to update plan with id = {} with plan: {}", id, newPlan);

        Plan plan = planService.findById(id);

        if (plan == null) {
            throw new NotFoundException(List.of("The plan with id = " + id.toString() + " does not exist."));
        }

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "'" + err.getField() + "' " + err.getDefaultMessage())
                    .toList();
            throw new BadRequestException(errors);
        }

        plan.setName(newPlan.getName());
        Plan updatedPlan = planService.save(plan);
        logger.info("Response: {}", updatedPlan);

        return updatedPlan;

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlan(@PathVariable Long id) {

        logger.info("Received request to delete plan with id = {}", id);

        Plan plan = planService.findById(id);

        if (plan == null) {
            throw new NotFoundException(List.of("The plan with id = " + id.toString() + " does not exist."));
        }

        planService.delete(id);

        logger.info("Plan with id = {} successfully deleted", id);

    }

}
