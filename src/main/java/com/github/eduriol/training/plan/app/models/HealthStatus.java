package com.github.eduriol.training.plan.app.models;

import lombok.Data;

@Data
public class HealthStatus {

    public HealthStatus() {
        this.healthy = true;
    }

    private boolean healthy;

}
