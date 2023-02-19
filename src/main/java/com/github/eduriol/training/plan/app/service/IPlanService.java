package com.github.eduriol.training.plan.app.service;

import com.github.eduriol.training.plan.app.models.domain.Plan;

import java.util.List;

public interface IPlanService {

    Plan save(Plan plan);

    Plan findById(Long id);

    void delete(Long id);

    List<Plan> findAll();

}
