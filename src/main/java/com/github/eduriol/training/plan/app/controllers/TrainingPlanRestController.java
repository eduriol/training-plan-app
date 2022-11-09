package com.github.eduriol.training.plan.app.controllers;

import com.github.eduriol.training.plan.app.models.HealthStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TrainingPlanRestController {

    @GetMapping("/health")
    public HealthStatus health() {
        return new HealthStatus();
    }

}
