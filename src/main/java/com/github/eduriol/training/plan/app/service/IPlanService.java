package com.github.eduriol.training.plan.app.service;

import com.github.eduriol.training.plan.app.models.domain.Plan;

public interface IPlanService {

    Plan save(Plan plan);

    Plan findById(Long id);
}
