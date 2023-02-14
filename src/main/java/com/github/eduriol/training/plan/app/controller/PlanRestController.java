package com.github.eduriol.training.plan.app.controller;

import com.github.eduriol.training.plan.app.exception.BadRequestException;
import com.github.eduriol.training.plan.app.models.domain.Plan;
import com.github.eduriol.training.plan.app.service.IPlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

        Plan createdPlan = planService.save(plan);
        logger.info("Response: {}", createdPlan);

        return createdPlan;
    }

}
