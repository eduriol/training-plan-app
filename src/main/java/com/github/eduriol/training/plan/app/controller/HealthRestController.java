package com.github.eduriol.training.plan.app.controller;

import com.github.eduriol.training.plan.app.models.HealthStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/health")
public class HealthRestController {

    private static final Logger logger = LoggerFactory.getLogger(HealthRestController.class);

    @GetMapping
    public HealthStatus health() {
        logger.info("Received health check request");
        HealthStatus healthStatus = new HealthStatus();
        logger.info("Health check completed, all components are healthy");
        return healthStatus;
    }

}
