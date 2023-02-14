package com.github.eduriol.training.plan.app.dao;

import com.github.eduriol.training.plan.app.models.domain.Plan;
import org.springframework.data.repository.CrudRepository;

public interface IPlanDao extends CrudRepository<Plan, Long> {
}
