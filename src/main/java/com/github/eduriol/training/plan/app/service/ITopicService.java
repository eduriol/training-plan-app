package com.github.eduriol.training.plan.app.service;

import com.github.eduriol.training.plan.app.models.domain.Plan;
import com.github.eduriol.training.plan.app.models.domain.Topic;

import java.util.List;

public interface ITopicService {

    Topic save(Topic topic);

    Topic findByIdAndPlanId(Long id, Long planId);

    void delete(Long id);

    List<Topic> findByPlan(Plan plan);
}
