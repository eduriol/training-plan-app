package com.github.eduriol.training.plan.app.dao;

import com.github.eduriol.training.plan.app.models.domain.Plan;
import com.github.eduriol.training.plan.app.models.domain.Topic;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ITopicDao extends CrudRepository<Topic, Long> {

    List<Topic> findAllByPlan(Plan plan);

    Topic findByIdAndPlanId(Long id, Long planId);

}
